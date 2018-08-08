package pdorobisz.scala.examples.cats.core.examples

import cats._
import cats.data.EitherT
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

/**
  * EitherT monad transformer and Traverse.
  */
object Example1 extends App {

  case class ServiceError(m: String)

  case class User(name: String)

  def toUser(s: String): EitherT[Future, ServiceError, User] =
    if (s.isEmpty) EitherT.leftT[Future, User](ServiceError("empty name")) else EitherT.rightT[Future, ServiceError](User(s))

  def saveUsers(users: List[User]): EitherT[Future, ServiceError, Unit] = {
    println("saving users: " + users)
    if (users.isEmpty) EitherT.leftT[Future, Unit](ServiceError("empty users list")) else EitherT.rightT[Future, ServiceError](())
  }

  val userNames = List("John", "Mark")

  val result = for {
    // traverse converts List[String] to EitherT[Future, ServiceError, List[User]]
    // by applying String => EitherT[Future, ServiceError, User] to every element
    users <- Traverse[List].traverse(userNames)(toUser)
    // now we can simply pass converted list of users (List[User]) to save method without having to explicitly deal
    // with all types (Future, Either) surrounding it
    saveResult <- saveUsers(users)
  } yield saveResult

  println(Await.result(result.value, Duration.Inf))

}
