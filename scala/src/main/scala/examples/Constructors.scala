package examples

/**
 * W Scali kazda klasa ma zawsze jeden podstawowy konstruktor. Pozostale moga byc zdefiniowane, ale musza korzystac
 * z podstawowego.
 */
object Constructors {

  class Person(val name: String, val lastName: String, val age: Int) {
    // cala definicja klasy jest jej podstawowym konstruktorem:

    val defaultLastName = "unknow last name"

    // dodatkowy konstruktor zawsze musi wywolac podstawowy konstruktor
    def this(name: String) = {
      // dopoki podstawowy konstruktor nie zakonczy dzialania nie mozna sie odwolywac do metod i pol zdefiniowanych w klasie
      // this(name, defaultLastName, 0) // nie skompiluje sie
      this(name, "unknown", 0)
      println("constructor is running")
    }

    // dodatkowy konstruktor
    def this(name: String, lastName: String) = this(name, lastName, 0)
  }

  def main(args: Array[String]) {
    val person1 = new Person("Janek")
    val person2 = new Person("Janek", "Kowalski")

    printPerson(person1)
    printPerson(person2)
  }

  def printPerson(p: Person) = println(s"person1: ${p.name} ${p.lastName}, ${p.age}")
}
