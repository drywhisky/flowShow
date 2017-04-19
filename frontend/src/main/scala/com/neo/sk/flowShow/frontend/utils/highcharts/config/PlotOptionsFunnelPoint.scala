/**
  * Automatically generated file. Please do not edit.
  * @author Highcharts Config Generator by Karasiq
  * @see [[http://api.highcharts.com/highcharts]]
  */
package com.neo.sk.flowShow.frontend.utils.highcharts.config

import scalajs.js, js.`|`
import com.neo.sk.flowShow.frontend.utils.highcharts.CleanJsObject
import com.neo.sk.flowShow.frontend.utils.highcharts.HighchartsUtils._

/**
  * @note JavaScript name: <code>plotOptions-funnel-point</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsFunnelPoint extends js.Object {

  /**
    * Events for each single point
    */
  val events: js.UndefOr[CleanJsObject[PlotOptionsFunnelPointEvents]] = js.undefined
}

object PlotOptionsFunnelPoint {
  /**
    * @param events Events for each single point
    */
  def apply(events: js.UndefOr[CleanJsObject[PlotOptionsFunnelPointEvents]] = js.undefined): PlotOptionsFunnelPoint = {
    val eventsOuter: js.UndefOr[CleanJsObject[PlotOptionsFunnelPointEvents]] = events
    new PlotOptionsFunnelPoint {
      override val events: js.UndefOr[CleanJsObject[PlotOptionsFunnelPointEvents]] = eventsOuter
    }
  }
}
