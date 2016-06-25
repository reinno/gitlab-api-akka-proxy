package com.reinno.gitlab.api.proxy.route

import akka.actor.ActorRef
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.reinno.gitlab.api.proxy.service.ApiMaster.GetProjectIssueNotesNum
import com.reinno.gitlab.api.proxy.util.ActorUtil.askActor

import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure}


class ProjectRouter(apiMaster: ActorRef)(implicit ec: ExecutionContext) extends BaseRouter {
  override def doRoute(implicit mat: Materializer): Route = {
    pathPrefix("projects" / IntNumber) { projectId =>
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
