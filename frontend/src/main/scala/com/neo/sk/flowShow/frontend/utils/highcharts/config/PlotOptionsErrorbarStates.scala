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
  * @note JavaScript name: <code>plotOptions-errorbar-states</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsErrorbarStates extends js.Object {

  /**
    * Options for the hovered series
    */
  val hover: js.UndefOr[CleanJsObject[PlotOptionsErrorbarStatesHover]] = js.undefined
}

object PlotOptionsErrorbarStates {
  /**
    * @param hover Options for the hovered series
    */
  def apply(hover: js.UndefOr[CleanJsObject[PlotOptionsErrorbarStatesHover]] = js.undefined): PlotOptionsErrorbarStates = {
    val hoverOuter: js.UndefOr[CleanJsObject[PlotOptionsErrorbarStatesHover]] = hover
    new PlotOptionsErrorbarStates {
      override val hover: js.UndefOr[CleanJsObject[PlotOptionsErrorbarStatesHover]] = hoverOuter
    }
  }
}
