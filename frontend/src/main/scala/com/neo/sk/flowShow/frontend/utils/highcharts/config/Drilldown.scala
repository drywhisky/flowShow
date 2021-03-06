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
  * @note JavaScript name: <code>drilldown</code>
  */
@js.annotation.ScalaJSDefined
class Drilldown extends js.Object {

  /**
    * Additional styles to apply to the X axis label for a point that has drilldown data. By default it is underlined and blue to invite to interaction.
    * @example <a href="http://jsfiddle.net/gh/get/jquery/3.1.1/highcharts/highcharts/tree/master/samples/highcharts/drilldown/labels/" target="_blank">Label styles</a>
    * @since 3.0.8
    */
  val activeAxisLabelStyle: js.UndefOr[js.Object] = js.undefined

  /**
    * Additional styles to apply to the data label of a point that has drilldown data. By default it is underlined and blue to invite to interaction.
    * @example <a href="http://jsfiddle.net/gh/get/jquery/3.1.1/highcharts/highcharts/tree/master/samples/highcharts/drilldown/labels/" target="_blank">Label styles</a>
    * @since 3.0.8
    */
  val activeDataLabelStyle: js.UndefOr[js.Object] = js.undefined

  /**
    * When this option is false, clicking a single point will drill down all points in the same category, equivalent to clicking the X axis label.
    * @example <a href="http://jsfiddle.net/gh/get/jquery/3.1.1/highcharts/highcharts/tree/master/samples/highcharts/drilldown/allowpointdrilldown-false/" target="_blank">Don't allow point drilldown</a>.
    * @since 4.1.7
    */
  val allowPointDrilldown: js.UndefOr[Boolean] = js.undefined

  /**
    * <p>Set the animation for all drilldown animations. Animation of a drilldown occurs when drilling between a column point and a column series, or a pie slice and a full pie series. Drilldown can still be used between series and points of different types, but animation will not occur.</p>
    *  
    *  <p>The animation can either be set as a boolean or a configuration object. If <code>true</code>,
    *  it will use the 'swing' jQuery easing and a duration of 500 ms. If used as a configuration object,
    *  the following properties are supported: 
    *  </p><dl>
    *  	<dt>duration</dt>
    *  	<dd>The duration of the animation in milliseconds.</dd>
    *  	
    *  	<dt>easing</dt>
    *  	<dd>A string reference to an easing function set on the <code>Math</code> object. See <a href="http://jsfiddle.net/gh/get/jquery/3.1.1/highcharts/highcharts/tree/master/samples/highcharts/plotoptions/series-animation-easing/">the easing demo</a>.</dd>
    *  </dl>
    * @since 3.0.8
    */
  val animation: js.UndefOr[Boolean | js.Object] = js.undefined

  /**
    * Options for the drill up button that appears when drilling down on a series. The text for the button is defined in <a href="#lang.drillUpText">lang.drillUpText</a>.
    * @example <a href="http://jsfiddle.net/gh/get/jquery/3.1.1/highcharts/highcharts/tree/master/samples/highcharts/drilldown/drillupbutton/" target="_blank">Drill up button</a>
    * @since 3.0.8
    */
  val drillUpButton: js.UndefOr[CleanJsObject[DrilldownDrillUpButton]] = js.undefined

  /**
    * An array of series configurations for the drill down. Each series configuration uses the same syntax as the <a href="#series">series</a> option set. These drilldown series are hidden by default. The drilldown series is linked to the parent series' point by its <code>id</code>.
    * @since 3.0.8
    */
  val series: js.UndefOr[js.Array[js.Object]] = js.undefined
}

object Drilldown {
  /**
    * @param activeAxisLabelStyle Additional styles to apply to the X axis label for a point that has drilldown data. By default it is underlined and blue to invite to interaction.
    * @param activeDataLabelStyle Additional styles to apply to the data label of a point that has drilldown data. By default it is underlined and blue to invite to interaction.
    * @param allowPointDrilldown When this option is false, clicking a single point will drill down all points in the same category, equivalent to clicking the X axis label.
    * @param animation <p>Set the animation for all drilldown animations. Animation of a drilldown occurs when drilling between a column point and a column series, or a pie slice and a full pie series. Drilldown can still be used between series and points of different types, but animation will not occur.</p>.  .  <p>The animation can either be set as a boolean or a configuration object. If <code>true</code>,.  it will use the 'swing' jQuery easing and a duration of 500 ms. If used as a configuration object,.  the following properties are supported: .  </p><dl>.  	<dt>duration</dt>.  	<dd>The duration of the animation in milliseconds.</dd>.  	.  	<dt>easing</dt>.  	<dd>A string reference to an easing function set on the <code>Math</code> object. See <a href="http://jsfiddle.net/gh/get/jquery/3.1.1/highcharts/highcharts/tree/master/samples/highcharts/plotoptions/series-animation-easing/">the easing demo</a>.</dd>.  </dl>
    * @param drillUpButton Options for the drill up button that appears when drilling down on a series. The text for the button is defined in <a href="#lang.drillUpText">lang.drillUpText</a>.
    * @param series An array of series configurations for the drill down. Each series configuration uses the same syntax as the <a href="#series">series</a> option set. These drilldown series are hidden by default. The drilldown series is linked to the parent series' point by its <code>id</code>.
    */
  def apply(activeAxisLabelStyle: js.UndefOr[js.Object] = js.undefined, activeDataLabelStyle: js.UndefOr[js.Object] = js.undefined, allowPointDrilldown: js.UndefOr[Boolean] = js.undefined, animation: js.UndefOr[Boolean | js.Object] = js.undefined, drillUpButton: js.UndefOr[CleanJsObject[DrilldownDrillUpButton]] = js.undefined, series: js.UndefOr[js.Array[js.Object]] = js.undefined): Drilldown = {
    val activeAxisLabelStyleOuter: js.UndefOr[js.Object] = activeAxisLabelStyle
    val activeDataLabelStyleOuter: js.UndefOr[js.Object] = activeDataLabelStyle
    val allowPointDrilldownOuter: js.UndefOr[Boolean] = allowPointDrilldown
    val animationOuter: js.UndefOr[Boolean | js.Object] = animation
    val drillUpButtonOuter: js.UndefOr[CleanJsObject[DrilldownDrillUpButton]] = drillUpButton
    val seriesOuter: js.UndefOr[js.Array[js.Object]] = series
    new Drilldown {
      override val activeAxisLabelStyle: js.UndefOr[js.Object] = activeAxisLabelStyleOuter
      override val activeDataLabelStyle: js.UndefOr[js.Object] = activeDataLabelStyleOuter
      override val allowPointDrilldown: js.UndefOr[Boolean] = allowPointDrilldownOuter
      override val animation: js.UndefOr[Boolean | js.Object] = animationOuter
      override val drillUpButton: js.UndefOr[CleanJsObject[DrilldownDrillUpButton]] = drillUpButtonOuter
      override val series: js.UndefOr[js.Array[js.Object]] = seriesOuter
    }
  }
}
