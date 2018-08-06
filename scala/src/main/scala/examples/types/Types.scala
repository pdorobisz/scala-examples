package examples.types

object Types {
  def main(args: Array[String]) {
    // Typ parametryzowany dwoma typami..
    val pair1: Pair[Int, String] = Pair(123, "abc")
    // ...moze byc uzywany jako operator infiksowy.
    val pair2: Int Pair String = Pair(123, "abc")
    // Po zdefiniowaniu aliasu "+" wyrazenie moze wygladac tak:
    val pair3: Int + String = Pair(123, "abc")
  }

  case class Pair[A, B](a: A, b: B)

  type +[A, B] = Pair[A, B]
}
