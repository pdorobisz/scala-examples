package pdorobisz.tagless

import cats.Monad
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
 * Tagless final approach doesn't requires coding additional algebraic datatypes, can be achieved using plain Scala.
 */
object Example1 extends App {

  case class User(id: Long, name: String)

  // Algebra definition (our DSL). UserAlgebra is a higher-kinded type as it accepts type constructor F[_], F - effect wrapper
  trait UserAlgebra[F[_]] {
    def getUser(id: Long): F[User]

    def deleteUser(u: User): F[Unit]
  }

  // Interpreter - algebra implementation
  val userAlgebraAsyncInterpreter = new UserAlgebra[Future] {
    override def getUser(id: Long): Future[User] = Future(User(id, "John Doe"))

    override def deleteUser(u: User): Future[Unit] = Future(println(s"deleting: $u"))
  }

  // Program, F has to be a monad to be able to use it in for-comprehension
  class Program[F[_] : Monad](userAlgebra: UserAlgebra[F]) {
    def run(id: Long): F[User] = for {
      u <- userAlgebra.getUser(id)
      _ <- userAlgebra.deleteUser(u)
    } yield u
  }

  // Now we can run our program
  val result = new Program(userAlgebraAsyncInterpreter).run(123L)
  println(Await.result(result, Duration.Inf))
}
