package com.reinno.gitlab.api.proxy.route

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.reinno.gitlab.api.proxy.service.GitLabEnhanceService
import com.reinno.gitlab.api.proxy.util.ActorUtil.askActor

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}


class ProjectRouter(apiMaster: ActorRef)(implicit ec: ExecutionContext) extends GitLabEnhanceRouter {
  override def doRouteWithToken(token: String)(implicit mat: Materializer): Route = {
    pathPrefix("projects" / IntNumber) { projectId =>
      path("issues" / "notes_num") {
        onComplete(askActor[Int](apiMaster,
          GitLabEnhanceService.GetProjectIssueNotesNum(token, projectId))) {
          case Success(value) =>
            complete(s"issue_num: $value")
          case Failure(ex) =>
            failWith(ex)
        }
      }
    }
  }
}
