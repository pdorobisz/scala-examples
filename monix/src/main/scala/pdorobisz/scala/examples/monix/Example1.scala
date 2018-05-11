package pdorobisz.scala.examples.monix

import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Await
import scala.concurrent.duration._
import monix.eval._


object Example1 extends App {

  // Tasks is lazy (nothing is executed)
  val task = Task {
    println("I'm running now!")
    1 + 1
  }

  // triggers asynchronous execution and returns future
  val future = task.runAsync
  println(Await.result(future, 5.seconds))
}
