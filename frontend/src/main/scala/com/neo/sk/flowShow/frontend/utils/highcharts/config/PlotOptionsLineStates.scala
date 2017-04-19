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
  * @note JavaScript name: <code>plotOptions-line-states</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsLineStates extends js.Object {

  /**
    * Options for the hovered series
    */
  val hover: js.UndefOr[CleanJsObject[PlotOptionsLineStatesHover]] = js.undefined
}

object PlotOptionsLineStates {
  /**
    * @param hover Options for the hovered series
    */
  def apply(hover: js.UndefOr[CleanJsObject[PlotOptionsLineStatesHover]] = js.undefined): PlotOptionsLineStates = {
    val hoverOuter: js.UndefOr[CleanJsObject[PlotOptionsLineStatesHover]] = hover
    new PlotOptionsLineStates {
      override val hover: js.UndefOr[CleanJsObject[PlotOptionsLineStatesHover]] = hoverOuter
    }
  }
}
