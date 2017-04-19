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
  * @note JavaScript name: <code>plotOptions-funnel-states</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsFunnelStates extends js.Object {

  /**
    * Options for the hovered series
    */
  val hover: js.UndefOr[CleanJsObject[PlotOptionsFunnelStatesHover]] = js.undefined
}

object PlotOptionsFunnelStates {
  /**
    * @param hover Options for the hovered series
    */
  def apply(hover: js.UndefOr[CleanJsObject[PlotOptionsFunnelStatesHover]] = js.undefined): PlotOptionsFunnelStates = {
    val hoverOuter: js.UndefOr[CleanJsObject[PlotOptionsFunnelStatesHover]] = hover
    new PlotOptionsFunnelStates {
      override val hover: js.UndefOr[CleanJsObject[PlotOptionsFunnelStatesHover]] = hoverOuter
    }
  }
}
