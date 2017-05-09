package com.neo.sk.flowShow.common

import java.util.concurrent.TimeUnit

import com.neo.sk.utils.SessionSupport.SessionConfig
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.LoggerFactory

/**
  * User: Taoz
  * Date: 9/4/2015
  * Time: 4:29 PM
  */
object AppSettings {

  private implicit class RichConfig(config: Config) {
    val noneValue = "none"

    def getOptionalString(path: String): Option[String] =
      if (config.getAnyRef(path) == noneValue) None
      else Some(config.getString(path))

    def getOptionalLong(path: String): Option[Long] =
      if (config.getAnyRef(path) == noneValue) None
      else Some(config.getLong(path))

    def getOptionalDurationSeconds(path: String): Option[Long] =
      if (config.getAnyRef(path) == noneValue) None
      else Some(config.getDuration(path, TimeUnit.SECONDS))
  }


  val log = LoggerFactory.getLogger(this.getClass)
  val config = ConfigFactory.parseResources("product.conf").withFallback(ConfigFactory.load())

  val appConfig = config.getConfig("app")

  val httpInterface = appConfig.getString("http.interface")
  val httpPort = appConfig.getInt("http.port")

  val serverProtocol = appConfig.getString("server.protocol")
  val serverHost = appConfig.getString("server.host")

  val realTimeCountInterval = config.getInt("app.realTimeCountInterval")
  val historyCountInterval = config.getInt("app.historyCountInterval")
  val realTimeMacInterval = config.getInt("app.realTimeMacInterval")
  val staffDuration = config.getInt("app.staffDuration")

  val tempPath = config.getString("app.tempPath")
  val rssiValue = config.getInt("app.rssiValue")
  val visitDurationLent = config.getInt("app.visitDurationLent")

  val slickConfig = config.getConfig("slick.db")
  val slickUrl = slickConfig.getString("url")
  val slickUser = slickConfig.getString("user")
  val slickPassword = slickConfig.getString("password")
  val slickMaximumPoolSize = slickConfig.getInt("maximumPoolSize")
  val slickConnectTimeout = slickConfig.getInt("connectTimeout")
  val slickIdleTimeout = slickConfig.getInt("idleTimeout")
  val slickMaxLifetime = slickConfig.getInt("maxLifetime")

  private val dependence = config.getConfig("dependence")

  val nyx = dependence.getConfig("nyx")

  val nyxProtocol = nyx.getString("protocol")
  val nyxWebsokectProtocol = nyx.getString("websokectProtocol")
  val nyxAppId = nyx.getString("appId")
  val nyxSecureKey = nyx.getString("secureKey")
  val nyxHost = nyx.getString("host")
  val nyxPort = nyx.getString("port")

  val akso = dependence.getConfig("akso")

  val aksoProtocol = akso.getString("protocol")
  val aksoWebsokectProtocol = akso.getString("websokectProtocol")
  val aksoAppId = akso.getString("appId")
  val aksoSecureKey = akso.getString("secureKey")
  val aksoHost = akso.getString("host")
  val aksoPort = akso.getString("port")

  val group = dependence.getConfig("group")
  val groupIdNameMap = {
    import collection.JavaConversions._
    val groupIdList = group.getIntList("groupId")
    val groupNameList = group.getStringList("groupName")
    require(groupIdList.length == groupNameList.length, "groupIdList.length and groupNameList.length not equal")
    groupIdList.zip(groupNameList).toMap
  }

  val sessionConfig = {
    val sConf = config.getConfig("session")
    SessionConfig(
      cookieName = sConf.getString("cookie.name"),
      serverSecret = sConf.getString("serverSecret"),
      domain = sConf.getOptionalString("cookie.domain"),
      path = sConf.getOptionalString("cookie.path"),
      secure = sConf.getBoolean("cookie.secure"),
      httpOnly = sConf.getBoolean("cookie.httpOnly"),
      maxAge = sConf.getOptionalDurationSeconds("cookie.maxAge"),
      sessionEncryptData = sConf.getBoolean("encryptData")
    )


  }



}
