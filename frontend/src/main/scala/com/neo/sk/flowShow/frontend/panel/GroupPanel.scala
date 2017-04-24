package com.neo.sk.flowShow.frontend.panel

import com.neo.sk.flowShow.frontend.Routes
import com.neo.sk.flowShow.frontend.utils.Panel
import org.scalajs.dom.html.Div
import io.circe.syntax._
import io.circe.generic.auto._

import scalatags.JsDom.short._
import com.neo.sk.flowShow.frontend.utils.{Http, JsFunc}
import com.neo.sk.flowShow.ptcl._
import io.circe.generic.auto._
import org.scalajs.dom.{MouseEvent, XMLHttpRequest, Event}
import com.neo.sk.flowShow.frontend.utils.{Modal, MyUtil}
import org.scalajs.dom.raw.FormData
import org.scalajs.dom

import org.scalajs.dom.html.IFrame
import scala.collection.mutable
import scala.scalajs.js.Date
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js


/**
  * Created by whisky on 17/4/22.
  */
object GroupPanel extends Panel{

  override def locationHash = ""

  private val currentDiv = div().render

  private val tmp = GroupListPanel.render

  val GroupMap = mutable.HashMap[Long, Group]()

  val BoxMap = mutable.HashMap[Long, Box]()

  currentDiv.appendChild(tmp)

  def SetContent(component:Div) = {
    currentDiv.textContent = ""
    currentDiv.appendChild(component)
  }

  override def build():Div = {
    currentDiv
  }


}

object GroupListPanel extends Panel{

  private val groupList = div(div()).render

  private val editBox = div().render

  private val createGroupButton = button(*.cls := "btn btn-warning")("+添加区域").render

  createGroupButton.onclick = { e: MouseEvent =>
    e.preventDefault()

    val modalName = input(*.`type` := "text", *.cls := "form-control").render
    val modalDua = input(*.`type` := "text", *.cls := "form-control").render
    val upLoadButton = button(*.cls := "btn btn-warning")("上传").render
    var fileUrl = ""

    val floorSvg = div().render

    val file = input(*.`type` := "file", *.name := "fileUpload").render
    val fileUpload = form(*.enctype := "multipart/form-data", *.cls := "pure-form", *.method := "post")(
      file
    ).render

    @js.native
    def uploadFile(url: String) ={
      val oData = new FormData(fileUpload)
      val oReq = new XMLHttpRequest()
      oReq.open("POST", url, true)
      oReq.send(oData)
      oReq.onreadystatechange = { e: Event =>
        if (oReq.readyState == 4) {
          if (oReq.status == 200) {
            val message = Future(oReq.responseText)
            Http.parser[CommonRsp](message).map {
              case Right(info) =>
                fileUrl = "/flowShow/static/map/" + info.msg
                println(s"svg url= ${info.msg}")
                floorSvg.innerHTML = ""
                floorSvg.appendChild(
                  iframe(*.src := fileUrl).render
                )

              case Left(error) =>
                println(s"parse error:$error")
                JsFunc.alert(s"上传失败！parse error:$error")
            }
          }
        }
      }
    }

    upLoadButton.onclick = { e: MouseEvent =>
      e.preventDefault()
      uploadFile(Routes.imageUpload)
    }


    val header = div(*.cls := "modal-title")("新建区域")
    val body = div(*.cls := "row", *.textAlign.center)(
      form(*.cls := "form-horizontal", *.textAlign.center, *.style := "margin-top:10px;")(
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label", *.color := "black")("区域名称"),
          div(*.cls := "col-md-4")(modalName)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label", *.color := "black")("驻留时长"),
          div(*.cls := "col-md-4")(
            div(*.cls := "input-group")(
              modalDua,
              span(*.cls := "input-group-addon")("分钟")
            )
          )
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label", *.color := "black")("区域地图"),
          div(*.cls := "col-md-4")(upLoadButton, fileUpload, floorSvg)
        )
      )
    )

    def clickFunction():Unit = {
      if (modalName.value == "" || modalDua.value == "" || fileUrl == "") {
        JsFunc.alert(s"error!")
      } else {
        val data = AddGroup(modalName.value, modalDua.value.toLong * 60000, fileUrl).asJson.noSpaces
        Http.postJsonAndParse[AddGroupRsp](Routes.addGroup, data).map { rsp =>
          if (rsp.errCode == 0) {
            JsFunc.alert(s"success")
            val newGroup = Group(rsp.id.getOrElse(0l), modalName.value, rsp.timestamp.getOrElse(0l), modalDua.value.toLong, fileUrl)
            GroupPanel.GroupMap.put(newGroup.id, newGroup)
            makeGroupList(GroupPanel.GroupMap)
          } else {
            JsFunc.alert(s"error: ${rsp.msg}")
          }
        }
      }
    }

    val model = new Modal(
      header.render,
      body.render,
      clickFunction,
      ""
    )

    model.show
    editBox.textContent = ""
    editBox.appendChild(model.render)

  }


  private def editAction(id: Long, name: String, time: Long, durationLength: Long, map:String) = {

    val modalId = input(*.`type` := "text", *.cls := "form-control", *.value := id, *.disabled := true).render
    val modalName = input(*.`type` := "text", *.cls := "form-control", *.value := name).render
    val modalTime = input(*.`type` := "text", *.cls := "form-control", *.value := MyUtil.DateFormatter(new Date(time), "YYYY-MM-DD hh:mm:ss"), *.disabled := true).render
    val modalDua = input(*.`type` := "text", *.cls := "form-control", *.value := durationLength).render


    val header = div(*.cls := "modal-title")("更新区域信息")
    val body = div(*.cls := "row", *.textAlign.center)(
      form(*.cls := "form-horizontal", *.textAlign.center, *.style := "margin-top:10px;")(
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label", *.color := "black")("区域id"),
          div(*.cls := "col-md-4")(modalId)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label", *.color := "black")("区域名称"),
          div(*.cls := "col-md-4")(modalName)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label", *.color := "black")("创建时间"),
          div(*.cls := "col-md-4")(modalTime)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label", *.color := "black")("驻留时长(min)"),
          div(*.cls := "col-md-4")(
            div(*.cls := "input-group")(
              modalDua,
              span(*.cls := "input-group-addon")("分钟")
            )
          )
        )
      )
    )

    def clickFunction():Unit = {
      if (modalName.value == "" || modalDua.value == "") {
        JsFunc.alert(s"error!")
      } else {
        val data = ModifyGroup(id, modalName.value, modalDua.value.toLong).asJson.noSpaces
        Http.postJsonAndParse[CommonRsp](Routes.modifyGroup, data).map { rsp =>
          if (rsp.errCode == 0) {
            JsFunc.alert(s"success")
            GroupPanel.GroupMap.update(id, Group(id, modalName.value, time, modalDua.value.toLong, map))
            makeGroupList(GroupPanel.GroupMap)
          } else {
            JsFunc.alert(s"error: ${rsp.msg}")
          }
        }
      }
    }

    val model = new Modal(
      header.render,
      body.render,
      clickFunction,
      ""
    )

    model.show
    editBox.textContent = ""
    editBox.appendChild(model.render)
  }

  private def makeGroupList(groups: mutable.HashMap[Long, Group]) : Unit= {

    def makeRow(group: Group) = {
      val editButton = button(*.cls := "btn btn-info", *.marginRight := "5px")("编辑").render
      val boxButton = button(*.cls := "btn btn-info")("查看盒子").render


      editButton.onclick = { e: MouseEvent =>
        e.preventDefault()
        editAction(group.id, group.name, group.createTime, group.durationLength, group.map)
      }

      boxButton.onclick = { e: MouseEvent =>
        e.preventDefault()
        GroupPanel.SetContent(new BoxListPanel(group.id, group.name, group.map).render)
      }

      tr(
        td(group.id),
        td(group.name),
        td(MyUtil.DateFormatter(new Date(group.createTime), "YYYY-MM-DD hh:mm:ss")),
        td(group.durationLength),
        td(editButton, boxButton)
      )
    }

    val newDiv = div(
      table(*.cls := "table")(
        thead(
          tr(
            th(*.textAlign.center)("#"),
            th(*.textAlign.center)("名称"),
            th(*.textAlign.center)("创建时间"),
            th(*.textAlign.center)("驻留时长"),
            th(*.textAlign.center)("操作")
          )
        ),
        tbody(*.textAlign.center)(
          groups.toList.map(_._2).map(m => makeRow(m))
        )
      )
    ).render

    groupList.replaceChild(newDiv, groupList.firstChild)
  }

  private def getGroups() = {
    Http.getAndParse[GroupsRsp](Routes.getGroups).map { rsp =>
      if (rsp.errCode == 0) {
        println(s"success")
        GroupPanel.GroupMap ++= rsp.data.map( g => (g.id, g))
        makeGroupList(GroupPanel.GroupMap)
      } else {
        println(s"getCategoryList error: ${rsp.msg}")
      }
    }
  }

  override def locationHash = ""

  override protected def build() : Div = {
    getGroups()
    div(
      div(*.cls := "row")(
        div(*.cls := "col-md-2")(h3("区域管理")),
        div(*.cls := "col-md-2 col-md-offset-8")(createGroupButton)
      ),
      div(*.cls := "row",*.style := "margin-top:20px;")(
        groupList
      ),
      div(*.cls := "row",*.style := "margin-top:20px;")(
        editBox
      )
    ).render
  }

}

class BoxListPanel(groupId: Long, name: String, map:String) extends Panel {

  private val createBoxButton = button(*.cls := "btn btn-warning")("+添加盒子").render

  private val ImgSvg = div(*.width := "100%").render

  private val boxList = div(div()).render

  private val editBox = div().render

  private def editAction(id: Long, name: String, mac:String, time: Long, rssi: Int, x: Double, y: Double) : Unit= {

    val modalId = input(*.`type` := "text", *.cls := "form-control", *.value := id, *.disabled := true).render
    val modalMac = input(*.`type` := "text", *.cls := "form-control", *.value := mac, *.disabled := true).render
    val modalName = input(*.`type` := "text", *.cls := "form-control", *.value := name).render
    val modalTime = input(*.`type` := "text", *.cls := "form-control", *.value := MyUtil.DateFormatter(new Date(time), "YYYY-MM-DD hh:mm:ss"), *.disabled := true).render
    val modalRssi = input(*.`type` := "text", *.cls := "form-control", *.value := rssi).render


    val header = div(*.cls := "modal-title")("更新区域信息")
    val body = div(*.cls := "row", *.textAlign.center)(
      form(*.cls := "form-horizontal", *.textAlign.center, *.style := "margin-top:10px;")(
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label", *.color := "black")("盒子id"),
          div(*.cls := "col-md-4")(modalId)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label", *.color := "black")("盒子名称"),
          div(*.cls := "col-md-4")(modalName)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label", *.color := "black")("盒子mac"),
          div(*.cls := "col-md-4")(modalMac)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label", *.color := "black")("创建时间"),
          div(*.cls := "col-md-4")(modalTime)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label", *.color := "black")("rssi"),
          div(*.cls := "col-md-4")(modalRssi)
        )
      )
    )

    def clickFunction():Unit = {
      if (modalName.value == "" || modalRssi.value == "") {
        JsFunc.alert(s"error!")
      } else {
        val data = ModifyBox(id, mac, modalName.value, modalRssi.value.toInt).asJson.noSpaces
        Http.postJsonAndParse[CommonRsp](Routes.modifyBox, data).map { rsp =>
          if (rsp.errCode == 0) {
            JsFunc.alert(s"success")
            GroupPanel.BoxMap.update(id, Box(id, modalName.value, mac, time, modalRssi.value.toInt, x, y))
            makeBoxList(GroupPanel.BoxMap)
          } else {
            JsFunc.alert(s"error: ${rsp.msg}")
          }
        }
      }
    }

    val model = new Modal(
      header.render,
      body.render,
      clickFunction,
      ""
    )

    model.show
    editBox.textContent = ""
    editBox.appendChild(model.render)
  }


  private def makeBoxList(boxs: mutable.HashMap[Long, Box]) : Unit = {

    def makeRow(box: Box) = {
      val editButton = button(*.cls := "btn btn-info")("编辑").render

      editButton.onclick = { e: MouseEvent =>
        e.preventDefault()
        editAction(box.id, box.name, box.mac, box.createTime, box.rssi, box.x, box.y)
      }

      tr(
        td(box.id),
        td(box.name),
        td(box.mac),
        td(MyUtil.DateFormatter(new Date(box.createTime), "YYYY-MM-DD hh:mm:ss")),
        td(box.rssi),
        td(editButton)
      )
    }

    val newDiv = div(
      table(*.cls := "table")(
        thead(
          tr(
            th(*.textAlign.center)("#"),
            th(*.textAlign.center)("名称"),
            th(*.textAlign.center)("mac"),
            th(*.textAlign.center)("创建时间"),
            th(*.textAlign.center)("rssi"),
            th(*.textAlign.center)("操作")
          )
        ),
        tbody(*.textAlign.center)(
          boxs.toList.map(_._2).map(m => makeRow(m))
        )
      )
    ).render

    boxList.replaceChild(newDiv, boxList.firstChild)
  }


  override def locationHash = ""

  private def getBox(groupId: Long) = {
    Http.getAndParse[BoxsRsp](Routes.getBoxs(groupId)).map { rsp =>
      if (rsp.errCode == 0) {
        println(s"success")
        GroupPanel.BoxMap ++= rsp.data.map(g => (g.id, g))
        makeBoxList(GroupPanel.BoxMap)
      } else {
        println(s"getCategoryList error: ${rsp.msg}")
      }
    }
  }

  def getImg(map:String) = {
    ImgSvg.innerHTML = ""
    ImgSvg.appendChild(
      iframe(*.id := "svg", *.src := map, *.width := "100%", *.height := "300px", *.onclick := {
        e: MouseEvent =>
          e.preventDefault()
          println(s"iframe click.")
          getMouseXY(e)
      }).render
    )
  }

  def getMouseXY(e : MouseEvent) = {
    val x = e.clientX
    val y = e.clientY
    newBox(x, y)
  }

  def newBox(x:Double, y:Double) = {

    val modalName = input(*.`type` := "text", *.cls := "form-control").render
    val modalMac = input(*.`type` := "text", *.cls := "form-control").render
    val modalRssi = input(*.`type` := "text", *.cls := "form-control").render

    val header = div(*.cls := "modal-title")("新建盒子")
    val body = div(*.cls := "row", *.textAlign.center)(
      form(*.cls := "form-horizontal", *.textAlign.center, *.style := "margin-top:10px;")(
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label", *.color := "black")("盒子名称"),
          div(*.cls := "col-md-4")(modalName)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label", *.color := "black")("盒子mac"),
          div(*.cls := "col-md-4")(modalMac)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label", *.color := "black")("rssi"),
          div(*.cls := "col-md-4")(modalRssi)
        )
      )
    )

    def clickFunction(): Unit = {
      if (modalName.value == "" || modalMac.value == "" || modalRssi.value == "") {
        JsFunc.alert(s"error!")
      } else {
        val data = AddBox(modalName.value, modalMac.value, modalRssi.value.toInt, groupId, x, y).asJson.noSpaces
        Http.postJsonAndParse[AddGroupRsp](Routes.addBox, data).map { rsp =>
          if (rsp.errCode == 0) {
            JsFunc.alert(s"success")
            val newBox = Box(rsp.id.getOrElse(0l), modalName.value, modalMac.value, rsp.timestamp.getOrElse(0l), modalRssi.value.toInt, x, y)
            GroupPanel.BoxMap.put(newBox.id, newBox)
            makeBoxList(GroupPanel.BoxMap)
          } else {
            JsFunc.alert(s"error: ${rsp.msg}")
          }
        }
      }
    }

    val model = new Modal(
      header.render,
      body.render,
      clickFunction,
      ""
    )

    model.show
    editBox.textContent = ""
    editBox.appendChild(model.render)
  }

  override protected def build(): Div = {
    getBox(groupId)
    getImg(map)
    div(
      div(*.cls := "row")(
        div(*.cls := "col-md-5", *.display := "inline-block")(
          a(*.onclick := { e: MouseEvent =>
            e.preventDefault()
            GroupPanel.SetContent(GroupListPanel.render)
          }, *.fontSize := "x-large"
          )(s"区域管理/${name}区")
        ),
        div(*.cls := "col-md-2 col-md-offset-5")(createBoxButton)
      ),
      div(*.cls := "row", *.style := "margin-top:20px;", *.width := "100%")(
        ImgSvg
      ),
      div(*.cls := "row", *.style := "margin-top:20px;")(
        boxList
      ),
      div(*.cls := "row", *.style := "margin-top:20px;")(
        editBox
      )
    ).render
  }

}
