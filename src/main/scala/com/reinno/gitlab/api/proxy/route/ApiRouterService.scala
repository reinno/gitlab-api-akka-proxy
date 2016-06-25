package com.reinno.gitlab.api.proxy.route

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext


trait ApiRouter {
  val actorSystem: ActorSystem
  val apiMaster: ActorRef

  implicit val ec: ExecutionContext = actorSystem.dispatcher

  val projectService = new ProjectRouter(apiMaster)
  val transportRoute = new TransparentRoute(actorSystem)

  def route: Route = {
     projectService.route ~ transportRoute.route
  }
}

class ApiRouterService(override val actorSystem: ActorSystem,
                       override val apiMaster: ActorRef) extends ApiRouter
