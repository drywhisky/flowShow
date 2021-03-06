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
  * @note JavaScript name: <code>series&lt;solidgauge&gt;-data</code>
  */
@js.annotation.ScalaJSDefined
class SeriesSolidgaugeData extends js.Object {

  /**
    * An additional, individual class name for the data point's graphic representation.
    * @since 5.0.0
    */
  val className: js.UndefOr[String] = js.undefined

  /**
    * Individual color for the point. By default the color is pulled from the global <code>colors</code> array.
    * @example <a href="http://jsfiddle.net/gh/get/jquery/3.1.1/highcharts/highcharts/tree/master/samples/highcharts/point/color/" target="_blank">Mark the highest point</a>
    */
  val color: js.UndefOr[String | js.Object] = js.undefined

  /**
    * <a href="http://www.highcharts.com/docs/chart-design-and-style/style-by-css">Styled mode</a> only. A specific color index to use for the point, so its graphic representations are given the class name <code>highcharts-color-{n}</code>.
    * @since 5.0.0
    */
  val colorIndex: js.UndefOr[Double] = js.undefined

  /**
    * Individual data label for each point. The options are the same as the ones for  <a class="internal" href="#plotOptions.series.dataLabels">plotOptions.series.dataLabels</a>
    * @example <a href="http://jsfiddle.net/gh/get/jquery/3.1.1/highcharts/highcharts/tree/master/samples/highcharts/point/datalabels/" target="_blank">Show a label for the last value</a>
    */
  val dataLabels: js.UndefOr[js.Object] = js.undefined

  /**
    * <p><i>Requires Accessibility module</i></p>
    * <p>A description of the point to add to the screen reader information about the point.</p>
    * @example <a href="http://jsfiddle.net/gh/get/jquery/3.1.1/highcharts/highcharts/tree/master/samples/maps/accessibility/accessible-map/">Accessible map</a>
    * @since 5.0.0
    */
  val description: js.UndefOr[String] = js.undefined

  /**
    * Individual point events
    */
  val events: js.UndefOr[CleanJsObject[SeriesSolidgaugeDataEvents]] = js.undefined

  /**
    * An id for the point. This can be used after render time to get a pointer to the point object through <code>chart.get()</code>.
    * @example <a href="http://jsfiddle.net/gh/get/jquery/3.1.1/highcharts/highcharts/tree/master/samples/highcharts/point/id/" target="_blank">Remove an id'd point</a>
    * @since 1.2.0
    */
  val id: js.UndefOr[String] = js.undefined

  /**
    * The inner radius of an individual point in a solid gauge. Can be given as a number (pixels) or percentage string.
    * @example <a href="http://jsfiddle.net/gh/get/jquery/3.1.1/highcharts/highcharts/tree/master/samples/highcharts/plotoptions/solidgauge-radius/" target="_blank">Individual radius and innerRadius</a>
    * @since 4.1.6
    */
  val innerRadius: js.UndefOr[Double | String] = js.undefined

  /**
    * The rank for this point's data label in case of collision. If two data labels are about to overlap, only the one with the highest <code>labelrank</code> will be drawn.
    */
  val labelrank: js.UndefOr[Double] = js.undefined

  /**
    * <p>The name of the point as shown in the legend, tooltip, dataLabel etc.</p>
    * 
    * <p>If the <a href="#xAxis.type">xAxis.type</a> is set to <code>category</code>, and no <a href="#xAxis.categories">categories</a> option exists, the category will be pulled from the <code>point.name</code> of the last series defined. For multiple series, best practice however is to define <code>xAxis.categories</code>.</p>
    * @example <a href="http://jsfiddle.net/gh/get/jquery/3.1.1/highcharts/highcharts/tree/master/samples/highcharts/series/data-array-of-objects/" target="_blank">Point names</a>
    */
  val name: js.UndefOr[String] = js.undefined

  /**
    * The outer radius of an individual point in a solid gauge. Can be given as a number (pixels) or percentage string.
    * @example <a href="http://jsfiddle.net/gh/get/jquery/3.1.1/highcharts/highcharts/tree/master/samples/highcharts/plotoptions/solidgauge-radius/" target="_blank">Individual radius and innerRadius</a>
    * @since 4.1.6
    */
  val radius: js.UndefOr[Double | String] = js.undefined

  /**
    * Whether the data point is selected initially.
    */
  val selected: js.UndefOr[Boolean] = js.undefined

  /**
    * The y value of the point.
    */
  val y: js.UndefOr[Double] = js.undefined
}

object SeriesSolidgaugeData {
  /**
    * @param className An additional, individual class name for the data point's graphic representation.
    * @param color Individual color for the point. By default the color is pulled from the global <code>colors</code> array.
    * @param colorIndex <a href="http://www.highcharts.com/docs/chart-design-and-style/style-by-css">Styled mode</a> only. A specific color index to use for the point, so its graphic representations are given the class name <code>highcharts-color-{n}</code>.
    * @param dataLabels Individual data label for each point. The options are the same as the ones for  <a class="internal" href="#plotOptions.series.dataLabels">plotOptions.series.dataLabels</a>
    * @param description <p><i>Requires Accessibility module</i></p>. <p>A description of the point to add to the screen reader information about the point.</p>
    * @param events Individual point events
    * @param id An id for the point. This can be used after render time to get a pointer to the point object through <code>chart.get()</code>.
    * @param innerRadius The inner radius of an individual point in a solid gauge. Can be given as a number (pixels) or percentage string.
    * @param labelrank The rank for this point's data label in case of collision. If two data labels are about to overlap, only the one with the highest <code>labelrank</code> will be drawn.
    * @param name <p>The name of the point as shown in the legend, tooltip, dataLabel etc.</p>. . <p>If the <a href="#xAxis.type">xAxis.type</a> is set to <code>category</code>, and no <a href="#xAxis.categories">categories</a> option exists, the category will be pulled from the <code>point.name</code> of the last series defined. For multiple series, best practice however is to define <code>xAxis.categories</code>.</p>
    * @param radius The outer radius of an individual point in a solid gauge. Can be given as a number (pixels) or percentage string.
    * @param selected Whether the data point is selected initially.
    * @param y The y value of the point.
    */
  def apply(className: js.UndefOr[String] = js.undefined, color: js.UndefOr[String | js.Object] = js.undefined, colorIndex: js.UndefOr[Double] = js.undefined, dataLabels: js.UndefOr[js.Object] = js.undefined, description: js.UndefOr[String] = js.undefined, events: js.UndefOr[CleanJsObject[SeriesSolidgaugeDataEvents]] = js.undefined, id: js.UndefOr[String] = js.undefined, innerRadius: js.UndefOr[Double | String] = js.undefined, labelrank: js.UndefOr[Double] = js.undefined, name: js.UndefOr[String] = js.undefined, radius: js.UndefOr[Double | String] = js.undefined, selected: js.UndefOr[Boolean] = js.undefined, y: js.UndefOr[Double] = js.undefined): SeriesSolidgaugeData = {
    val classNameOuter: js.UndefOr[String] = className
    val colorOuter: js.UndefOr[String | js.Object] = color
    val colorIndexOuter: js.UndefOr[Double] = colorIndex
    val dataLabelsOuter: js.UndefOr[js.Object] = dataLabels
    val descriptionOuter: js.UndefOr[String] = description
    val eventsOuter: js.UndefOr[CleanJsObject[SeriesSolidgaugeDataEvents]] = events
    val idOuter: js.UndefOr[String] = id
    val innerRadiusOuter: js.UndefOr[Double | String] = innerRadius
    val labelrankOuter: js.UndefOr[Double] = labelrank
    val nameOuter: js.UndefOr[String] = name
    val radiusOuter: js.UndefOr[Double | String] = radius
    val selectedOuter: js.UndefOr[Boolean] = selected
    val yOuter: js.UndefOr[Double] = y
    new SeriesSolidgaugeData {
      override val className: js.UndefOr[String] = classNameOuter
      override val color: js.UndefOr[String | js.Object] = colorOuter
      override val colorIndex: js.UndefOr[Double] = colorIndexOuter
      override val dataLabels: js.UndefOr[js.Object] = dataLabelsOuter
      override val description: js.UndefOr[String] = descriptionOuter
      override val events: js.UndefOr[CleanJsObject[SeriesSolidgaugeDataEvents]] = eventsOuter
      override val id: js.UndefOr[String] = idOuter
      override val innerRadius: js.UndefOr[Double | String] = innerRadiusOuter
      override val labelrank: js.UndefOr[Double] = labelrankOuter
      override val name: js.UndefOr[String] = nameOuter
      override val radius: js.UndefOr[Double | String] = radiusOuter
      override val selected: js.UndefOr[Boolean] = selectedOuter
      override val y: js.UndefOr[Double] = yOuter
    }
  }
}
