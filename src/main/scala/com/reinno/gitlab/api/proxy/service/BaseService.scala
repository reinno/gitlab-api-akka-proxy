package com.reinno.gitlab.api.proxy.service

import akka.actor.{ActorLogging, Stash, Actor}


trait ServiceMsg

object BaseService {
  sealed trait Msg
}

trait BaseService extends Actor with Stash with ActorLogging


