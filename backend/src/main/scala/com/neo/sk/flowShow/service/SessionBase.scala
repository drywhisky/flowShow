package com.neo.sk.flowShow.service

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives.redirect

import akka.http.scaladsl.server.directives.BasicDirectives
import akka.http.scaladsl.server.{Directive0, Directive1}
import org.slf4j.LoggerFactory
import com.neo.sk.utils.SessionSupport
import com.neo.sk.flowShow.common.AppSettings

/**
  * User: Taoz
  * Date: 12/4/2016
  * Time: 7:57 PM
  */

object SessionBase {
  private val logger = LoggerFactory.getLogger(this.getClass)

  val SessionTypeKey = "STKey"

  object UserSessionKey {
    val SESSION_TYPE = "userSession"
    val uid = "uid"
    val loginTime = "loginTime"
  }

  case class UserSession(
    uid: Long,
    loginTime: Long
  ) {
    def toSessionMap = Map(
      SessionTypeKey -> UserSessionKey.SESSION_TYPE,
      UserSessionKey.uid -> uid.toString,
      UserSessionKey.loginTime -> loginTime.toString
    )
  }

  implicit class SessionTransformer(sessionMap: Map[String, String]) {
    def toUserSession: Option[UserSession] = {
      logger.debug(s"toUserSession: change map to session, ${sessionMap.mkString(",")}")
      try {
        if (sessionMap.get(SessionTypeKey).exists(_.equals(UserSessionKey.SESSION_TYPE))) {
          Some(UserSession(
            sessionMap(UserSessionKey.uid).toLong,
            sessionMap(UserSessionKey.loginTime).toLong
          ))
        } else {
          logger.debug("no session type in the session")
          None
        }
      } catch {
        case e: Exception =>
          e.printStackTrace()
          logger.warn(s"toUserSession: ${e.getMessage}")
          None
      }
    }
  }

}

trait SessionBase extends SessionSupport {

  import SessionBase._

  override val sessionEncoder = SessionSupport.PlaySessionEncoder
  override val sessionConfig = AppSettings.sessionConfig

  private val sessionTimeOut = 24 * 60 * 60 * 1000


  protected def setUserSession(userSession: UserSession): Directive0 = setSession(userSession.toSessionMap)

  protected val optionalUserSession: Directive1[Option[UserSession]] = optionalSession.flatMap {
    case Right(sessionMap) => BasicDirectives.provide(sessionMap.toUserSession)
    case Left(error) =>
      logger.debug(error)
      BasicDirectives.provide(None)
  }

  protected def UserAction(f: UserSession => server.Route): server.Route = {
    optionalUserSession {
      case Some(userSession) =>
        if (System.currentTimeMillis() - userSession.loginTime > sessionTimeOut) {
          logger.info("Login failed for Timeout !")
          redirect("/flowShow/user/login", StatusCodes.SeeOther)
        } else {
          f(UserSession(userSession.uid, userSession.loginTime))
        }
      case None =>
        redirect("/flowShow/user/login", StatusCodes.SeeOther)
    }
  }

}
