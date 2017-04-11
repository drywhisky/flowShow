package com.neo.sk.flowShow.core

import akka.actor.ActorRef
import akka.event.{EventBus, SubchannelClassification}
import akka.util.Subclassification
import com.neo.sk.flowShow.ptcl.{ComeIn, GetOut}

/**
  * Created by dry on 2017/4/5.
  */
object DataBus {
  val ALL_CLASSIFY = "ALL"

  sealed trait PushData
}

class DataBus extends EventBus with SubchannelClassification {
  import DataBus._

  override type Event = (String, String)
  override type Classifier = String
  override type Subscriber = ActorRef

  //  type(1 is come , 0 is leave)
  override protected def publish(event: Event, subscriber: Subscriber): Unit = {
    if(event._1.toInt == 1) subscriber ! ComeIn(event._2)
    else if(event._1.toInt == 0) subscriber ! GetOut(event._2)
    else subscriber ! event._2
  }


  override protected def classify(event: Event): Classifier = event._1

  override protected implicit def subclassification : Subclassification[Classifier] = Subclassification

  def subscribeAll(subscriber: Subscriber) = this.subscribe(subscriber, ALL_CLASSIFY)

  val Subclassification = new Subclassification[String] {
    override def isEqual(x: String, y: String): Boolean = y == x

    override def isSubclass(x: String, y: String): Boolean = y.startsWith(x)
  }

}
