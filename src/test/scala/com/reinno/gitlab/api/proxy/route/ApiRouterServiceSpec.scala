package com.reinno.gitlab.api.proxy.route

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import com.reinno.gitlab.api.proxy.helper.HttpClientHelperDefaultEmptyResponse
import com.reinno.gitlab.api.proxy.service.ApiMaster
import com.reinno.gitlab.api.proxy.util.LogUtil
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.concurrent.duration._
import scala.language.postfixOps


class ApiRouterHttpClientSenderStub(implicit val system: ActorSystem)
  extends HttpClientHelperDefaultEmptyResponse

class ApiRouterServiceSpec extends FlatSpec with ScalatestRouteTest with Matchers with BeforeAndAfterAll {
  implicit val httpClient = new ApiRouterHttpClientSenderStub()
  val LOG = LogUtil.getLogger(getClass)
  val apiMaster = system.actorOf(ApiMaster.props(), "ApiMaster")

  it should "transparent transfer msg" in {
    val route = new ApiRouterService(system, apiMaster).route
    implicit val customTimeout = RouteTestTimeout(15 seconds)

    addHeader("PRIVATE-TOKEN", "xxx")(Get(s"/api/v3/projects")) ~> route ~> check {
      LOG.info(responseAs[String])
      assert(status == StatusCodes.Unauthorized)
    }
  }

  it should "handle ex api" in {
    val route = new ApiRouterService(system, apiMaster).route
    implicit val customTimeout = RouteTestTimeout(15 seconds)

    addHeader("PRIVATE-TOKEN", "xxx")(Get(s"/api/v3/projects/123/issues/notes_num")) ~> route ~> check {
      LOG.info(responseAs[String])
      assert(status == StatusCodes.OK)
    }
  }

  it should "fail without token" in {
    val route = new ApiRouterService(system, apiMaster).route
    implicit val customTimeout = RouteTestTimeout(15 seconds)

    Get(s"/api/v3/projects/123/issues/notes_num") ~> route ~> check {
      LOG.info(responseAs[String])
      assert(status == StatusCodes.Unauthorized)
    }
  }
}
