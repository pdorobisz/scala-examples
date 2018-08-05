package pdorobisz.scala.examples.cats.core.typeclasses

import cats.Semigroup
import cats.implicits._

/**
  * Semigroup - type class with combine (|+|) operator.
  * They have to be associative:
  * combine(x, combine(y, z)) = combine(combine(x, y), z)
  */
object CatsSemigroup extends App {
  // no need to define semigroup for basic types as they're already defined in cats.implicits._
  //
  //  implicit val intSemigroup: Semigroup[Int] = new Semigroup[Int] {
  //    def combine(x: Int, y: Int): Int = x + y
  //  }

  val x = 1
  val y = 2

  println(Semigroup[Int].combine(x, y))
  // combine
  println(x |+| y)

  // more complex example - result: "foo" -> ("bar" -> 11)
  val map1 = Map("foo" → Map("bar" → 5))
  val map2 = Map("foo" → Map("bar" → 6))
  val combinedMap = Semigroup[Map[String, Map[String, Int]]].combine(map1, map2)
  println(combinedMap)


  /////// custom type
  case class Box[T](value: T)

  implicit def boxSemigroup[T: Semigroup] = new Semigroup[Box[T]] {
    override def combine(x: Box[T], y: Box[T]): Box[T] = Box(Semigroup[T].combine(x.value, y.value))
  }

  val b1 = Box(10)
  val b2 = Box(20)
  println(b1 |+| b2)

  /////// associativity
  // foldLeft and foldRight give same result
  println(List(1, 2, 3).foldLeft(0)(_ |+| _) == List(1, 2, 3).foldRight(0)(_ |+| _))

  val (left, right) = List(1, 2, 3, 4, 5).splitAt(2)
  val sumLeft = left.foldLeft(0)(_ |+| _)
  val sumRight = right.foldLeft(0)(_ |+| _)
  println(sumLeft |+| sumRight)

  // it's not possible to implement generic method for sum (for every type) as zero element is missing (Monoid provides it)
}
