package examples.techniques

/**
 * Technika umozliwiajaca "dodawanie" metod do istniejacych klasy (nie jest to tak naprawde dodawanie, a konwersja do
 * klasy posiadajacej taka metode).
 */
object PimpMyLibrary {

  // klasa zawierajaca dodawana metode
  class QuoteString(s: String) {
    def quote: String = s"'$s'"
  }

  // metoda zamieniajaca String na klase posiadajaca dodawana metode
  implicit def toQuoteString(s: String) = new QuoteString(s)

  /**
   * Zamiast definiowana metody wykonujacej konwersje mozna zdefiniowac klase jako implicit:
   * implicit class QuoteString(s: String) { ... }
   * Wtedy konstruktor bedzie metoda wykonujaca konwersje. Ma to pewne ograniczenia (szczegoly w [[examples.Implicits.implicitConversionDemo()]]).
   */

  def main(args: Array[String]) {
    val s = "Ala ma kota"
    println(s.quote)
  }

}
