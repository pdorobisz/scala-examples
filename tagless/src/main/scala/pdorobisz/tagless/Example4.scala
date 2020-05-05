package pdorobisz.tagless

import cats.Monad
import cats.data.{EitherT, OptionT}
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

// Returning results as Option, Either etc.
object Example4 extends App {

  case class User(id: Long, name: String)

  // Algebra definition - methods are returning response wrapped directly within F, no additional effects (e.g. Option) defined here
  trait UserAlgebra[F[_]] {
    // it just returns F[User], not F[Option[User]]
    def getUser(id: Long): F[User]

    def deleteUser(u: User): F[Unit]
  }

  // Helper functions
  object UserAlgebra {
    def apply[F[_]](implicit Alg: UserAlgebra[F]): UserAlgebra[F] = Alg
  }

  // Interpreters - here we can specify effects
  type FutureEither[A] = EitherT[Future, String, A]
  implicit val userAlgebraEitherTInterpreter: UserAlgebra[FutureEither] = new UserAlgebra[FutureEither] {
    override def getUser(id: Long): FutureEither[User] = EitherT.pure[Future, String](User(id, "John Doe"))

    override def deleteUser(u: User): FutureEither[Unit] = EitherT.fromEither[Future](Left(s"Failed to delete user ${u.id}"))
  }

  type FutureOption[A] = OptionT[Future, A]
  implicit val userAlgebraOptionTInterpreter: UserAlgebra[FutureOption] = new UserAlgebra[FutureOption] {
    override def getUser(id: Long): FutureOption[User] = OptionT.pure[Future](User(id, "John Doe"))

    override def deleteUser(u: User): FutureOption[Unit] = OptionT.none[Future, Unit]
  }

  // Program that returns F[User]
  def program[F[_] : UserAlgebra : Monad](id: Long): F[User] = for {
    u <- UserAlgebra[F].getUser(id)
    _ <- UserAlgebra[F].deleteUser(u)
  } yield u

  // Now we can specify different interpreters to get different effects (e.g. Option, Either)
  val eitherResult: Future[Either[String, User]] = program[FutureEither](123L).value
  println(Await.result(eitherResult, Duration.Inf))

  val optionResult = program[FutureOption](123L).value
  println(Await.result(optionResult, Duration.Inf))
}
