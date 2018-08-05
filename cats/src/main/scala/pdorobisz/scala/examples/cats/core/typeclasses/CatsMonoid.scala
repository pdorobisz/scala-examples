package pdorobisz.scala.examples.cats.core.typeclasses

import cats.{Monoid, Semigroup}
import cats.data.NonEmptyList
import cats.instances.all._

/**
  * Monoid - type class extending Semigroup with zero value (empty).
  * Following law must hold:
  * combine(x, empty) = combine(empty, x) = x
  */
object CatsMonoid extends App {
  // combineAll uses empty and combine to combine all collection elements
  println(Monoid.combineAll(List(1, 2, 3, 4)))

  // when type is Semigroup only (no empty method defined) it can be wrapped with Option (Option has empty = None)
  val semigroupList = List(NonEmptyList(1, List(2, 3)), NonEmptyList(4, List(5, 6)))
  val lifted = semigroupList.map(nel => Option(nel))
  println(Monoid.combineAll(lifted))

  // or Semigroup.combineAllOption can be used (does the same thing)
  println(Semigroup.combineAllOption(semigroupList))

  // for more complex type - result: ("a" -> 4, "b" -> 2)
  println(Monoid[Map[String, Int]].combineAll(List(Map("a" → 1, "b" → 2), Map("a" → 3))))
}
