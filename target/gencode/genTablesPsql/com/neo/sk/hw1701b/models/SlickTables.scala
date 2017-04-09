package com.neo.sk.hw1701b.models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object SlickTables extends {
  val profile = slick.jdbc.PostgresProfile
} with SlickTables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait SlickTables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = tChatLog.schema ++ tChatRoom.schema ++ tStayRecord.schema ++ tUser.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table tChatLog
   *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param roomId Database column room_id SqlType(int8)
   *  @param userId Database column user_id SqlType(int8)
   *  @param userNickName Database column user_nick_name SqlType(varchar), Length(255,true)
   *  @param content Database column content SqlType(varchar), Length(255,true)
   *  @param createTime Database column create_time SqlType(int8) */
  case class rChatLog(id: Long, roomId: Long, userId: Long, userNickName: String, content: String, createTime: Long)
  /** GetResult implicit for fetching rChatLog objects using plain SQL queries */
  implicit def GetResultrChatLog(implicit e0: GR[Long], e1: GR[String]): GR[rChatLog] = GR{
    prs => import prs._
    rChatLog.tupled((<<[Long], <<[Long], <<[Long], <<[String], <<[String], <<[Long]))
  }
  /** Table description of table chat_log. Objects of this class serve as prototypes for rows in queries. */
  class tChatLog(_tableTag: Tag) extends profile.api.Table[rChatLog](_tableTag, "chat_log") {
    def * = (id, roomId, userId, userNickName, content, createTime) <> (rChatLog.tupled, rChatLog.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(roomId), Rep.Some(userId), Rep.Some(userNickName), Rep.Some(content), Rep.Some(createTime)).shaped.<>({r=>import r._; _1.map(_=> rChatLog.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column room_id SqlType(int8) */
    val roomId: Rep[Long] = column[Long]("room_id")
    /** Database column user_id SqlType(int8) */
    val userId: Rep[Long] = column[Long]("user_id")
    /** Database column user_nick_name SqlType(varchar), Length(255,true) */
    val userNickName: Rep[String] = column[String]("user_nick_name", O.Length(255,varying=true))
    /** Database column content SqlType(varchar), Length(255,true) */
    val content: Rep[String] = column[String]("content", O.Length(255,varying=true))
    /** Database column create_time SqlType(int8) */
    val createTime: Rep[Long] = column[Long]("create_time")

    /** Index over (roomId) (database name clm_index) */
    val index1 = index("clm_index", roomId)
  }
  /** Collection-like TableQuery object for table tChatLog */
  lazy val tChatLog = new TableQuery(tag => new tChatLog(tag))

  /** Entity class storing rows of table tChatRoom
   *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param roomName Database column room_name SqlType(varchar), Length(255,true)
   *  @param state Database column state SqlType(int4), Default(1)
   *  @param createTime Database column create_time SqlType(int8) */
  case class rChatRoom(id: Long, roomName: String, state: Int = 1, createTime: Long)
  /** GetResult implicit for fetching rChatRoom objects using plain SQL queries */
  implicit def GetResultrChatRoom(implicit e0: GR[Long], e1: GR[String], e2: GR[Int]): GR[rChatRoom] = GR{
    prs => import prs._
    rChatRoom.tupled((<<[Long], <<[String], <<[Int], <<[Long]))
  }
  /** Table description of table chat_room. Objects of this class serve as prototypes for rows in queries. */
  class tChatRoom(_tableTag: Tag) extends profile.api.Table[rChatRoom](_tableTag, "chat_room") {
    def * = (id, roomName, state, createTime) <> (rChatRoom.tupled, rChatRoom.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(roomName), Rep.Some(state), Rep.Some(createTime)).shaped.<>({r=>import r._; _1.map(_=> rChatRoom.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column room_name SqlType(varchar), Length(255,true) */
    val roomName: Rep[String] = column[String]("room_name", O.Length(255,varying=true))
    /** Database column state SqlType(int4), Default(1) */
    val state: Rep[Int] = column[Int]("state", O.Default(1))
    /** Database column create_time SqlType(int8) */
    val createTime: Rep[Long] = column[Long]("create_time")
  }
  /** Collection-like TableQuery object for table tChatRoom */
  lazy val tChatRoom = new TableQuery(tag => new tChatRoom(tag))

  /** Entity class storing rows of table tStayRecord
   *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(int8)
   *  @param roomId Database column room_id SqlType(int8)
   *  @param beginTime Database column begin_time SqlType(int8)
   *  @param endTime Database column end_time SqlType(int8), Default(None) */
  case class rStayRecord(id: Long, userId: Long, roomId: Long, beginTime: Long, endTime: Option[Long] = None)
  /** GetResult implicit for fetching rStayRecord objects using plain SQL queries */
  implicit def GetResultrStayRecord(implicit e0: GR[Long], e1: GR[Option[Long]]): GR[rStayRecord] = GR{
    prs => import prs._
    rStayRecord.tupled((<<[Long], <<[Long], <<[Long], <<[Long], <<?[Long]))
  }
  /** Table description of table stay_record. Objects of this class serve as prototypes for rows in queries. */
  class tStayRecord(_tableTag: Tag) extends profile.api.Table[rStayRecord](_tableTag, "stay_record") {
    def * = (id, userId, roomId, beginTime, endTime) <> (rStayRecord.tupled, rStayRecord.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userId), Rep.Some(roomId), Rep.Some(beginTime), endTime).shaped.<>({r=>import r._; _1.map(_=> rStayRecord.tupled((_1.get, _2.get, _3.get, _4.get, _5)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(int8) */
    val userId: Rep[Long] = column[Long]("user_id")
    /** Database column room_id SqlType(int8) */
    val roomId: Rep[Long] = column[Long]("room_id")
    /** Database column begin_time SqlType(int8) */
    val beginTime: Rep[Long] = column[Long]("begin_time")
    /** Database column end_time SqlType(int8), Default(None) */
    val endTime: Rep[Option[Long]] = column[Option[Long]]("end_time", O.Default(None))
  }
  /** Collection-like TableQuery object for table tStayRecord */
  lazy val tStayRecord = new TableQuery(tag => new tStayRecord(tag))

  /** Entity class storing rows of table tUser
   *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param nickname Database column nickname SqlType(varchar), Length(255,true)
   *  @param headImg Database column head_img SqlType(varchar), Length(255,true), Default(None)
   *  @param sex Database column sex SqlType(int4)
   *  @param loginPsw Database column login_psw SqlType(varchar), Length(255,true)
   *  @param createTime Database column create_time SqlType(int8)
   *  @param phone Database column phone SqlType(varchar), Length(31,true)
   *  @param state Database column state SqlType(int4), Default(0) */
  case class rUser(id: Long, nickname: String, headImg: Option[String] = None, sex: Int, loginPsw: String, createTime: Long, phone: String, state: Int = 0)
  /** GetResult implicit for fetching rUser objects using plain SQL queries */
  implicit def GetResultrUser(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[Int]): GR[rUser] = GR{
    prs => import prs._
    rUser.tupled((<<[Long], <<[String], <<?[String], <<[Int], <<[String], <<[Long], <<[String], <<[Int]))
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class tUser(_tableTag: Tag) extends profile.api.Table[rUser](_tableTag, "user") {
    def * = (id, nickname, headImg, sex, loginPsw, createTime, phone, state) <> (rUser.tupled, rUser.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(nickname), headImg, Rep.Some(sex), Rep.Some(loginPsw), Rep.Some(createTime), Rep.Some(phone), Rep.Some(state)).shaped.<>({r=>import r._; _1.map(_=> rUser.tupled((_1.get, _2.get, _3, _4.get, _5.get, _6.get, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column nickname SqlType(varchar), Length(255,true) */
    val nickname: Rep[String] = column[String]("nickname", O.Length(255,varying=true))
    /** Database column head_img SqlType(varchar), Length(255,true), Default(None) */
    val headImg: Rep[Option[String]] = column[Option[String]]("head_img", O.Length(255,varying=true), O.Default(None))
    /** Database column sex SqlType(int4) */
    val sex: Rep[Int] = column[Int]("sex")
    /** Database column login_psw SqlType(varchar), Length(255,true) */
    val loginPsw: Rep[String] = column[String]("login_psw", O.Length(255,varying=true))
    /** Database column create_time SqlType(int8) */
    val createTime: Rep[Long] = column[Long]("create_time")
    /** Database column phone SqlType(varchar), Length(31,true) */
    val phone: Rep[String] = column[String]("phone", O.Length(31,varying=true))
    /** Database column state SqlType(int4), Default(0) */
    val state: Rep[Int] = column[Int]("state", O.Default(0))
  }
  /** Collection-like TableQuery object for table tUser */
  lazy val tUser = new TableQuery(tag => new tUser(tag))
}
