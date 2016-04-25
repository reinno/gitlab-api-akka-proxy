package com.reinno.gitlab.api.proxy.route

import com.reinno.gitlab.api.proxy.service.ApiMaster
import com.reinno.gitlab.api.proxy.util.LogUtil

import scala.concurrent.duration._

import akka.actor.{Props, ActorSystem}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.testkit.{ScalatestRouteTest, RouteTestTimeout}

import org.scalatest.{BeforeAndAfterAll, Matchers, FlatSpec}

class ApiRouterServiceSpec extends FlatSpec with ScalatestRouteTest with Matchers with BeforeAndAfterAll {
  implicit def actorSystem: ActorSystem = system
  implicit val mat = ActorMaterializer()
  val LOG = LogUtil.getLogger(getClass)
  val apiMaster = system.actorOf(ApiMaster.props(actorSystem), "ApiMaster")

  it should "tranport transfer msg" in {
    val route = new ApiRouterService(system, mat, apiMaster).route
    implicit val customTimeout = RouteTestTimeout(15 seconds)

    addHeader("PRIVATE-TOKEN", "xxx")(Get(s"/api/v3/projects")) ~> route ~> check {
      LOG.info(responseAs[String])
      assert(status.intValue() == 200)
    }
  }

  it should "handle ex api" in {
    val route = new ApiRouterService(system, mat, apiMaster).route
    implicit val customTimeout = RouteTestTimeout(15 seconds)

    addHeader("PRIVATE-TOKEN", "xxx")(Get(s"/api/v3/projects/123/issues/notes_num")) ~> route ~> check {
      LOG.info(responseAs[String])
      assert(status.intValue() == 200)
    }
  }
}
