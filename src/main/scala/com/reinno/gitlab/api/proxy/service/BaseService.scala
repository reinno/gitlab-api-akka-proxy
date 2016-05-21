package com.reinno.gitlab.api.proxy.service

import akka.actor.{ActorLogging, Stash, Actor}

/**
  * Created by w00140031 on 2016/5/21.
  */
trait BaseService extends Actor with Stash with ActorLogging
