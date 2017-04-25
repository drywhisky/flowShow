package com.neo.sk.flowShow.models.dao

import com.neo.sk.flowShow.models.SlickTables._
import com.neo.sk.utils.DBUtil.db
import slick.dbio.DBIOAction
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by whisky on 17/4/22.
  */
object UserDao {

  def getAccount(userName:String) = db.run(
    tUsers.filter(_.userName === userName).result.headOption
  )

  def newUser(account:String, password:String, curTime:Long) = {
    val actions = for {
      exists1 <- tUsers.filter(_.userName === account).exists.result
      id <-
      if(exists1)
        DBIOAction.successful(-1l)
      else
        tUsers.returning(tUsers.map(_.userId)).+=(rUsers(-1l, account, curTime, password))
    } yield {
      id
    }

    db.run(actions)
  }



}
