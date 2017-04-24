package com.neo.sk.flowShow.frontend

/**
  * User: Taoz
  * Date: 1/16/2017
  * Time: 6:49 PM
  */
object Routes {


  val baseUrl = "/flowShow/user"

  val login = baseUrl + "/login"
  val loginSubmit = baseUrl + "/loginSubmit"
  val registerSubmit = baseUrl + "/registerSubmit"
  val home = baseUrl + "/home"
  val register = baseUrl + "/register"

  val imageUpload = baseUrl + "/imageUpload"
  val getGroups = baseUrl + "/getGroups"
  val modifyGroup = baseUrl + "/modifyGroup"
  val addGroup = baseUrl + "/addGroup"

  def getBoxs(groupId:Long) = {
    baseUrl + s"groupId:$groupId"
  }
  val modifyBox = baseUrl + "/modifyBox"
  val addBox = baseUrl + "/addBox"

  val brandUrl = baseUrl + "/brandInfo"
  val ratioUrl = baseUrl + "/ratioInfo"
  val frequencyUrl = baseUrl + "/frequencyInfo"
  val residentUrl = baseUrl + "/residentInfo"
  val realTimeUrl = baseUrl + "/realTimeInfo"


}
