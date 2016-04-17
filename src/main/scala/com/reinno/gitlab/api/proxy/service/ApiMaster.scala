package com.reinno.gitlab.api.proxy.service

import akka.actor.{ActorSystem, ActorLogging, Stash, Actor}


object ApiMaster {
  sealed trait ApiMsterRecvMsg
  case class GetProjectIssueNotesNum(projectId: Int) extends ApiMasterSendMsg

  sealed trait ApiMasterSendMsg
  case object UnknownMsg extends ApiMasterSendMsg
}

class ApiMaster(actorSystem: ActorSystem) extends Actor with Stash with ActorLogging {
  import ApiMaster._

  override def receive: Receive = initializing

  def initializing: Receive = {
    case msg =>
      log.debug("initializing , stash all msgs...")
      stash()
  }

  def running: Receive = {
    case msg:GetProjectIssueNotesNum =>
      log.info(s"get project:${msg.projectId} issue comments num")

    case msg =>
      log.error(s"unknown msg: $msg")
      sender ! UnknownMsg
  }
}
