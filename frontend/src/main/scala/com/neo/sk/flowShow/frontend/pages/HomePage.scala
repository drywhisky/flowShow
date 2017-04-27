package com.neo.sk.flowShow.frontend.pages

import com.neo.sk.flowShow.frontend.components.MenuList
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

  private def listItemClick(name: String): Unit = {
    println(s"HomePage item click: $name")
    val target = name match {
      case "111" => GroupPanel.render
      case "222" => AreaPanel.render
      case "333" => FlowPanel.render
      case _ => GroupPanel.render
    }

    panelBox.textContent = ""
    panelBox.appendChild(target)

  }

  private val itemList = List(
    ("区域管理", "111"),
    ("区域数据呈现", "222"),
    ("用户行为跟踪", "333")
  )

  private val menuList = new MenuList(itemList, listItemClick).render

  private val panelBox = div(*.cls := "col-md-10")(GroupPanel.render).render

  override def locationHash: String = ""

  override def build(): Div = {
    div(*.id := "artpip", *.height := "100%")(
      div(*.id := "content", *.height := "100%")(
        div(*.cls := "navbar navbar-default", *.role := "navigation", *.style := "background-color: #282b3f;", *.height := "10%")(
          div(*.cls := "navbar-pre")(),
          div(*.cls := "navbar-container")(
            div(*.cls := "header-left")(
              h3(*.color := "white")("实时客流可视化系统")
            ),
            div(*.cls := "header-right", *.left := "70%")(
              a(*.color := "white", *.href := "/flowShow/user/logout", *.fontSize := "large")("退出")
            )
          )
        ),
        div(*.id := "main", *.style := "background-color: #282B3F; height: 90%")(
          div(*.cls := "featured-container", *.height := "100%")(
            div(*.cls := "row alt", *.style := "background-color: #282B3F;")(
              div(*.cls := "col-md-2", *.textAlign.center)(
                menuList
              ),
              panelBox
            )
          )
        )
      )
    ).render
  }
}
