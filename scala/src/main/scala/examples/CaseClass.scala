package examples

object CaseClass {

  case class Person(name: String, lastName: String, age: Int)

  def main(args: Array[String]): Unit = {
    patternMatchingExample()
  }

  private def patternMatchingExample(): Unit = {
    println("\n---pattern matching example---")
    val person = Person("John", "Smith", 35)
    val Person(n, _, a) = person // we can extract only some fields and omit others
    println(s"$n: $a")
  }

  private def inheritanceExample() = {
    // case-to-case inheritance is not supported
    //    println("\n---inheritance example---")
    //    case class Employee(override val name: String, override val lastName: String, override val age: Int, position: String) extends Person(name, lastName, age)
    //
    //    val e1 = Employee("John", "Smith", 35, "teacher")
    //    val e2 = Employee("John", "Smith", 35, "assistant")
    //
    //    println(e1)
    //    println(e2)
  }
}
