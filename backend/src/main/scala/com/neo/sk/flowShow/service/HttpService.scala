package com.neo.sk.flowShow.service

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.util.Timeout
import scala.concurrent.ExecutionContextExecutor

/**
  * User: Taoz
  * Date: 8/26/2016
  * Time: 10:27 PM
  */
trait HttpService extends ResourceService
                          with TestService
                          with WsService
                          with UserService {


  implicit val system: ActorSystem

  implicit val executor: ExecutionContextExecutor

  implicit val materializer: Materializer

  implicit val timeout: Timeout
  

  val routes: Route =
    pathPrefix("flowShow") {
      resourceRoutes ~ testRoute ~ wsRoutes ~ userRoutes
    }




}
