package com.neo.sk.flowShow.frontend.components

import org.scalajs.dom.html.Div
import com.neo.sk.flowShow.frontend.utils.Component

/**
  * User: Taoz
  * Date: 3/18/2017
  * Time: 8:04 PM
  */
object TopNavigation extends Component[Div]{


  import scalatags.JsDom.short._

  override protected def build(): Div = {
    div(*.cls := "container")(
      div(*.cls := "collapse navbar-collapse")(
        h1("实时客流可视化系统")
      )
    ).render
  }


}
