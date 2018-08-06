package examples

object Classes {
  def main(args: Array[String]) {
    val p = new Person

    p.age_=(12) // setter, to samo co: p.age = 12
    val age = p.age // getter

    p.name = "Janek"
    println("p.name: " + p.name)

    val c = new p.Car
    p.driveACar(c)

    // nie mozna stworzyc obiektu bo glowny konstruktor jest prywatny
    // new PrivateConstructor("Unknown")

    val outer = new Outer("outer object")
    new outer.Inner("inner object").identify()

    val b: B = new B
    println("b.getProperty: " + b.getProperty)
    println("b.method: " + b.method)
  }

  class Person {
    // stworzenie property, tak naprawde pole age jest prywatne i sa do niego generowane publiczne gettery i settery
    var age = 0

    // generowany jest tylko getter
    val create = new java.util.Date

    // "reczna" definicja property
    private var privateName = ""

    def name_=(n: String) {
      println(s"set name: $n")
      privateName = n
    }

    def name = {
      println(s"get name: $privateName")
      privateName
    }

    // object-private property - nie mozna dostac sie do tego pola poza tym obiektem, dla pol tego typu nie powstaja gettery ani settery
    private[this] def address = ""

    private def talkTo(p: Person) = {
      println(s"hello ${p.privateName}") // mozna dostac sie do class-private property obiektu tej samej klasy
      // p.address // nie ma dostepu do property object-private z innego obiektu
    }

    def driveACar(c: Car) = {
      c.drive()
    }

    class Car {
      // prywatne property (sa dla niego generowane gettery i settery) do ktorego dostep maja tylko obiekty klasy Person
      // Nie mozna podac dowolnej klasy, mozna tylko podac klase zawierajaca ta klase.
      // Mozna tez podac nazwe pakietu w ktorym znajduje sie klasa lub tez nazwe nadrzednego pakietu - wtedy klasy
      // z podanego pakietu beda mialy dostep.
      private[Person] def drive() = println("brrrumm")
    }

  }

  // prywatny glowny konstruktor
  class PrivateConstructor private(val name: String)

  class Outer(private val description: String) {

    // self-type annotation (zazwyczaj nazywa sie "self") - alias dla Outer.this
    self =>

    class Inner(private val description: String) {
      def identify() {
        // this odnosi sie do obiektu wewnetrznej klasy
        val desc = this.description
        // mozna sie odniesc do instancji zewnetrznej klasy przy pomocy self-type annotation...
        val outerDesc = self.description
        // ... lub poprzez this:
        // val outerDesc = Outer.this.description
        println(s"$desc in $outerDesc")
      }
    }

  }

  abstract class A {
    val property = "property - A"

    def getProperty = property

    def method = "method - A"

    def method2 = "method2 - A"

    var v: String
  }

  class B extends A {
    // val moze przedefiniowac val...
    override val property = "property - B"

    // ...lub def (ale bez parametrow!).
    override val method = "method - B"

    // def moze tylko przedefiniowac def
    override def method2 = "method2 - B"

    // var moze przedefiniowac tylko abstrakcyjne var ('override' nie jest wymagane przy przedefiniowywaniu abstrakcyjnych pol i metod)
    var v = ""
  }

}
