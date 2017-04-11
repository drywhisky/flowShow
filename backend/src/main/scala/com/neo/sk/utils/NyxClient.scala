package com.neo.sk.utils

import java.text.SimpleDateFormat

import com.github.nscala_time.time.Imports.DateTime
import io.circe.generic.auto._
import io.circe.parser.decode
import com.neo.sk.flowShow.common.AppSettings
import com.neo.sk.utils.HttpUtil.Imports.getRequestSend
import org.slf4j.LoggerFactory
import com.neo.sk.flowShow.ptcl._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by dry on 2017/4/11.
  */
object NyxClient {

  private val baseUrl = AppSettings.nyxProtocol + "://" +AppSettings.nyxHost + ":" + AppSettings.nyxPort

  private val appId = AppSettings.nyxAppId

  private val secureKey = AppSettings.nyxSecureKey

  private val log = LoggerFactory.getLogger(this.getClass)

  def realtimeDetail(groupId: String) = {
    val sn = System.nanoTime().toString
    val (timestamp, nonce, signature) =
      SecureUtil.generateSignatureParameters(List(appId,groupId.toString, sn), secureKey)

    val url = baseUrl + s"/nyx/api/count/realtime?appId=$appId&sn=$sn&timestamp=$timestamp&nonce=$nonce&groupId=$groupId&signature=$signature"

    getRequestSend("realtime", url, List()).map {
      case Right(str) =>
        decode[RealTimeDataRsp](str) match {
          case Right(rsp) =>
            if (rsp.errCode == 0)
              Right(RealTimeInfo(rsp.flow, rsp.groupId, rsp.max, rsp.now, rsp.total))
            else {
              log.error(s"RealTimeDataRsp error.error:${rsp.msg}")
              Left(s"${rsp.msg}")
            }

          case Left(e) =>
            log.error(s"RealTimeDataRsp parse error.$e")
            Left(s"Error.$e")
        }

      case Left(e) =>
        log.error(s"getRequestSend realtimeDetail error:" + e)
        Left(s"Error.$e")
    }
  }

  def residentsInfo(groupId: String) = {
    val dateFormat = new SimpleDateFormat("yyyyMMdd")
    val date = dateFormat.format(DateTime.yesterday())

    val sn = System.nanoTime().toString
    val (timestamp, nonce, signature) =
      SecureUtil.generateSignatureParameters(List(appId ,groupId, date, sn), secureKey)
    val url = baseUrl + s"/nyx/api/residents?appId=$appId&sn=$sn&timestamp=$timestamp&nonce=$nonce&date=$date&groupId=$groupId&signature=$signature"

    getRequestSend("residentsInfo", url, List()).map {
      case Right(str) =>
        decode[ResidentsDataRsp](str) match {
          case Right(rsp) =>
            if (rsp.errCode == 0)
              Right(ResidentsInfo(rsp.groupId, rsp.data))
            else {
              log.error(s"ResidentsDataRsp error.error:${rsp.msg}")
              Left(s"${rsp.msg}")
            }

          case Left(e) =>
            log.error(s"ResidentsDataRsp parse error.$e")
            Left(s"Error.$e")
        }

      case Left(e) =>
        log.error(s"getRequestSend residentsInfo error:" + e)
        Left(s"Error.$e")
    }
  }

  def brandsInfo(groupId: String) = {
    val dateFormat = new SimpleDateFormat("yyyyMMdd")
    val timePoint = dateFormat.format(DateTime.now())

    val sn = System.nanoTime().toString
    val (timestamp, nonce, signature) =
      SecureUtil.generateSignatureParameters(List(appId ,groupId, timePoint, sn), secureKey)
    val url = baseUrl + s"/nyx/api/brands?appId=$appId&sn=$sn&timestamp=$timestamp&nonce=$nonce&timePoint=$timePoint&groupId=$groupId&signature=$signature"

    getRequestSend("brandsInfo", url, List()).map {
      case Right(str) =>
        decode[BrandsDataRsp](str) match {
          case Right(rsp) =>
            if (rsp.errCode == 0)
              Right(BrandsInfo(rsp.groupId, rsp.data))
            else {
              log.error(s"BrandsDataRsp error.error:${rsp.msg}")
              Left(s"${rsp.msg}")
            }

          case Left(e) =>
            log.error(s"BrandsDataRsp parse error.$e")
            Left(s"Error.$e")
        }

      case Left(e) =>
        log.error(s"getRequestSend brandsInfo error:" + e)
        Left(s"Error.$e")
    }
  }

  def frequencyInfo(groupId: String) = {
    val dateFormat = new SimpleDateFormat("yyyyMMdd")
    val date = dateFormat.format(DateTime.now())

    val sn = System.nanoTime().toString
    val (timestamp, nonce, signature) =
      SecureUtil.generateSignatureParameters(List(appId ,groupId, date, sn), secureKey)
    val url = baseUrl + s"/nyx/api/frequency?appId=$appId&sn=$sn&timestamp=$timestamp&nonce=$nonce&date=$date&groupId=$groupId&signature=$signature"

    getRequestSend("frequencyInfo", url, List()).map {
      case Right(str) =>
        decode[FrequencyDataRsp](str) match {
          case Right(rsp) =>
            if (rsp.errCode == 0)
              Right(FrequencyInfo(rsp.groupId, rsp.visitFrequency))
            else {
              log.error(s"FrequencyDataRsp error.error:${rsp.msg}")
              Left(s"${rsp.msg}")
            }

          case Left(e) =>
            log.error(s"FrequencyDataRsp parse error.$e")
            Left(s"Error.$e")
        }

      case Left(e) =>
        log.error(s"getRequestSend frequencyInfo error:" + e)
        Left(s"Error.$e")
    }
  }

  def ratioInfo(groupId: String) = {
    val dateFormat = new SimpleDateFormat("yyyyMMdd")
    val endDate = dateFormat.format(DateTime.now())
    val startDate = dateFormat.format(DateTime.lastMonth())

    val sn = System.nanoTime().toString
    val (timestamp, nonce, signature) =
      SecureUtil.generateSignatureParameters(List(appId, groupId, startDate, endDate, sn), secureKey)
    val url = baseUrl +
      s"/nyx/api/ratio?appId=$appId&sn=$sn&timestamp=$timestamp&nonce=$nonce&startDate=$startDate&endDate=$endDate&groupId=$groupId&signature=$signature"

    getRequestSend("ratioInfo", url, List()).map {
      case Right(str) =>
        decode[RatioDataRsp](str) match {
          case Right(rsp) =>
            if (rsp.errCode == 0)
              Right(RatioInfo(rsp.groupId, rsp.ratio))
            else {
              log.error(s"RatioDataRsp error.error:${rsp.msg}")
              Left(s"${rsp.msg}")
            }

          case Left(e) =>
            log.error(s"RatioDataRsp parse error.$e")
            Left(s"Error.$e")
        }

      case Left(e) =>
        log.error(s"getRequestSend ratioInfo error:" + e)
        Left(s"Error.$e")
    }
  }

}
