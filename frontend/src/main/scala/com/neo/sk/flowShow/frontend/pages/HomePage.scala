package com.neo.sk.flowShow.frontend.pages

import com.neo.sk.flowShow.frontend.utils.Page
import org.scalajs.dom.html.Div
import scalatags.JsDom.short._
import com.neo.sk.flowShow.frontend.panel.FlowPanel

/**
  * Created by dry on 2017/4/10.
  */
object HomePage extends Page{

  private val panelBox = FlowPanel.render

  override def locationHash: String = ""

  override def build(): Div = {
    div(*.id := "artpip")(
      div(*.id := "content")(
        div(*.cls := "navbar navbar-default", *.role := "navigation")(
          h1("实时客流可视化系统")
        ),
        div(*.cls := "cd-vertical-nav")(
          ul(
            li(a(*.cls := "smooth")("1")),
            li(a(*.cls := "smooth")("2")),
            li(a(*.cls := "smooth")("3")),
            li(a(*.cls := "smooth")("4")),
            li(a(*.cls := "smooth")("5"))
          )
        ),
        div(*.id := "main")(
          div(*.cls := "featured-container")(
            panelBox
          )
        )
      )
    ).render
  }
}
