package com.reinno.gitlab.api.proxy.route

import scala.concurrent.duration._

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.testkit.{ScalatestRouteTest, RouteTestTimeout}

import org.scalatest.{BeforeAndAfterAll, Matchers, FlatSpec}

class ApiRouterServiceSpec extends FlatSpec with ScalatestRouteTest with Matchers with BeforeAndAfterAll {
  implicit def actorSystem: ActorSystem = system
  implicit val mat = ActorMaterializer()

  it should "tranport transfer msg" in {
    val route = new ApiRouterService(system, mat).route
    implicit val customTimeout = RouteTestTimeout(15 seconds)

    addHeader("PRIVATE-TOKEN", "xxx")(Get(s"/api/v3/user")) ~> route ~>  check {
      println(responseAs[String])
      assert(status.intValue() == 200)
    }

  }
}
