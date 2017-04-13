package com.neo.sk.flowShow.frontend.pages

import com.neo.sk.flowShow.frontend.utils.Page
import org.scalajs.dom.html.Div

import scalatags.JsDom.short._
import com.neo.sk.flowShow.frontend.panel._
import org.scalajs.dom
import org.scalajs.dom.MouseEvent

/**
  * Created by dry on 2017/4/10.
  */
object HomePage extends Page {

  private val panelBox = div().render

  panelBox.textContent = ""
  panelBox.appendChild(ResidentPanel.render)

  private val flowButton = button(*.onclick := { e: MouseEvent =>
    e.preventDefault()
    dom.document.getElementById("main").removeAttribute("style")
    dom.document.getElementById("main").setAttribute("style", "background-color: #282B3F; height: 90%")
    panelBox.textContent = ""
    panelBox.appendChild(FlowPanel.render)
  })(1).render

  private val frequencyButton = button(*.onclick := { e: MouseEvent =>
    e.preventDefault()
    dom.document.getElementById("main").removeAttribute("style")
    dom.document.getElementById("main").setAttribute("style", "background-color: #282B3F; height: 90%")
    panelBox.textContent = ""
    panelBox.appendChild(FrequencyPanel.render)
  })(2).render

  private val ratioButton = button(*.onclick := { e: MouseEvent =>
    e.preventDefault()
    dom.document.getElementById("main").removeAttribute("style")
    dom.document.getElementById("main").setAttribute("style", "background-color: #F1FDFF; height: 90%")
    panelBox.textContent = ""
    panelBox.appendChild(RatioPanel.render)
  })(3).render

  private val residentButton = button(*.onclick := { e: MouseEvent =>
    e.preventDefault()
    dom.document.getElementById("main").removeAttribute("style")
    dom.document.getElementById("main").setAttribute("style", "background-color: #F1FDFF; height: 90%")
    panelBox.textContent = ""
    panelBox.appendChild(ResidentPanel.render)
  })(4)

  private val brandButton = button(*.onclick := { e: MouseEvent =>
    e.preventDefault()
    dom.document.getElementById("main").removeAttribute("style")
    dom.document.getElementById("main").setAttribute("style", "background-color: #282B3F; height: 90%")
    panelBox.textContent = ""
    panelBox.appendChild(BrandPanel.render)
  })(5).render

  override def locationHash: String = ""

  override def build(): Div = {
    div(*.id := "artpip", *.height := "100%")(
      div(*.id := "content", *.height := "100%")(
        div(*.cls := "navbar navbar-default", *.role := "navigation", *.style := "background-color: #282b3f;", *.height := "10%")(
          div(*.cls := "navbar-pre")(
            h3(*.color := "white")("实时客流可视化系统")
          )
        ),
        div(*.id := "main", *.style := "background-color: #282B3F; height: 90%")(
          div(*.cls := "featured-container", *.height := "100%")(
            div(*.cls := "row alt", *.height := "90%")(
              panelBox,
              ul(*.cls := "slick-dots")(
                li(flowButton),
                li(ratioButton),
                li(frequencyButton),
                li(residentButton),
                li(brandButton)
              )
            )
          )
        )
      )
    ).render
  }
}
