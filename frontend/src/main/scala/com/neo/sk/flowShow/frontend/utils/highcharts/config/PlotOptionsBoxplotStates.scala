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
  * @note JavaScript name: <code>plotOptions-boxplot-states</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsBoxplotStates extends js.Object {

  /**
    * Options for the hovered series
    */
  val hover: js.UndefOr[CleanJsObject[PlotOptionsBoxplotStatesHover]] = js.undefined
}

object PlotOptionsBoxplotStates {
  /**
    * @param hover Options for the hovered series
    */
  def apply(hover: js.UndefOr[CleanJsObject[PlotOptionsBoxplotStatesHover]] = js.undefined): PlotOptionsBoxplotStates = {
    val hoverOuter: js.UndefOr[CleanJsObject[PlotOptionsBoxplotStatesHover]] = hover
    new PlotOptionsBoxplotStates {
      override val hover: js.UndefOr[CleanJsObject[PlotOptionsBoxplotStatesHover]] = hoverOuter
    }
  }
}
