package pdorobisz.freemonad

import cats._
import cats.data.{EitherT, OptionT}
import cats.free.Free
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

// Returning effects like Option or Either in interpreter
object FreeMonadExample4 extends App {

  case class User(id: Long, name: String)

  // Algebra - we're not using Option/Either here as a return type
  sealed trait UserAlgebra[T]

  // Return type is just User, not Option[User]
  case class GetUser(id: Long) extends UserAlgebra[User]

  case class DeleteUser(u: User) extends UserAlgebra[Unit]

  // Free type
  type UserF[A] = Free[UserAlgebra, A]

  // DSL
  def getUser(id: Long): UserF[User] = Free.liftF(GetUser(id))

  def deleteUser(u: User): UserF[Unit] = Free.liftF(DeleteUser(u))

  // Program
  def program(id: Long): Free[UserAlgebra, User] = for {
    u <- getUser(id)
    _ <- deleteUser(u)
  } yield u

  // Interpreter - now we specify effects we want to use (e.g. Option or Either)
  type FutureEither[A] = EitherT[Future, String, A]
  val eitherInterpreter: UserAlgebra ~> FutureEither = new (UserAlgebra ~> FutureEither) {
    override def apply[A](fa: UserAlgebra[A]): FutureEither[A] = fa match {
      case GetUser(id) => EitherT.pure[Future, String](User(id, "John Doe"))
      case DeleteUser(u) => EitherT.fromEither[Future](Left(s"Failed to delete user ${u.id}"))
    }
  }

  type FutureOption[A] = OptionT[Future, A]
  val optionInterpreter: UserAlgebra ~> FutureOption = new (UserAlgebra ~> FutureOption) {
    override def apply[A](fa: UserAlgebra[A]): FutureOption[A] = fa match {
      case GetUser(id) => OptionT.pure[Future](User(id, "John Doe"))
      case DeleteUser(u) => OptionT.none[Future, Unit]
    }
  }

  // User different interpreters to run program
  val eitherResult: Future[Either[String, User]] = program(123L).foldMap(eitherInterpreter).value
  println(Await.result(eitherResult, Duration.Inf))

  val optionResult: Future[Option[User]] = program(123L).foldMap(optionInterpreter).value
  println(Await.result(optionResult, Duration.Inf))
}
