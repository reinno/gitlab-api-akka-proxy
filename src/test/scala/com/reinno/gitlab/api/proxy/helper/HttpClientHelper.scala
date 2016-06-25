package com.reinno.gitlab.api.proxy.helper

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpResponse, HttpRequest}
import com.reinno.gitlab.api.proxy.service.{HttpClientSender, HttpClientService}
import org.json4s.{DefaultFormats, jackson}
import org.scalatest.Matchers

import scala.concurrent.Future


trait HttpClientHelper extends HttpClientSender with Matchers {
  implicit val formats = DefaultFormats
  implicit val serialization = jackson.Serialization
  implicit val system: ActorSystem

  def sendHttpRequestPartial: PartialFunction[HttpRequest, Future[HttpResponse]]

  override def sendHttpReq(req: HttpRequest): Future[HttpResponse] = {
    sendHttpRequestPartial(req)
  }
}

trait HttpClientHelperDefaultEmptyResponse extends HttpClientHelper {
  import system.dispatcher

  override def sendHttpRequestPartial: PartialFunction[HttpRequest, Future[HttpResponse]] = {
    case x => Future(HttpResponse())
  }
}