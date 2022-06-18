package examples.classes

/**
 * Every class always has a main constructor. Additional constructors can be defined but they always have to call
 * the main one.
 */
object Constructors {

  class Person(val name: String, val lastName: String, val age: Int) {
    // whole class definition is its main constructor

    val defaultLastName = "unknow last name"

    // additional constructor always has to call main constructor
    def this(name: String) = {
      // can't refer to classe fields and methods until main constructor is executed
      // this(name, defaultLastName, 0) // won't compile
      this(name, "unknown", 0)
      println("constructor is running")
    }

    // additional constructor
    def this(name: String, lastName: String) = this(name, lastName, 0)
  }

  def main(args: Array[String]): Unit = {
    val person1 = new Person("Janek")
    val person2 = new Person("Janek", "Kowalski")

    printPerson(person1)
    printPerson(person2)
  }

  def printPerson(p: Person): Unit = println(s"person1: ${p.name} ${p.lastName}, ${p.age}")
}
