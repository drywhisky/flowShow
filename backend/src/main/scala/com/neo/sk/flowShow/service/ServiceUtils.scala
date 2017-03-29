package com.neo.sk.flowShow.service

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}

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

}
