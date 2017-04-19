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
  * @note JavaScript name: <code>series&lt;funnel&gt;-states</code>
  */
@js.annotation.ScalaJSDefined
class SeriesFunnelStates extends js.Object {

  /**
    * Options for the hovered series
    */
  val hover: js.UndefOr[CleanJsObject[SeriesFunnelStatesHover]] = js.undefined
}

object SeriesFunnelStates {
  /**
    * @param hover Options for the hovered series
    */
  def apply(hover: js.UndefOr[CleanJsObject[SeriesFunnelStatesHover]] = js.undefined): SeriesFunnelStates = {
    val hoverOuter: js.UndefOr[CleanJsObject[SeriesFunnelStatesHover]] = hover
    new SeriesFunnelStates {
      override val hover: js.UndefOr[CleanJsObject[SeriesFunnelStatesHover]] = hoverOuter
    }
  }
}
