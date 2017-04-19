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
  * @note JavaScript name: <code>plotOptions-column-states</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsColumnStates extends js.Object {

  /**
    * Options for the hovered series
    */
  val hover: js.UndefOr[CleanJsObject[PlotOptionsColumnStatesHover]] = js.undefined
}

object PlotOptionsColumnStates {
  /**
    * @param hover Options for the hovered series
    */
  def apply(hover: js.UndefOr[CleanJsObject[PlotOptionsColumnStatesHover]] = js.undefined): PlotOptionsColumnStates = {
    val hoverOuter: js.UndefOr[CleanJsObject[PlotOptionsColumnStatesHover]] = hover
    new PlotOptionsColumnStates {
      override val hover: js.UndefOr[CleanJsObject[PlotOptionsColumnStatesHover]] = hoverOuter
    }
  }
}
