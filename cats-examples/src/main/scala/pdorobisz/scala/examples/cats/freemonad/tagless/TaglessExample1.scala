package pdorobisz.scala.examples.cats.freemonad.tagless

import java.util.UUID

import cats.implicits._
import cats.{Id, Monad}
import pdorobisz.scala.examples.cats.freemonad.util.Util._

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.higherKinds

/**
  * Tagless final approach doesn't requires coding additional algebraic datatypes, can be achieved using plain Scala.
  * Program is built from functions (free: from constructors)
  */
object TaglessExample1 extends App {

  // Algebra definition
  sealed trait ActionAlgebra[F[_], T] {

    def read(id: UUID): F[T]

    def write(id: UUID, value: T): F[Unit]

    def delete(id: UUID): F[Unit]
  }

  // Interpreter

  val actionAsyncInterpreter = new ActionAlgebra[Future, String] {
    private val store = mutable.Map.empty[UUID, String]

    override def read(id: UUID): Future[String] = {
      log(s"reading $id")
      Future.successful(store(id))
    }

    override def write(id: UUID, value: String): Future[Unit] = {
      log(s"writing $id -> $value")
      Future(store(id) = value)
    }

    override def delete(id: UUID): Future[Unit] = {
      log(s"delete $id")
      Future(store.remove(id))
    }
  }

  val actionInterpreter = new ActionAlgebra[Id, String] {
    private val store = mutable.Map.empty[UUID, String]

    override def read(id: UUID): Id[String] = {
      log(s"reading $id")
      store(id)
    }

    override def write(id: UUID, value: String): Id[Unit] = {
      log(s"writing $id -> $value")
      store(id) = value
    }

    override def delete(id: UUID): Id[Unit] = {
      log(s"delete $id")
      store.remove(id)
    }
  }

  // Program
  class ActionProgram[F[_] : Monad](alg: ActionAlgebra[F, String]) {
    def run(uuid: UUID, value: String) = for {
      _ <- alg.write(uuid, value)
      r <- alg.read(uuid)
      _ <- alg.delete(uuid)
    } yield r
  }

  // run
  println("running synchronously...")
  val result1 = new ActionProgram(actionAsyncInterpreter).run(UUID.randomUUID(), "hello world")
  println(Await.result(result1, 2.second))

  println("--------")
  println("running asynchronously...")
  val result2 = new ActionProgram(actionInterpreter).run(UUID.randomUUID(), "hello world")
  println(result2)

}
