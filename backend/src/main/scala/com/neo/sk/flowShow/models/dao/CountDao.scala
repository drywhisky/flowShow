package com.neo.sk.flowShow.models.dao

import com.neo.sk.flowShow.models.SlickTables._
import com.neo.sk.utils.DBUtil.db
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Created by whisky on 17/4/17.
  */
object CountDao {

  def addCountDetail(groupId: String, count: (Long, Int)) = {
    db.run(tCountDetail.+=(rCountDetail(-1l, groupId, count._1, count._2)))
  }

  def getCountDetailByInterval(groupId: String, startTime: Long, endTime: Long) = db.run{
    tCountDetail.filter(r => r.groupId === groupId && r.timestamp >= startTime && r.timestamp < endTime).result
  }

  def userIn(clientMac:String, groupId:Long, time:Long) = db.run(
    tUserAction.+=(rUserAction(-1l, clientMac, groupId, time, None))
  )

  def userOut(clientMac:String, groupId:Long, time: Long) = {
    val actions = for {
      r1 <- tUserAction.filter(r => (r.cilentMac === clientMac) && (r.groupId === groupId)).map(_.id).max.result
      r2 <- tUserAction.filter(_.id === r1).map(_.outTime).update(Some(time))
    } yield{
      r2
    }

    db.run(actions.transactionally)

  }


}