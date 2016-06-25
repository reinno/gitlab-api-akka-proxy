package com.reinno.gitlab.api.proxy.service

import akka.actor.{ActorLogging, Stash, Actor}


trait ServiceMsg

object BaseService {
  sealed trait Msg
}

trait BaseService extends Actor with Stash with ActorLogging


object GitLabEnhanceService {
  sealed trait Msg {
    val token: String
  }

  case class GetProjectIssueNotesNum(token: String, projectId: Int) extends Msg
}


