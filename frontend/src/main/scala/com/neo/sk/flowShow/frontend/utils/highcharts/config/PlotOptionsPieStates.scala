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
  * @note JavaScript name: <code>plotOptions-pie-states</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsPieStates extends js.Object {

  /**
    * Options for the hovered series
    */
  val hover: js.UndefOr[CleanJsObject[PlotOptionsPieStatesHover]] = js.undefined
}

object PlotOptionsPieStates {
  /**
    * @param hover Options for the hovered series
    */
  def apply(hover: js.UndefOr[CleanJsObject[PlotOptionsPieStatesHover]] = js.undefined): PlotOptionsPieStates = {
    val hoverOuter: js.UndefOr[CleanJsObject[PlotOptionsPieStatesHover]] = hover
    new PlotOptionsPieStates {
      override val hover: js.UndefOr[CleanJsObject[PlotOptionsPieStatesHover]] = hoverOuter
    }
  }
}
