package com.reinno.gitlab.api.proxy

import scala.concurrent.Await
import scala.concurrent.duration._

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.reinno.gitlab.api.proxy.route.ApiRouterService

object Boot {
  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("gitlab-api-proxy")
  implicit val timeout = Timeout(10 seconds)

  implicit val mat = ActorMaterializer()
  val service = new ApiRouterService(system, mat)

  val bindFuture = Http().bindAndHandle(Route.handlerFlow(service.route), "0.0.0.0", 8001)
  Await.result(bindFuture, 15 seconds)

  Await.result(system.whenTerminated, Duration.Inf)
}
