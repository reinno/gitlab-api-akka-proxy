package com.reinno.gitlab.api.proxy.route

import akka.stream.Materializer
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

import com.reinno.gitlab.api.proxy.util.{LogUtil, Constants}

trait BaseRouter {
  protected def doRoute(implicit mat: Materializer): Route
  protected def prefix = Slash ~ "api" / s"${Constants.REST_VERSION}"
  protected val LOG = LogUtil.getLogger(getClass)

  def route: Route
}
