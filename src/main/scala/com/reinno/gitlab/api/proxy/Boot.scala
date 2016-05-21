package com.reinno.gitlab.api.proxy

import com.reinno.gitlab.api.proxy.service.ApiMaster

import scala.concurrent.Await
import scala.concurrent.duration._

import akka.actor.{Props, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.reinno.gitlab.api.proxy.route.ApiRouterService
import com.reinno.gitlab.api.proxy.util.Constants

import scala.language.postfixOps

object Boot extends App {
  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("gitlab-api-proxy")
  implicit val timeout = Timeout(10 seconds)

  val apiMaster = system.actorOf(Props[ApiMaster], "ApiMaster")

  implicit val mat = ActorMaterializer()
  val service = new ApiRouterService(system, mat, apiMaster)


  val bindFuture = Http().bindAndHandle(Route.handlerFlow(service.route),
    Constants.SERVER_HOST, Constants.SERVER_PORT)

  Await.result(bindFuture, 15 seconds)
  Await.result(system.whenTerminated, Duration.Inf)
}
