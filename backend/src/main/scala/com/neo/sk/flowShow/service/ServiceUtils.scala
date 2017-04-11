package com.neo.sk.flowShow.service

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server

import scala.concurrent.Future
import akka.http.scaladsl.server.Directives._
import scala.util.{Failure, Success}

/**
  * User: Taoz
  * Date: 11/18/2016
  * Time: 7:57 PM
  */
trait ServiceUtils {


  def htmlResponse(html: String): HttpResponse = {
    HttpResponse(entity = HttpEntity(ContentTypes.`text/html(UTF-8)`, html))
  }

  def jsonResponse(json: String): HttpResponse = {
    HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, json))
  }

  def dealFutureResult(future: => Future[server.Route]) = onComplete(future){
    case Success(route) =>
      route

    case Failure(e) =>
      e.printStackTrace()
      complete("Internal error.")
  }

}
