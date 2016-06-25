package com.reinno.gitlab.api.proxy.service

import akka.actor._

object ApiMaster {
  def props()(implicit httpClientItf: HttpClientSender): Props = Props(new ApiMaster())
}

class ApiMaster()(implicit val httpClient: HttpClientSender) extends BaseService with HttpClientService {

  override def receive: Receive = running

  def initializing: Receive = {
    case msg =>
      log.debug("initializing , stash all msgs...")
      stash()
  }

  def running: Receive = {
    case msg@GitLabEnhanceService.GetProjectIssueNotesNum(token, id) =>
      log.info(s"get project:$id issue comments num")
      sender() ! 0

    case msg =>
      log.error(s"unknown msg: $msg")
  }
}
