package examples.functions

import scala.runtime.NonLocalReturnControl

object LocalReturn {
  def main(args: Array[String]) {

    try {
      val list = List(1, 2, 3, 4, 5, 6).map(x => {
        if (x > 5) return // return z anonimowej funkcji powoduje rzucenie wyjatku NonLocalReturnControl
        x + 10
      })
      println("ok: " + list)
    } catch {
      case e: NonLocalReturnControl[Int] => e.printStackTrace()
    }

    try {
      val list = List(3, 4, 5).map(x => {
        (1 to x).map(y => {
          if (x > 4) return
          y + 10
        }).toList
      })
      println("ok: " + list)
    } catch {
      case e: NonLocalReturnControl[Int] => e.printStackTrace()
    }
  }
}
