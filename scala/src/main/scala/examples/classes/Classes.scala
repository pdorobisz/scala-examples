package examples.classes

object Classes {
  def main(args: Array[String]): Unit = {
    val p = new Person

    p.age_=(12) // setter, same: p.age = 12
    val age = p.age // getter

    p.name = "John"
    println("p.name: " + p.name)

    val c = new p.Car
    p.driveACar(c)

    // can't create object because main construction is private
    // new PrivateConstructor("Unknown")

    val outer = new Outer("outer object")
    new outer.Inner("inner object").identify()

    val b: B = new B
    println("b.getProperty: " + b.getProperty)
    println("b.method: " + b.method)
  }

  class Person {
    // property: under the hood `age` field is private and public getter and setter are automatically generated for it
    var age = 0

    // only setter is generated
    val create = new java.util.Date

    // we can also "manually" define "name" property
    private var privateName = ""

    def name_=(n: String): Unit = {
      println(s"set name: $n")
      privateName = n
    }

    def name: String = {
      println(s"get name: $privateName")
      privateName
    }

    // object-private property - can't access this field outside the object itself, getters and setters aren't generated for fields like this
    private[this] def address = ""

    private def talkTo(p: Person): Unit = {
      println(s"hello ${p.privateName}") // can access class-private property of same class object
      // p.address // but can't access object-private property of a different instance of same class
    }

    def driveACar(c: Car): Unit = {
      c.drive()
    }

    class Car {
      // Private property (getters and setters are generated for it), only `Perons` instances have access to it.
      // You can only specify outer class that contains this class. You can also use containing package name or
      // parent package name - all classes from specified package will have access to this property.
      private[Person] def drive(): Unit = println("vroom")
    }
  }

  // private main constructor
  class PrivateConstructor private(val name: String)

  class Outer(private val description: String) {

    // self-type annotation (usually named "self") - alias for Outer.this
    self =>

    class Inner(private val description: String) {
      def identify(): Unit = {
        // this refers to inner class
        val desc = this.description
        // we can refer to outer instance via self-type annotation...
        val outerDesc = self.description
        // ... or by this:
        // val outerDesc = Outer.this.description
        println(s"$desc in $outerDesc")
      }
    }

  }

  abstract class A {
    val property = "property - A"

    def getProperty: String = property

    def method = "method - A"

    def method2 = "method2 - A"

    var v: String
  }

  class B extends A {
    // val can redefine val...
    override val property = "property - B"

    // ...or def (but only when no parameters).
    override val method = "method - B"

    // def can only redefine def
    override def method2 = "method2 - B"

    // var can only redefine abstract var ('override' not required when redefining abstract fields nad methods)
    var v = ""
  }
}
