package pdorobisz.freemonad

import cats._
import cats.data.Writer
import cats.free.Free
import cats.implicits._

// Tracking with Writer monad
object FreeMonadExample5 extends App {

  case class User(id: Long, name: String)

  // Algebra
  sealed trait UserAlgebra[T]

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

  // Interpreter - Writer monad tracks used steps, can be used for tests
  type Log[A] = Writer[List[UserAlgebra[_]], A]
  val logInterpreter: UserAlgebra ~> Log = new (UserAlgebra ~> Log) {
    override def apply[A](fa: UserAlgebra[A]): Log[A] = fa match {
      case g@GetUser(id) => Writer(List(g), User(id, "John Doe"))
      case d@DeleteUser(u) => Writer(List(d), ())
    }
  }

  // Run program and inspect which steps were used
  val (steps, result) = program(123L).foldMap(logInterpreter).run
  println(result)
  println(steps)
}
