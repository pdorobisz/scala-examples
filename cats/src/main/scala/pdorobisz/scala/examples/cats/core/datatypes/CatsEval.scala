package pdorobisz.scala.examples.cats.core.datatypes

import cats.Eval

/**
  * Monad that controls when value it holds will be evaluated. 3 strategies:
  * - Now (immediately)
  * - Later (with first call)
  * - Always (with every call)
  */
object CatsEval extends App {
  def getValue(i: Int): Int = {
    println("returning " + i)
    i
  }

  val evalNow = Eval.now(getValue(10)) // evaluation happens here
  println("--now--")
  println(evalNow.value)
  println(evalNow.value)

  val evalLater = Eval.later(getValue(20)) // evaluation is not happening here
  println("--later--")
  println(evalLater.value) // it happens here
  println(evalLater.value)

  val evalAlways = Eval.always(getValue(30)) // evaluation is not happening here
  println("--always--")
  println(evalAlways.value) // it happens here
  println(evalAlways.value) // and here too
}
