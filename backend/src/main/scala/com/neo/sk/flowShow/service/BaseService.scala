package com.neo.sk.flowShow.service

import akka.http.scaladsl.server.Directives._
import com.neo.sk.utils.CirceSupport
import org.slf4j.LoggerFactory

/**
  * Created by dry on 2017/4/10.
  */
trait BaseService extends ServiceUtils with SessionBase with CirceSupport{

  private val log = LoggerFactory.getLogger("com.neo.sk.hw1701b.service.BaseService")

  val baseRoutes = pathPrefix("base")(
    staticRoutes
  )

  private val staticRoutes = (path("home") & get & pathEndOrSingleSlash) {
    getFromResource("html/index.html")
  }

}
