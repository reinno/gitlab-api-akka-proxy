package com.reinno.gitlab.api.proxy.route


import scala.concurrent.ExecutionContext

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer


trait ApiRouter {
  implicit val ec: ExecutionContext
  val actorSystem: ActorSystem

  def serviceRouters: Route = ???

  def route: Route = {
    serviceRouters
  }
}

class ApiRouterService(val actorSystem: ActorSystem,
                       val mat: ActorMaterializer)
  extends ApiRouter {
  implicit val ec: ExecutionContext = actorSystem.dispatcher
}
