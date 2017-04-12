package com.neo.sk.flowShow.frontend.pages

import com.neo.sk.flowShow.frontend.utils.Page
import org.scalajs.dom.html.Div

import scalatags.JsDom.short._
import com.neo.sk.flowShow.frontend.panel._
import org.scalajs.dom.MouseEvent

/**
  * Created by dry on 2017/4/10.
  */
object HomePage extends Page {

  private val panelBox = FlowPanel.render

  private val flowButton = button(*.onclick := { e: MouseEvent =>
    e.preventDefault()
    panelBox.textContent = ""
    panelBox.appendChild(FlowPanel.render)
  })(1).render

  private val frequencyButton = button(*.onclick := { e: MouseEvent =>
    e.preventDefault()
    panelBox.textContent = ""
    panelBox.appendChild(FrequencyPanel.render)
  })(2).render

  private val ratioButton = button(*.onclick := { e: MouseEvent =>
    e.preventDefault()
    panelBox.textContent = ""
    panelBox.appendChild(RatioPanel.render)
  })(3).render

  private val residentButton = button(*.onclick := { e: MouseEvent =>
    e.preventDefault()
    panelBox.textContent = ""
    panelBox.appendChild(ResidentPanel.render)
  })(4)

  private val brandButton = button(*.onclick := { e: MouseEvent =>
    e.preventDefault()
    panelBox.textContent = ""
    panelBox.appendChild(BrandPanel.render)
  })(5).render

  override def locationHash: String = ""

  override def build(): Div = {
    div(*.id := "artpip")(
      div(*.id := "content")(
        div(*.cls := "navbar navbar-default", *.role := "navigation")(
          div(*.cls := "navbar-pre")(
            h3("实时客流可视化系统")
          )
        ),
        div(*.id := "main")(
          div(*.cls := "featured-container")(
            div(*.cls := "row alt")(
              panelBox,
              ul(*.cls := "slick-dots")(
                li(flowButton),
                li(frequencyButton),
                li(ratioButton),
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
