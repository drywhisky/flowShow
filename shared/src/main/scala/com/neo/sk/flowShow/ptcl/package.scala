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

  case class ComeIn(data:String) extends WebSocketMsg

  case class GetOut(data:String) extends WebSocketMsg


  sealed trait DataFromNyx

  case class CountData(count: Int, timestamp: Long)

  case class RealTimeDataRsp(flow: List[CountData], groupId: String, max: Int, now: Int, total: Int, msg: String, errCode: Int) extends DataFromNyx

  case class DurationData(duration: Int, num: Int)

  case class ResidentsDataRsp(errCode: Int, msg: String, groupId: String, data: List[DurationData]) extends DataFromNyx

  case class BrandsData(brandId: Int, name: String, num: Int)

  case class BrandsDataRsp(data: List[BrandsData], groupId: String, msg: String, errCode: Int) extends  DataFromNyx

  case class FrequencyData(count:Int, frequency:String)

  case class FrequencyDataRsp(visitFrequency: List[FrequencyData], groupId: String, msg: String, errCode: Int) extends DataFromNyx

  case class RatioData(date:String, newRatio:Int, oldRatio:Int)

  case class RatioDataRsp(ratio:List[RatioData], groupId: String, msg: String, errCode: Int) extends DataFromNyx


  sealed trait RspResult

  case class ResidentsInfo(groupId: String, data: List[DurationData]) extends RspResult

  case class BrandsInfo(groupId: String, data: List[BrandsData]) extends RspResult

  case class FrequencyInfo(groupId: String, visitFrequency: List[FrequencyData]) extends RspResult

  case class RatioInfo(groupId: String, ratio:List[RatioData]) extends RspResult

  case class RealTimeInfo(flow: List[CountData], groupId: String, max: Int, now: Int, total: Int) extends RspResult


  case class ResidentsInfoRsp(data: Option[List[ResidentsInfo]], msg: String, errCode: Int) extends RspResult

  case class BrandsInfoRsp(data: Option[List[BrandsInfo]], msg: String, errCode: Int) extends RspResult

  case class FrequencyInfoRsp(data: Option[List[FrequencyInfo]], msg: String, errCode: Int) extends RspResult

  case class RatioInfoRsp(data: Option[List[RatioInfo]], msg: String, errCode: Int) extends RspResult

  case class RealTimeInfoRsp(data: Option[List[RealTimeInfo]], msg: String, errCode: Int) extends RspResult



}
