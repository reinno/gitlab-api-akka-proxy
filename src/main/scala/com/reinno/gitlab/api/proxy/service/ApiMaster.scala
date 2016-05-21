package com.reinno.gitlab.api.proxy.service

import akka.actor._
import akka.http.scaladsl.model.HttpRequest

object ApiMaster {
  sealed trait ApiMasterRecvMsg {
    //val srcRequest: HttpRequest
  }
  case class GetProjectIssueNotesNum(projectId: Int) extends ApiMasterRecvMsg

  sealed trait ApiMasterSendMsg
  case object UnknownMsg extends ApiMasterSendMsg

  def props()(implicit httpClientItf: HttpClientSender): Props = Props(new ApiMaster())
}

class ApiMaster()(implicit val httpClient: HttpClientSender) extends BaseService with HttpClientService {
  import ApiMaster._

  override def receive: Receive = running

  def initializing: Receive = {
    case msg =>
      log.debug("initializing , stash all msgs...")
      stash()
  }

  def running: Receive = {
    case msg: GetProjectIssueNotesNum =>
      log.info(s"get project:${msg.projectId} issue comments num")
      sender() ! 0

    case msg =>
      log.error(s"unknown msg: $msg")
      sender ! UnknownMsg
  }
}
