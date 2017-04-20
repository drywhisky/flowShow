package com.neo.sk.flowShow.frontend.utils

import org.scalajs.dom.window

import scala.scalajs.js.Date
import scala.util.matching.Regex

/**
  * Created by springlustre on 2017/1/31.
  */
object MyUtil {

  def GetQueryString(name:String) = {
    val regex = new Regex("""(^|&)"""+name+"""=([^&]*)(&|$)""")
    val url = scalajs.js.URIUtils.decodeURI(window.location.search.substring(1))
    regex.findFirstIn(url) match{
      case Some(res) =>
        Some(res.split("=").last.split("&").head)
      case None =>
        None
    }
  }

  def timeFormat(timestamp:Long) = {
    new Date(timestamp).toLocaleString
  }

  /**
    * dateFormat default yyyy-MM-dd HH:mm:ss
    * */
  def dataFormatDefault(timestamp:Long,format:String = "yyyy-MM-dd HH:mm:ss"):String ={
    DateFormatter(new Date(timestamp),format)
  }

  def DateFormatter(date:Date,`type`:String):String ={
    val y = date.getFullYear()
    val m = date.getMonth() + 1
    val d = date.getDate()
    val h = date.getHours()
    val mi = date.getMinutes()
    val s = date.getSeconds()
    val mS = if(m < 10 )
        "0" + m
      else
        m
    val dS = if(d < 10)
      "0"+d
        else
          d
    val hS = if(h < 10)
      "0" + h
        else
          h
    val miS = if(mi < 10)
      "0" + mi
          else
          mi
    val sS = if(s < 10)
      "0" + s
        else
          s
    `type`match {
      case "YYYY-MM-DD hh:mm:ss" =>
         y+"-"+mS+"-"+dS+" "+hS+":"+miS+":"+sS
      case "YYYY-MM-DD hh:mm"=>
         y+"-"+mS+"-"+dS+" "+hS+":"+miS
      case "YYYY-MM-DD" =>
          y+"-"+mS+"-"+dS
      case "YYYY-MM"=>
        y+"-"+mS
      case "MM-DD" =>
        mS+"-"+dS
      case "hh:mm"=>
        hS+":"+miS
      case x =>
        y+"-"+mS+"-"+dS+" "+hS+":"+miS+":"+sS
    }
  }

  def WeekFormatter(date:Date):List[Date] = {
    var first =date.getTime()
    var last = date.getTime()
    date.getDay() match{
      case 0 =>
        first = date.getTime()- 86400000*6
        last = date.getTime()
        List(new Date(first),new Date(last))
      case 1 =>
        first = date.getTime()
        last = date.getTime()+ 86400000*6
        List(new Date(first),new Date(last))
      case 2=>
        first = date.getTime()-86400000
        last = date.getTime()+ 86400000*5
        List(new Date(first),new Date(last))
      case 3=>
        first = date.getTime()-86400000*2
        last = date.getTime()+ 86400000*4
        List(new Date(first),new Date(last))
      case 4=>
        first = date.getTime()-86400000*3
        last = date.getTime()+ 86400000*3
        List(new Date(first),new Date(last))
      case 5=>
        first = date.getTime()-86400000*4
        last = date.getTime()+ 86400000*2
        List(new Date(first),new Date(last))
      case 6=>
        first = date.getTime()-86400000*5
        last = date.getTime()+ 86400000
        List(new Date(first),new Date(last))
      case x =>
          List(date)
    }
  }

  def timestampToTime(time: Long) = {

    (if (new Date(time).getHours() < 10)
      "0" + new Date(time).getHours()
    else new Date(time).getHours())+ ":" +
      (if (new Date(time).getMinutes() < 10)
        "0" + new Date(time).getMinutes()
      else new Date(time).getMinutes()) + ":" +
      (if (new Date(time).getSeconds() < 10)
        "0" + new Date(time).getSeconds()
      else new Date(time).getSeconds())

  }

}
