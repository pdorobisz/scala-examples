package pdorobisz.scala.examples.cats.core.typeclasses

import cats._
import cats.implicits._

/**
  * Type class extending Functor with "ap". "ap" is similar to "map", but takes F[A => B] as an argument.
  */
object CatsApply extends App {
  val add2: Int ⇒ Int = _ + 2
  val multiply10: Int ⇒ Int = _ * 10

  println(Apply[Option].ap(Some(add2))(Some(1)))
  println(Apply[List].ap(List(add2))(List(1, 2, 3)))

  // It's possible to pass list of functions. They will be applied in the same order and new list will be created. Result: List(3, 4, 5, 10, 20, 30)
  println(Apply[List].ap(List(add2, multiply10))(List(1, 2, 3)))

  // None if function not provided
  println(Apply[Option].ap(None)(Some(1)))

  // There're versions of "ap" that take more than one argument (ap2, ap3...ap22). Similar for map (map2, map3...)
  val addArity2 = (a: Int, b: Int) ⇒ a + b
  println(Apply[Option].ap2(Some(addArity2))(Some(1), Some(2)))

  // Function called for all possible pairs, result: List(11, 12, 21, 22)
  println(Apply[List].ap2(List(addArity2))(List(10, 20), List(1, 2)))

  // operator |@| enables using "apWith" instead of "apN" for function of N arguments
  println((Option(1) |@| Option(2)).apWith(Some(addArity2)))

  // "map" can be used instead of "mapN"
  println((Option(1) |@| Option(2)).map(addArity2))
}
