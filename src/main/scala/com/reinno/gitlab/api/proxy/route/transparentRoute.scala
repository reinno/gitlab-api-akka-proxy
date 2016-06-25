package com.reinno.gitlab.api.proxy.route

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route


import com.reinno.gitlab.api.proxy.util.Constants

import scala.concurrent.ExecutionContext

class TransparentRoute(actorSystem: ActorSystem)(implicit ec: ExecutionContext) extends BaseRouter {
  override def doRoute(implicit mat: Materializer): Route = {
    context =>
      val request = context.request.withUri(context.request.uri.withAuthority(Constants.GITLAB_HOST, Constants.GITLAB_PORT).withScheme("https"))
      LOG.info("Opening connection to " + request.uri.authority.host.address)
      val flow = Http(actorSystem).outgoingConnectionHttps(Constants.GITLAB_HOST)
      Source.single(request)
        .via(flow)
        .runWith(Sink.head)
        .flatMap(r => context.complete(r))
  }
}
