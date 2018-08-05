package pdorobisz.scala.examples.cats.core.typeclasses

import cats._
import cats.implicits._

/**
  * Functor = type class that defines map for types with single "hole" (F[?])
  */
object CatFunctor extends App {

  val list: List[String] = List("aa", "bbb", "cccc")
  val f: String => Int = s => s.length
  val mapped = Functor[List].map(list)(f)
  println(mapped)

  // other methods

  // lift converts function A => B to F[A] => F[B]
  val lifted: (List[String]) => List[Int] = Functor[List].lift(f)

  // fproduct applies function f and returns tuple (value, f(value))
  val product: List[(String, Int)] = Functor[List].fproduct(list)(f)
  println(product)

  // compose combines functors into new functor
  val listOpt = Functor[List] compose Functor[Option]
  val listOptResult = listOpt.map(List(Some("aa"), None, Some("bbb")))(f)
  print(listOptResult)
}
