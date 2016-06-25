package com.reinno.gitlab.api.proxy.route

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.reinno.gitlab.api.proxy.util.{Constants, LogUtil}

trait BaseRouter {

  protected def doRoute(implicit mat: Materializer): Route
  protected def prefix = Slash ~ "api" / s"${Constants.REST_VERSION}"
  protected val LOG = LogUtil.getLogger(getClass)

  def route: Route = encodeResponse {
    extractMaterializer {implicit mat =>
      rawPathPrefix(prefix) {
        doRoute(mat)
      }
    }
  }
}


trait GitLabEnhanceRouter extends BaseRouter {

  protected def doRouteWithToken(token: String)(implicit mat: Materializer): Route

  override final def doRoute(implicit mat: Materializer): Route = {
    extractRequest {
      context =>
        context.headers.find(_.name == "PRIVATE-TOKEN") match {
          case Some(head) =>
            doRouteWithToken(head.value)
          case None =>
            complete(HttpResponse(StatusCodes.Unauthorized))
        }
    }
  }
}
