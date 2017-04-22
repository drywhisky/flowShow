package com.neo.sk.flowShow.frontend.components

import com.neo.sk.flowShow.frontend.utils.Component
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.html.{Anchor, Div}

/**
  * Created by whisky on 17/4/22.
  */
class MenuList (
                 showAndNameList: List[(String, String)],
                 itemClick: String => Unit,
                 activeIndex: Int = 0
               )extends Component[Div]  {

  import scalatags.JsDom.short._

  private val itemList = {
    var count = 0
    showAndNameList.map { case (show, name) =>
      val item = a(*.cls := "list-group-item", *.name := name)(show).render
      if(count == activeIndex){
        item.classList.add("active")
      }
      item.onclick = { event: MouseEvent =>
        event.preventDefault()
        internalItemClicked(item, event)
      }
      count += 1
      item
    }
  }

  private[this] var currentActiveItem = itemList.apply(activeIndex)

  private[this] def internalItemClicked(item: Anchor, event: MouseEvent): Unit = {
    println(s"MenuList item click: ${item.getAttribute("name")}")

    if (item.getAttribute("name") != currentActiveItem.getAttribute("name")) {
      currentActiveItem.classList.remove("active")
      item.classList.add("active")
      currentActiveItem = item

      itemClick(currentActiveItem.getAttribute("name"))
    } else {
      //do nothing.
    }
  }


  override protected def build(): Div = {
    div(*.cls := "list-group")(
      itemList
    ).render
  }



}
