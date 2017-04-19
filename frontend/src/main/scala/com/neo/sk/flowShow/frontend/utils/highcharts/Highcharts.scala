package com.neo.sk.flowShow.frontend.utils.highcharts

import com.neo.sk.flowShow.frontend.utils.highcharts.config.HighchartsConfig
import org.scalajs.dom.CanvasRenderingContext2D

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

/**
  * Highcharts API object
  * @see [[http://api.highcharts.com/highcharts]]
  */
//@js.native
//@JSName("Highcharts")
//object Highcharts extends api.Highcharts

@JSName("Chart")
@js.native
class Highcharts(context: CanvasRenderingContext2D, props: HighchartsConfig) extends api.Highcharts{

  def destroy(): Unit = js.native

}