package com.neo.sk.flowShow.service

import java.util.Date

import akka.http.scaladsl.server.Directives._
import com.neo.sk.flowShow.service.SessionBase.UserSession
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Left, Success}
import scalatags.Text.TypedTag
import com.neo.sk.utils.{HttpUtil, EhCacheApi}

/**
  * User: Taoz
  * Date: 11/18/2016
  * Time: 7:50 PM
  */
trait TestService extends ServiceUtils with SessionBase with HttpUtil {


  //import scalatags.Text.tags._
  import scalatags.Text.short._
  //import scalatags.Text.{attrs => attr, styles => css}*/

  implicit val executor: ExecutionContext

  val redP: TypedTag[String] = p(*.backgroundColor := "red", *.color := "white")

  private val logger = LoggerFactory.getLogger("com.neo.sk.webchat.http.TestService")

  def inputBox(name: String, action: String): TypedTag[String] =
    form(*.action := action, *.method := "get")(
      input(*.`type` := "text", *.name := name, *.placeholder := name),
      input(*.`type` := "submit", *.value := "ok")
    )



  val userSessionTest = pathPrefix("userSession") {

    (path("set") & get) {
      val uid = 1234567l
      val userSession = UserSession(uid, System.currentTimeMillis())
      setUserSession(userSession) {
        complete(s"user session set: $userSession")
      }
    } ~ (path("read") & get) {
      optionalUserSession {
        case Some(userSession) => complete("")
        case None => complete("")
      }
    } ~ (path("clean") & get) {
      complete("")
    }

  }

  val sessionTest = pathPrefix("session") {
    (path("set") & get) {
      val session = Map("a" -> "1", "b" -> "2")
      setSession(session) { cxt =>
        cxt.complete("session set.")
      }
    } ~ (path("read") & get) {
      optionalSession {
        case Right(session) =>
          println(s"---------- find session in read: ${session.mkString(";")}")
          complete(s"Your session: ${session.mkString(";")}")
        case Left(error) => complete(s"You have No session. error: [$error]")
      }
    } ~ (path("clean") & get) {
      println("---------------- CLEAN SESSION  !!!!!!!!! ")
      optionalSession {
        case Right(session) =>
          println(s"++++++++++ find session in clean: ${session.mkString(";")}")
          invalidateSession {
            complete(s"Your session: ${session.mkString(";")}, and it has been clean.")
          }
        case Left(error) =>
          println(s"++++++++++ find NO NO NO session in clean: $error")
          invalidateSession {
            complete(s"you have no session to clean. error: [$error]")
          }
      }
    } ~ (path("remove") & get) {
      val keys = Set("a", "c")
      removeSession(keys) {
        complete(s"remove sesion: $keys")
      }
    }
  }


  val testCache = EhCacheApi.createCache[String]("testCache", 10, 30)

  val cacheTest = pathPrefix("cache") {
    (path("get" / Segment) & get) { key =>
      val dataF = testCache(key) {
        val d = new Date()
        "cache data with time:" + d.toString
      }
      onComplete(dataF) {
        case Success(r) => complete(s"success: $r")
        case Failure(e) => complete(s"error: ${e.getMessage}")
      }
    } ~ (path("clean" / Segment) & get) { key =>
      testCache.remove(key)
      complete(s"cache [$key] removed.")
    }
  }


  val testHttpUtil = (path("httpUtil") & get) {

    val url = "http://super6:30309/mpAuth/api/webAuth"
    val parameters =
      List(
        "mpAppId" -> "wx0f9648452232479f",
        "code" -> "021VqIIG1iVN350e8IJG1NkRIG1VqIIV",
        "appClientId" -> "444",
        "infoType" -> "wxInfo",
        "timestamp" -> "1481525897407",
        "nonce" -> "TQS8fn",
        "signature" -> "e2408f95846363053f5aad15dd18969bf5572878"
      )
    logger.info(s"test httpUtil: url = $url parameters = ${parameters.mkString(",")}")
    onComplete(getRequestSend("testHttpUtil", url, parameters)) {
      case Success(Right(rst)) =>
        logger.info(s"rst: $rst")
        complete(s"got msg: $rst")
      case Success(Left(e)) => complete(s"error: ${e.getMessage}")
      case Failure(e) => complete(s"internal error: ${e.getMessage}")
    }
  }

  val test1 = {
    (path("1") & get) {
      val str = "<!DOCTYPE html>" + html(
        head(
          script("console.log('sss....')")
        ),
        body(
          h1("hi, world."),
          h2(*.backgroundColor := "blue", *.color := "white")("hi, world."),
          h3("hi, world."),
          p("hi, world."),
          redP("red ppp"),
          a(*.href := "http://www.baidu.com")("baidu"),
          (1 to 3).map { i =>
            tag(s"h$i")(s"i am h$i")
          },
          inputBox("name", "http://www.baidu.com"),
          inputBox("passwd", "http://www.baidu.com")
        )
      )
      println(s"str------------\n$str")
      complete(htmlResponse(str))
    }
  }


  val testRoute = pathPrefix("tests") {
    test1 ~ testHttpUtil ~ sessionTest ~ cacheTest
  }


}
