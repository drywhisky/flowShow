package com.neo.sk.flowShow.service

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import com.neo.sk.utils.{CirceSupport, SecureUtil}
import org.slf4j.LoggerFactory
import akka.pattern.ask
import akka.util.Timeout
import com.neo.sk.flowShow.ptcl._
import io.circe.generic.auto._
import akka.http.scaladsl.server.Directives._
import com.neo.sk.flowShow.models.dao.{BoxDao, GroupDao, UserDao}
import com.neo.sk.flowShow.service.SessionBase.{UserSession, UserSessionKey}
import com.neo.sk.flowShow.core.GroupManager._
import scala.concurrent.ExecutionContext.Implicits.global
import io.circe.Error

/**
  * Created by whisky on 17/4/22.
  */
trait UserService extends ServiceUtils with SessionBase with CirceSupport{

  private val log = LoggerFactory.getLogger("com.neo.sk.hw1701b.service.UserService")

  val groupManager: ActorRef

  implicit val timeout: Timeout

  val userRoutes = pathPrefix("user")(
    staticRoutes ~ loginSubmit ~ registerSubmit ~ logout ~
      getGroups ~ getBoxs ~ addBox ~ addGroup ~ modifyGroup ~
      modifyBox ~ imageUpload ~ getAllBoxs

  )

  private val staticRoutes = (path("login" | "register") & get) {
    getFromResource("html/login.html")
  } ~ (path("home") & get & pathEndOrSingleSlash) {
    UserAction { _ =>
      getFromResource("html/index.html")
    }
  }

  private val loginSubmit = (path("loginSubmit") & post & pathEndOrSingleSlash) {
    entity(as[Either[Error, UserConfirm]]) {
      case Right(req) =>
        dealFutureResult {
          UserDao.getAccount(req.account).map {
            case Some(info) =>
              if (SecureUtil.getSecurePassword(req.psw, info.userName, info.createTime) == info.loginPsw) {
                setUserSession(UserSession(info.userId, System.currentTimeMillis())) {
                  complete(CommonRsp())
                }
              } else {
                log.warn(s"UserService:wrong password.")
                complete(CommonRsp(100100, "wrong password."))
              }

            case None =>
              log.error(s"UserService:can't find this account.")
              complete(CommonRsp(100101, "can't find this account."))
          }
        }

      case Left(error) =>
        log.warn(s"error: $error")
        complete(CommonRsp(104002, "parse error."))
    }
  }

  private val registerSubmit = (path("registerSubmit") & post & pathEndOrSingleSlash) {
    entity(as[Either[Error, UserRegisterInfo]]) {
      case Right(info) =>
        dealFutureResult {
          val time = System.currentTimeMillis()
          UserDao.newUser(info.account, SecureUtil.getSecurePassword(info.password, info.account, time), time).map {
            id =>
              if (id == -1)
                complete(CommonRsp(104003, "This account already exists."))
              else {
                val session = Map(
                  UserSessionKey.uid -> id.toString,
                  UserSessionKey.loginTime -> time.toString
                )
                addSession(session) { ctx =>
                  ctx.complete(CommonRsp())
                }
              }
          }
        }

      case Left(e) =>
        complete(CommonRsp(104002, "parse error."))
    }
  }

  private val logout = (path("logout") & get & pathEndOrSingleSlash) {
    invalidateSession {
      redirect("/flowShow/user/login", StatusCodes.SeeOther)
    }
  }

  private val getGroups = (path("getGroups") & get & pathEndOrSingleSlash) {
    UserAction { user =>
      dealFutureResult {
        GroupDao.listGroupsByUserId(user.uid).map { res =>
            val groupList = res.map {
              r => Group(r.groupId, r.groupName, r.createTime, r.durationLength, r.map, r.scala)
            }.toList
            complete(GroupsRsp(data = groupList))
        }.recover{
          case t =>
            complete(CommonRsp(104004, s"error.$t"))
        }
      }
    }
  }

  private val getBoxs = (path("getBoxs") & get & pathEndOrSingleSlash) {
    UserAction { user =>
      parameters(
        'groupId.as[Long]
      ) { case (groupId) =>
        dealFutureResult {
          BoxDao.listBoxs(groupId, user.uid).map { res =>
            val boxList = res.map {
              r => Box(r.boxId, r.boxName, r.boxMac, r.createTime, r.rssiSet, r.x, r.y)
            }.toList
            complete(BoxsRsp(data = boxList))
          }.recover {
            case t =>
              complete(CommonRsp(104005, s"error.$t"))
          }
        }
      }
    }
  }

  private val addGroup = (path("addGroup") & post & pathEndOrSingleSlash) {
    UserAction { user =>
      entity(as[Either[Error, AddGroup]]){
        case Right(req) =>
          dealFutureResult {
            groupManager.ask(AddGroupMsg(req, user.uid)).map {
              case (id: Long, time: Long) =>
                complete(AddGroupRsp(id = Some(id), timestamp = Some(time)))

              case "Error" =>
                complete(AddGroupRsp(None, None, 104006, "Error"))
            }
          }

        case Left(e) =>
          complete(AddGroupRsp(None, None, 104002, "parse error."))
      }
    }
  }

  private val addBox = (path("addBox") & post & pathEndOrSingleSlash) {
    UserAction { user =>
      entity(as[Either[Error, AddBox]]){
        case Right(req) =>
          dealFutureResult {
            groupManager.ask(AddBoxMsg(req, user.uid)).map {
              case (id: Long, time: Long) =>
                complete(AddGroupRsp(id = Some(id), timestamp = Some(time)))

              case "Error" =>
                complete(AddGroupRsp(None, None, 104007, "Error"))
            }
          }

        case Left(e) =>
          complete(AddGroupRsp(None, None, 104002, "parse error."))
      }
    }
  }

  private val modifyGroup = (path("modifyGroup") & post & pathEndOrSingleSlash) {
    UserAction { user =>
      entity(as[Either[Error, ModifyGroup]]){
        case Right(req) =>
          dealFutureResult {
            groupManager.ask(ModifyGroupMsg(req)).map {
              case "OK" =>
                complete(CommonRsp())

              case "Error" =>
                complete(CommonRsp(104008, "Error"))
            }
          }

        case Left(e) =>
          complete(CommonRsp(104002, "parse error."))
      }
    }
  }

  private val modifyBox = (path("modifyBox") & post & pathEndOrSingleSlash) {
    UserAction { user =>
      entity(as[Either[Error, ModifyBox]]){
        case Right(req) =>
          dealFutureResult {
            groupManager.ask(ModifyBoxMsg(req)).map {
              case "OK" =>
                complete(CommonRsp())

              case "Error" =>
                complete(CommonRsp(104009, "Error"))
            }
          }

        case Left(e) =>
          complete(CommonRsp(104002, "parse error."))
      }
    }
  }

  private val imageUpload = (path("imageUpload") & post & pathEndOrSingleSlash){
    import com.neo.sk.utils.ImageUtil._
    UserAction { _ =>
      uploadedFile("fileUpload") {
        case(metadata, tmpFile) =>
          val imgName = getExtName(metadata.fileName).getOrElse("")
          val destFile = storeTmpFIle(tmpFile, imgName)
          complete(CommonRsp(0, destFile.getName))
      }
    }
  }

  private val getAllBoxs = (path("getAllBoxs") & get & pathEndOrSingleSlash){
    UserAction{ user =>
      dealFutureResult {
        BoxDao.listAllBoxs(user.uid).map { res =>
          val boxList = res.map {
            r => Box(r.boxId, r.boxName, r.boxMac, r.createTime, r.rssiSet, r.x, r.y)
          }.toList
          complete(BoxsRsp(data = boxList))
        }.recover {
          case t =>
            complete(CommonRsp(104005, s"error.$t"))
        }
      }
    }
  }


}
