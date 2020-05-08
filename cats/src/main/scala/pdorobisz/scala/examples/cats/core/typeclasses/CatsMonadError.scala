package pdorobisz.scala.examples.cats.core.typeclasses

import cats.MonadError
import cats.implicits._

import scala.util.Try

/**
 * MonadError is a type class that abstracts over error-handling monads. Allows you to raise and handle an error value.
 */
object CatsMonadError extends App {

  // Instead of explicitly wrapping success/error case into some value (e.g. Either's Right/Left) we can make it generic
  // with MonadError
  def strToInt[F[_]](s: String)(implicit M: MonadError[F, Throwable]): F[Int] = s.toIntOption match {
    case Some(i) => M.pure(i)
    case None => M.raiseError(new Exception(s"unable to convert '$s' to int"))
  }

  val tryResult: Try[Int] = strToInt[Try]("abc")
  println(tryResult)

  val eitherResult = strToInt[Either[Throwable, *]]("abc")
  println(eitherResult)

  // Another example - we operate on some genereric F monad rather than committing to concrete type
  def sum[F[_]](s1: String, s2: String)(implicit M: MonadError[F, Throwable]): F[Int] = for {
    a <- strToInt[F](s1)
    b <- strToInt[F](s2)
  } yield a + b

  val sumResult = sum[Try]("2", "3b")
  println(sumResult)
}
