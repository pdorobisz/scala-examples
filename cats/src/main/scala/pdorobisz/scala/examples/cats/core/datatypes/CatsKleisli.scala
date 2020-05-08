package pdorobisz.scala.examples.cats.core.datatypes

import cats.data.Kleisli
import cats.implicits._

/**
 * Kleisli is a wrapper around A => F[B] function (F - some context, monad).
 * It enables compositions of such functions.
 */
object CatsKleisli extends App {

  // Functions returning wrapper
  val stringToInt: String => Option[Int] = s => s.toIntOption
  val inc: Int => Option[Int] = i => Some(i + 1)
  val nonZero: Int => Option[Boolean] = i => Some(i != 0)

  // Kleislis:
  val stringToIntK: Kleisli[Option, String, Int] = Kleisli(stringToInt)
  val incK: Kleisli[Option, Int, Int] = Kleisli(inc)
  val nonZeroK: Kleisli[Option, Int, Boolean] = Kleisli(nonZero)

  // composed functions:
  val composed: Kleisli[Option, String, Boolean] = stringToIntK andThen incK andThen nonZeroK

  val result: Option[Boolean] = composed("1")
  println(result)
}
