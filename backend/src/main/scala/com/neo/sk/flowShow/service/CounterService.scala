package com.neo.sk.flowShow.service

import akka.http.scaladsl.server.Directives._
import io.circe.Error
import org.slf4j.LoggerFactory
import com.neo.sk.utils.CirceSupport
import io.circe.generic.auto._
import akka.actor.ActorRef


/**
  * User: Taoz
  * Date: 1/16/2017
  * Time: 3:46 PM
  */
object CounterService {

  private val log = LoggerFactory.getLogger(this.getClass)

}

trait CounterService extends CirceSupport {

  import com.neo.sk.flowShow.ptcl
  import com.neo.sk.flowShow.ptcl._

  private var counter = 0

  val receiveDataActor : ActorRef


  import CounterService.log
  //import io.circe._


  val counterRouter = pathPrefix("counter") {

    ( path("hiPage") | path("helloPage") & get) {
      getFromResource("html/index.html")
    } ~ (path("plus") & post) {
      entity(as[Either[Error, ptcl.Plus]]) {
        case Right(p) =>
          counter += p.value
          if(counter > 10) {
            counter = 10
          }
          complete(ptcl.CounterRsp(CounterData(counter, System.currentTimeMillis())))
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(ptcl.CommonRsp(1, s"error: $error"))
      }
    } ~ (path("minus") & post) {
      entity(as[Either[Error, ptcl.Minus]]) {
        case Right(m) =>
          counter -= m.value
          if(counter < 0) {
            counter = 0
          }
          complete(ptcl.CounterRsp(CounterData(counter, System.currentTimeMillis())))
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(ptcl.CommonRsp(1, s"error: $error"))
      }
    } ~ (path("get") & get) {
      complete(ptcl.CounterRsp(CounterData(counter, System.currentTimeMillis())))
    }

  }


}
