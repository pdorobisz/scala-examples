package examples.patternmatching


object PatterMatching {
  def main(args: Array[String]) {
    val country: Country = Country("Polska", "Warszawa")

    // Companion object dla case class posiada automatycznie wygenerowana metode unapply. Jest to tzw. "extractor", ktory pozwala
    // wyjac wartosci w postaci Option.
    val option: Option[(String, String)] = Country.unapply(country)
    val (name, capital) = option.get
    println(s"1. $name: $capital")

    // metoda unapply jest uzywana podczas pattern matchingu - gdy zwroci Some, to znaczy, ze dane wyrazenie pasuje.
    country match {
      case Country(name, capital) => println(s"2. $name: $capital")

      // mozna tez uzyc notacji infixowej:
      //case name Country capital => println(s"2. $name: $capital")
    }

    // Mozemy tez sami zaimplementowac metode unapply i wtedy taka klasa rowniez moze byc wykorzystywana w pattern matchingu
    // mimo, ze nie jest case class
    val person = new Person("Jan", 18)
    person match {
      case Person(name, age) => println(s"3. ${person.name} - ${person.age}")
    }

    // wykorzystanie boolean extractora
    person match {
      // "p@" - przypisanie sprawdzanej wartosci (jest przekazywana do ekstraktora jako jego argument) do zmiennej p
      case p@IsAdult() => println(s"4. $p TAK")
      case _ => println("4. NIE")
    }
  }

  /////////////////

  class Person(val name: String, val age: Int)

  // recznie stworzony companion object
  object Person {
    def unapply(p: Person): Option[(String, Int)] = Some(p.name, p.age)
  }

  // boolean extractor - zwraca tylko true lub false
  object IsAdult {
    def unapply(p: Person): Boolean = p.age >= 18
  }

  // case class - automatycznie wygenerowana metoda unapply w companion object
  case class Country(name: String, capital: String)

}
