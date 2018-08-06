package examples.patternmatching

object PatternMatching2 {
  def main(args: Array[String]) {
    val p = new Person("Jan Jozef Stanislaw Kowalski")

    println("test1")
    p match {
      // zadziala tylko z osoba, ktora ma dokladnie 3 imiona
      case Person(first, second, third) => println(s"1. $first $second $third")
      // zadziala tylko z osoba, ktora ma dokladnie jedno imie
      case Person(first) => println(s"2. $first")
      // zadziala tylko z osoba, ktora ma przynajmniej 2 imiona
      case Person(first, second, _*) => println(s"3. $first $second")
    }

    println("test2")
    p match {
      // zadziala tylko z osoba, ktora ma dokladnie 3 imiona
      case Person2(last, first, second) => println(s"1. $last $first $second")
      // zadziala tylko z osoba, ktora ma przynajmniej 2 imiona
      case Person2(last, first, second, _*) => println(s"3. $last $first $second")
    }
  }

  /////////////

  class Person(val name: String)

  object Person {
    // gdy chcemy w pattern matchingu dopasowywac zmienna liczbe wartosci to nalezy zaimplementowac metode unapplySeq
    def unapplySeq(p: Person): Option[Seq[String]] = {
      if (p.name.isEmpty) {
        None
      }
      else {
        Some(p.name.split(" "))
      }
    }
  }

  object Person2 {
    // Mozna tez stworzyc matcher, ktory zwraca okreslona liczbe wartosci na poczatku, a nastepnie reszte.
    // Seq musi byc ostatnim elementem!
    // Tutaj - najpierw zwracamy ostatni element imienia, a pozniej pozostale elementy, ale juz bez ostatniego
    def unapplySeq(p: Person): Option[(String, Seq[String])] = {
      if (p.name.isEmpty) {
        None
      }
      else {
        val split: Array[String] = p.name.split(" ")
        Some((split.last, split.dropRight(1)))
      }
    }
  }

}
