package com.neo.sk.flowShow

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.dispatch.MessageDispatcher
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.routing.RoundRobinPool
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.language.postfixOps
import scala.util.{Failure, Success}
import com.neo.sk.flowShow.service.HttpService
import com.neo.sk.flowShow.core.{AssistedDataActor, GroupManager, ReceiveDataActor, WsClient}

/**
  * User: Taoz
  * Date: 11/16/2016
  * Time: 1:00 AM
  */
object Boot extends HttpService {


  import concurrent.duration._
  import com.neo.sk.flowShow.common.AppSettings._


  override implicit val system = ActorSystem("appSystem", config)
  // the executor should not be the default dispatcher.
  override implicit val executor: MessageDispatcher =
    system.dispatchers.lookup("akka.actor.my-blocking-dispatcher")

  override implicit val materializer = ActorMaterializer()

  override val timeout = Timeout(20 seconds) // for actor asks

  val log: LoggingAdapter = Logging(system, getClass)

//  override val receiveDataActor = system.actorOf(ReceiveDataActor.props(),"receiveDataActor")
//
//  override val assistedDataActor = system.actorOf(AssistedDataActor.props(), "assistedDataActor")

  val wsClient = system.actorOf(WsClient.props(system, materializer, executor), "wsClient")

  val groupManager = system.actorOf(GroupManager.props(wsClient), "groupManager")

  def main(args: Array[String]) {
    log.info("Starting.")
    val binding = Http().bindAndHandle(routes, httpInterface, httpPort)
    binding.onComplete {
      case Success(b) ⇒
        val localAddress = b.localAddress
        println(s"Server is listening on ${localAddress.getHostName}:${localAddress.getPort}")
      case Failure(e) ⇒
        println(s"Binding failed with ${e.getMessage}")
        system.terminate()
        System.exit(-1)
    }
  }



}
