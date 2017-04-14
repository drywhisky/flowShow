package com.neo.sk.flowShow.core

import akka.actor.{Actor, ActorRef, Props, Stash}
import com.neo.sk.flowShow.common.AppSettings
import org.slf4j.LoggerFactory

/**
  * Created by whisky on 17/4/14.
  */
object GroupActor {

  def props(id:String, storeRouter: ActorRef, fileSaver: ActorRef) = Props[GroupActor](new GroupActor(id, storeRouter, fileSaver))

}

object StatisticSymbol {
  val stayTime = "StayTimer"
  val flow = "Flower"
  val visitFreq = "VisitFreqer"
  val newOldRatio = "NewOldRatior"
  val conversionRate = "ConversionRate"
  val brand = "Brand"
}


class GroupActor(id:String, storeRouter: ActorRef, fileSaver: ActorRef) extends Actor with Stash{

  import GroupActor._

  private[this] val log = LoggerFactory.getLogger(this.getClass)
  private[this] val logPrefix = context.self.path

  private[this] val selfRef = context.self

  private[this] def terminate(cause:String) = {
    log.info(s"$logPrefix will terminate because $cause.")
    context.stop(selfRef)
  }

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.info(s"$logPrefix starts.")
  }

  override def postStop(): Unit = {
    log.info(s"$logPrefix stops.")
  }

  def getStatisticActor(symbol: String, historyDurationLength: Int): ActorRef = {
    val name = s"$symbol"
//    context.child(name).getOrElse {
//
//      val actor = symbol match {
//        case StatisticSymbol.stayTime =>
//          StayTimer.props(storeRouter, historyDurationLength)// create new statistic worker
//        case StatisticSymbol.flow =>
//          Counter.props(storeRouter, fileSaver, historyDurationLength)
//        case StatisticSymbol.visitFreq =>
//          VisitFrequency.props(unitId, storeRouter, historyDurationLength)
//        case StatisticSymbol.newOldRatio =>
//          NewOldRatio.props(unitId, storeRouter, historyDurationLength)
//        case StatisticSymbol.conversionRate =>
//          ConversionRate.props(unitId,storeRouter)
//        case StatisticSymbol.brand =>
//          Brand.props(storeRouter, historyDurationLength)
//      }
//      val child = context.actorOf(actor, symbol)
//      log.info(s"$logPrefix $name is starting.")
//      context.watch(child)
//      child
//    }
  }

  def getRealTimeActors(historyDurationLength: Int)= {
    val actors = List(
      getStatisticActor(StatisticSymbol.flow, historyDurationLength),
      getStatisticActor(StatisticSymbol.stayTime, historyDurationLength)
      //      getStatisticActor(StatisticSymbol.newOldRatio, historyDurationLength)
    )
    if(true)
      List(getStatisticActor(StatisticSymbol.brand, historyDurationLength)) ++ actors
    else actors
  }

  def getHistoryActors(historyDurationLength: Int): List[ActorRef] = {
    List(
      getStatisticActor(StatisticSymbol.conversionRate, historyDurationLength),
      getStatisticActor(StatisticSymbol.visitFreq, historyDurationLength),
      getStatisticActor(StatisticSymbol.brand, historyDurationLength)
    )
  }

  def getActorsWhoNeedDurationLength(historyDurationLength: Int): List[ActorRef] = {
    List(
      getStatisticActor(StatisticSymbol.flow, historyDurationLength),
      getStatisticActor(StatisticSymbol.stayTime, historyDurationLength),
      getStatisticActor(StatisticSymbol.brand, historyDurationLength)
    )
  }

  override def receive = init()

  def init() : Receive = {

  }


}
