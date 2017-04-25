package com.neo.sk.flowShow

/**
  * User: Taoz
  * Date: 1/16/2017
  * Time: 3:50 PM
  */
package object ptcl {

  trait Request

  trait Response{
    val errCode: Int
    val msg: String
  }

  case class CommonRsp( errCode: Int = 0, msg: String = "ok") extends Response


  sealed trait WebSocketMsg

  //"heartbeat"
  case class Heartbeat(id: String) extends WebSocketMsg

  case class ComeIn(data:Int) extends WebSocketMsg

  case class GetOut(data:Int) extends WebSocketMsg


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

  case class ResidentInfo(groupId: String, data: List[DurationData]) extends RspResult

  case class BrandsInfo(groupId: String, data: List[BrandsData]) extends RspResult

  case class FrequencyInfo(groupId: String, visitFrequency: List[FrequencyData]) extends RspResult

  case class RatioInfo(groupId: String, ratio:List[RatioData]) extends RspResult

  case class RealTimeInfo(flow: List[CountData], groupId: String, max: Int, now: Int, total: Int) extends RspResult


  case class ResidentInfoRsp(data: Option[List[ResidentInfo]], msg: String, errCode: Int) extends RspResult

  case class BrandsInfoRsp(data: Option[List[BrandsInfo]], msg: String, errCode: Int) extends RspResult

  case class FrequencyInfoRsp(data: Option[List[FrequencyInfo]], msg: String, errCode: Int) extends RspResult

  case class RatioInfoRsp(data: Option[List[RatioInfo]], msg: String, errCode: Int) extends RspResult

  case class RealTimeInfoRsp(data: Option[List[RealTimeInfo]], msg: String, errCode: Int) extends RspResult


  /**
    * User
    */

  case class UserConfirm(account: String, psw: String) extends Request

  case class UserRegisterInfo(account:String, password:String) extends Request


  /**
    * Groups and Boxs
    */

  case class Group(id: Long, name: String, createTime:Long, durationLength: Long, map: String, scala:Double)

  case class ModifyGroup(id:Long, name:String, durationLength: Long)

  case class AddGroup(name:String, durationLength: Long, map:String, scala:Double, width:Double, height:Double)

  case class AddGroupRsp(id:Option[Long], timestamp:Option[Long], errCode: Int = 0, msg: String = "ok")

  case class GroupsRsp(data: List[Group],
                         errCode: Int = 0,
                         msg: String = "ok") extends Response

  case class Box(id: Long, name: String, mac:String, createTime:Long, rssi: Int, x: Double, y: Double)

  case class BoxsRsp(data: List[Box],
                       errCode: Int = 0,
                       msg: String = "ok") extends Response

  case class ModifyBox(id:Long, mac:String, name:String, rssi:Int)

  case class AddBox(name:String, mac:String, rssi:Int, groupId:Long, x: Double, y: Double)


}
