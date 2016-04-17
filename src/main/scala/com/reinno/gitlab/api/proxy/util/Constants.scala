package com.reinno.gitlab.api.proxy.util

import java.util.concurrent.TimeUnit

object Constants {
  val REST_VERSION = "v3"
  val GITLAB_HOST = "gitlab.com"
  val GITLAB_PORT = 80

  val SERVER_PORT = 8001
  val SERVER_HOST = "0.0.0.0"

  val FUTURE_TIMEOUT = akka.util.Timeout(15, TimeUnit.SECONDS)
}