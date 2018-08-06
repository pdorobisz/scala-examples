package examples.types

/**
 * Powiazanie instancji klasy wewnetrznej z konkretna instancja klasy zewnetrznej.
 */
object PathDependentTypes {
  def main(args: Array[String]) {
    val poland = new Country("Poland")
    val uk = new Country("United Kingdom")

    // Stworzenie instancji klasy Person powiazanych z konkretnymi instancjami klasy Country.
    // "poland.Person" reprezentuje sciezke do typu Person, stad nazwa "path-dependent types"
    val tusk = new poland.Person("Donald Tusk")
    val cameron = new uk.Person("David Cameron")

    // ok
    setPrimeMinister(poland)(tusk)
    uk.primeMinister = Some(cameron)

    // Nie skompiluje sie gdyz nie mozna uzyc wewnetrzengo typu (Person) poza instancja zewnetrznego typu
    // do ktorego jest ona przypisana.
    // uk.primeMinister jest typu uk.Person, a probujemy przypisac obiekt klasy poland.Person
    // uk.primeMinister = Some(tusk)

    // Tak tez nie zadziala:
    // setPrimeMinister(poland)(cameron)

    // ok
    poland.friend = Some(cameron)
  }

  class Country(val name: String) {

    case class Person(name: String)

    var primeMinister: Option[Person] = None

    // Type Projection - gdy chcemy uzyc instancje wewnetrznej klasy z dowolnej instancji klasy zewnetrznej
    var friend: Option[Country#Person] = None
  }

  def setPrimeMinister(c: Country)(pm: c.Person) = c.primeMinister = Some(pm)
}
