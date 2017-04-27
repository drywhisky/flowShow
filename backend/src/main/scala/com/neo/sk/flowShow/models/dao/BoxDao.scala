package com.neo.sk.flowShow.models.dao

import com.neo.sk.flowShow.models.SlickTables._
import com.neo.sk.utils.DBUtil.db
import slick.dbio.DBIOAction
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Created by whisky on 17/4/14.
  */
object BoxDao {

  def listDistributedBoxs = db.run(
    tBoxs.result
  )

  def listBoxs(groupId:Long, userId:Long) = db.run(
    tBoxs.filter(c => (c.groupId === groupId) && (c.userId === userId)).result
  )

  def listAllBoxs(userId: Long) = db.run(
    tBoxs.filter(_.userId === userId).result
  )

  def addBox(name:String, mac:String, rssi:Int, userId:Long, groupId:Long, timestamp:Long, x: Option[Double], y: Option[Double]) = {
    val actions = for {
      exists1 <- tBoxs.filter(_.boxMac === mac).exists.result
      id <-
      if(exists1)
        DBIOAction.successful(-1l)
      else
        tBoxs.returning(tBoxs.map(_.boxId)).+=(rBoxs(-1l, name, mac, timestamp, userId, groupId, rssi, x, y))
    } yield {
      id
    }

    db.run(actions)
  }

  def modifyBox(id:Long, name:String, rssi:Int) = db.run(
    tBoxs.filter(_.boxId === id).map(r => (r.boxName, r.rssiSet)).update((name, rssi))
  )

}
