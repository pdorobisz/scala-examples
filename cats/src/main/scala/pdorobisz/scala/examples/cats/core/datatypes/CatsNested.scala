package pdorobisz.scala.examples.cats.core.datatypes

import cats.data.{EitherT, Nested}
import cats.implicits._

/**
 * Helps with `F[G[_]]`. types
 */
object CatsNested extends App {

  val optionList: Option[List[Int]] = Some(List(1, 2, 3, 4, 5))
  val n = Nested(optionList)
  val optionList2: Option[List[String]] = n.map(x => s"[$x]").value
  println(optionList2)

  // it also works with Monad transformers:
  val eitherOption: Either[String, Option[Int]] = Right(Some(123))
  val eitherOptionT: EitherT[Option, String, Option[Int]] = EitherT(Some(eitherOption))
  val eitherOptionT2: EitherT[Option, String, Option[Int]] = Nested(eitherOptionT).map(_ * 10).value
  println(eitherOptionT2)
}
