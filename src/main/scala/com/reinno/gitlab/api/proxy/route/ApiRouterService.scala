package com.reinno.gitlab.api.proxy.route

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

import akka.actor.{ActorRef, ActorSystem}

import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

import com.reinno.gitlab.api.proxy.service.ApiMaster.GetProjectIssueNotesNum
import com.reinno.gitlab.api.proxy.util.{LogUtil, Constants}
import com.reinno.gitlab.api.proxy.util.ActorUtil._




trait ApiRouter {
  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val ec: ExecutionContext
  implicit val mat: ActorMaterializer
  val actorSystem: ActorSystem
  val apiMaster: ActorRef
  val LOG = LogUtil.getLogger(getClass)

  protected def prefix = Slash ~ "api" / s"${Constants.REST_VERSION}"

  def serviceRouters: Route = encodeResponse {
    extractMaterializer {implicit mat =>
      rawPathPrefix(prefix) {
        pathPrefix("projects" / IntNumber) { projectId => {
            path("issues" / "notes_num") {
              onComplete(askActor[Int](apiMaster, GetProjectIssueNotesNum(projectId))) {
                case Success(value) =>
                  complete(s"issue_num: $value")
                case Failure(ex) =>
                  failWith(ex)
              }
            }
          }
        }
      }
    }
  }

  def transportRouters: Route = {
    context =>
      val request = context.request.withUri(context.request.uri.withAuthority(Constants.GITLAB_HOST, Constants.GITLAB_PORT).withScheme("https"))
      LOG.info("Opening connection to " + request.uri.authority.host.address)
      val flow = Http(actorSystem).outgoingConnectionHttps(Constants.GITLAB_HOST)
      Source.single(request)
        .via(flow)
        .runWith(Sink.head)
        .flatMap(r => context.complete(r))
  }

  def route: Route = {
    transportRouters
  }
}

class ApiRouterService(val actorSystem: ActorSystem,
                       val mat: ActorMaterializer,
                       val apiMaster: ActorRef)
  extends ApiRouter {
  implicit val ec: ExecutionContext = actorSystem.dispatcher
}
