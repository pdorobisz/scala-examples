package examples

object CaseClass {

  case class Person(name: String, lastName: String, age: Int)

  def main(args: Array[String]) {
    val person = Person("Janek", "Kowalski", 15)

    val Person(n, _, a) = person // przypisanie wartosci z case klasy (pominiecie drugiego argumentu)

    println( s"$n: $a")
  }
}
