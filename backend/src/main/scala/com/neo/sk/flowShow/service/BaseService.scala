package com.neo.sk.flowShow.service

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import com.neo.sk.utils.CirceSupport
import org.slf4j.LoggerFactory
import com.neo.sk.flowShow.core.AssistedDataActor._
import akka.pattern.ask
import akka.util.Timeout
import com.neo.sk.flowShow.ptcl._
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by dry on 2017/4/10.
  */
trait BaseService extends ServiceUtils with SessionBase with CirceSupport{

  private val log = LoggerFactory.getLogger("com.neo.sk.hw1701b.service.BaseService")
  implicit val timeout: Timeout

  val receiveDataActor: ActorRef
  val assistedDataActor: ActorRef

  val baseRoutes = pathPrefix("user")(
    staticRoutes ~ getResidentInfo ~ getRatioInfo ~ getBrandInfo ~ getFrequencyInfo ~ getRealTimeInfo
  )

  private val staticRoutes = (path("home") & get & pathEndOrSingleSlash) {
    getFromResource("html/index.html")
  }

  private val getResidentInfo = (path("residentInfo") & get & pathEndOrSingleSlash) {
//    dealFutureResult {
//      assistedDataActor.ask(GetResidentInfo).map {
//        case data:List[_] => complete(ResidentInfoRsp(Some(data.asInstanceOf[List[ResidentInfo]]), "ok", 0))
//        case "Error" => complete(ResidentInfoRsp(None, "error", 100001))
//      }
//    }
    val a = List(ResidentInfo("11", List(DurationData(0, 390), DurationData(1, 63), DurationData(2, 15), DurationData(3,7), DurationData(4, 17), DurationData(5, 6))))
    complete(ResidentInfoRsp(Some(a), "ok", 0))
  }

  private val getRatioInfo = (path("ratioInfo") & get & pathEndOrSingleSlash) {
    dealFutureResult {
      assistedDataActor.ask(GetRatioInfo).map {
        case data:List[_] => complete(RatioInfoRsp(Some(data.asInstanceOf[List[RatioInfo]]), "ok", 0))
        case "Error" => complete(RatioInfoRsp(None, "error", 100002))
      }
    }
  }

  private val getBrandInfo = (path("brandInfo") & get & pathEndOrSingleSlash) {
//    dealFutureResult {
//      assistedDataActor.ask(GetBrandInfo).map {
//        case data:List[_] => complete(BrandsInfoRsp(Some(data.asInstanceOf[List[BrandsInfo]]), "ok", 0))
//        case "Error" => complete(BrandsInfoRsp(None, "error", 100003))
//      }
//    }
    val a = List(BrandsInfo( "11", List(BrandsData(1, "三星", 200), BrandsData(2, "小米", 300))))
    complete(BrandsInfoRsp(Some(a), "ok", 0))
  }

  private val getFrequencyInfo = (path("frequencyInfo") & get & pathEndOrSingleSlash) {
    dealFutureResult {
      assistedDataActor.ask(GetFrequencyInfo).map {
        case data:List[_] => complete(FrequencyInfoRsp(Some(data.asInstanceOf[List[FrequencyInfo]]), "ok", 0))
        case "Error" => complete(FrequencyInfoRsp(None, "error", 100004))
      }
    }
  }

  private val getRealTimeInfo = (path("realTimeInfo") & get & pathEndOrSingleSlash) {
    dealFutureResult {
      assistedDataActor.ask(GetRealTimeInfo).map {
        case data:List[_] => complete(RealTimeInfoRsp(Some(data.asInstanceOf[List[RealTimeInfo]]), "ok", 0))
        case "Error" => complete(FrequencyInfoRsp(None, "error", 100005))
      }
    }
  }

}
