package com.neo.sk.flowShow.frontend.pages

import com.neo.sk.flowShow.frontend.utils.Page
import org.scalajs.dom.html.Div
import scalatags.JsDom.short._
import com.neo.sk.flowShow.frontend.components.TopNavigation
import com.neo.sk.flowShow.frontend.panel.FlowPanel

/**
  * Created by dry on 2017/4/10.
  */
object HomePage extends Page{

  private val topNav = TopNavigation.render

  private val panelBox = FlowPanel.render


  override def locationHash: String = ""

  override def build(): Div = {
    div(
      div(*.cls := "navbar navbar-default navbar-fixed-top")(
        topNav
      ),
      div(*.cls := "container", *.marginTop := 20, *.paddingBottom := 50)(
        panelBox
      )
    ).render


  }
}
