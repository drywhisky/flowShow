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
  * @note JavaScript name: <code>plotOptions-area-states</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsAreaStates extends js.Object {

  /**
    * Options for the hovered series
    */
  val hover: js.UndefOr[CleanJsObject[PlotOptionsAreaStatesHover]] = js.undefined
}

object PlotOptionsAreaStates {
  /**
    * @param hover Options for the hovered series
    */
  def apply(hover: js.UndefOr[CleanJsObject[PlotOptionsAreaStatesHover]] = js.undefined): PlotOptionsAreaStates = {
    val hoverOuter: js.UndefOr[CleanJsObject[PlotOptionsAreaStatesHover]] = hover
    new PlotOptionsAreaStates {
      override val hover: js.UndefOr[CleanJsObject[PlotOptionsAreaStatesHover]] = hoverOuter
    }
  }
}
