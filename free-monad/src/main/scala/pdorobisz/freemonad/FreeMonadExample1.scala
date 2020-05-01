package pdorobisz.freemonad

import java.util.UUID

import cats._
import cats.free.Free
import cats.implicits._
import Util._

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
 * Free monad is something we can wrap around an arbitrary type constructor (F[_]) to construct a monad.
 * It allows us to separate the structure of the computation (program) from its interpreter allowing different
 * interpretations if needed.
 * Program is data, built from ADT. It's a recursive data structure which can grow very big but is stack safe.
 */
object FreeMonadExample1 extends App {

  // Algebra definition

  sealed trait ActionAlgebra[T]

  case class Read[T](id: UUID) extends ActionAlgebra[T]

  case class Write[T](id: UUID, value: T) extends ActionAlgebra[T]

  case class Delete(id: UUID) extends ActionAlgebra[Unit]

  // Free type (not necessary, but useful)

  type ActionF[A] = Free[ActionAlgebra, A]

  // DSL (to map our classes to Free)

  def read[T](id: UUID): ActionF[T] = Free.liftF(Read(id))

  def write[T](id: UUID, value: T): ActionF[T] = Free.liftF(Write(id, value))

  def delete(id: UUID): ActionF[Unit] = Free.liftF(Delete(id))

  // Interpreters

  // "~>" - natural transformation (FunctionK[F,G]) - transforms container type F[_] to G[_].
  // F (ActionAlgebra) is the representation inside free monad, G (Id) is a monad inside which we really run the computation.
  val seqInterpreter: ActionAlgebra ~> Id = new (ActionAlgebra ~> Id) {

    private val store = mutable.Map.empty[UUID, Any]

    override def apply[A](fa: ActionAlgebra[A]): Id[A] = fa match {
      case Read(id) =>
        log(s"read $id")
        store(id).asInstanceOf[A]
      case Write(id, value) =>
        log(s"write $id -> $value")
        store(id) = value
        ().asInstanceOf[A]
      case Delete(id) =>
        log(s"delete $id")
        store.remove(id)
        ().asInstanceOf[A]
    }
  }

  val asyncInterpreter: ActionAlgebra ~> Future = new (ActionAlgebra ~> Future) {

    private val store = mutable.Map.empty[UUID, Any]

    override def apply[A](fa: ActionAlgebra[A]): Future[A] = fa match {
      case Read(id) =>
        log(s"read $id")
        Future(store(id).asInstanceOf[A])
      case Write(id, value) =>
        log(s"write $id -> $value")
        store(id) = value
        Future(().asInstanceOf[A])
      case Delete(id) =>
        log(s"delete $id")
        store.remove(id)
        Future(().asInstanceOf[A])
    }
  }

  // Program - operations provided by our algebra are wrapped with Free monad so we can use for-comprehension (flatMap)

  def program[T](uuid: UUID, value: T): ActionF[T] = for {
    _ <- write[T](uuid, value)
    r <- read[T](uuid)
    _ <- delete(uuid)
  } yield r

  // Now we can run same program with different interpreters
  private val prog = program(UUID.randomUUID(), "hello world")

  println("running synchronously...")
  val result1 = prog.foldMap(seqInterpreter)
  println(result1)

  println("--------")
  println("running asynchronously...")
  val result2 = prog.foldMap(asyncInterpreter)
  println(Await.result(result2, 2.second))
}
