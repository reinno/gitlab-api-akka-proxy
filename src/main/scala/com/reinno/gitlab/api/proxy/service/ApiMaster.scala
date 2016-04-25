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

  def props(actorSystem: ActorSystem): Props = Props(new ApiMaster(actorSystem))
}

class ApiMaster(actorSystem: ActorSystem) extends Actor with Stash with ActorLogging {
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
