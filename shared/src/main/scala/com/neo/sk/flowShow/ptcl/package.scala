package com.neo.sk.flowShow

/**
  * User: Taoz
  * Date: 1/16/2017
  * Time: 3:50 PM
  */
package object ptcl {


  trait Request

  case class Plus(value: Int) extends Request
  case class Minus(value: Int) extends Request



  trait Response{
    val errCode: Int
    val msg: String
  }


  case class CommonRsp( errCode: Int = 0, msg: String = "ok") extends Response

  case class CounterData(value: Int, timestamp: Long)

  case class CounterRsp(
    data: CounterData,
    errCode: Int = 0,
    msg: String = "ok"
  ) extends Response


  sealed trait WebSocketMsg

  //"heartbeat"
  case class Heartbeat(id: String) extends WebSocketMsg




}
