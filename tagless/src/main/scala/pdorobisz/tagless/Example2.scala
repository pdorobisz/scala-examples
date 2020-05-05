package pdorobisz.tagless

import cats.implicits._
import cats.{Id, Monad}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

// Same as Example 1 but making it more type-classy
object Example2 extends App {

  case class User(id: Long, name: String)

  // Algebra definition
  trait UserAlgebra[F[_]] {
    def getUser(id: Long): F[User]

    def deleteUser(u: User): F[Unit]
  }

  // Helper functions
  object UserAlgebra {
    def apply[F[_]](implicit Alg: UserAlgebra[F]): UserAlgebra[F] = Alg
  }

  // Interpreters - type class implementations
  implicit val userAlgebraAsyncInterpreter: UserAlgebra[Future] = new UserAlgebra[Future] {
    override def getUser(id: Long): Future[User] = Future(User(id, "John Doe"))

    override def deleteUser(u: User): Future[Unit] = Future(println(s"deleting: $u"))
  }

  implicit val userAlgebraSyncInterpreter: UserAlgebra[Id] = new UserAlgebra[Id] {
    override def getUser(id: Long): Id[User] = User(id, "John Doe")

    override def deleteUser(u: User): Id[Unit] = println(s"deleting: $u")
  }

  // Program
  def program[F[_] : UserAlgebra : Monad](id: Long): F[User] = for {
    u <- UserAlgebra[F].getUser(id)
    _ <- UserAlgebra[F].deleteUser(u)
  } yield u

  // Now we can run different versions of the program
  val asyncResult = program[Future](123L)
  println(Await.result(asyncResult, Duration.Inf))

  val syncResult = program[Id](123L)
  println(syncResult)
}
