package com.reinno.gitlab.api.proxy.route

import akka.http.scaladsl.Http
import akka.stream.scaladsl.{Sink, Source}
import com.reinno.gitlab.api.proxy.util.Constants

import scala.concurrent.ExecutionContext

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._


trait ApiRouter {
  implicit val ec: ExecutionContext
  implicit val mat: ActorMaterializer
  val actorSystem: ActorSystem
  protected def prefix = Slash ~ "api" / s"${Constants.REST_VERSION}"

  def serviceRouters: Route = encodeResponse {
    extractMaterializer {implicit mat =>
      rawPathPrefix(prefix) {
        ???
      }
    }
  }

  def transportRouters: Route = {
    context =>
      val request = context.request
      println("Opening connection to " + request.uri.authority.host.address)
      val flow = Http(actorSystem).outgoingConnectionHttps("www.gitlab.com")
      Source.single(context.request.withUri(request.uri.withAuthority("gitlab.com", 443)))
        .via(flow)
        .runWith(Sink.head)
        .flatMap(r => context.complete(r))
  }

  def route: Route = {
    transportRouters
  }
}

class ApiRouterService(val actorSystem: ActorSystem,
                       val mat: ActorMaterializer)
  extends ApiRouter {
  implicit val ec: ExecutionContext = actorSystem.dispatcher
}
