package examples

/**
 * Implicit conversions umozliwiaja automatyczna konwersje jednego typu na drugi tak by dane wyrazenie bylo prawdziwe.
 * W przypadku E.x (E - wyrazenie typu T, x - pole/metoda nienalezace do T) kompilator automatycznie wstawi konwersje I:
 * I(E).x tak by takie wyrazenie bylo prawdziwe (I - konwersja typu T na typ posiadajacy pole/metode x).
 */
object ImplicitConversion {
  // Metoda realizujaca konwersje moze miec dowolna nazwe. W przypadku wiecej niz jednej metody realizujacej konwersje
  // konwersja nie zostanie wstawiona.
  implicit def intToString(d: Int) = new String("conversion of " + d)

  def main(args: Array[String]) {
    // Proba wywolania metody klasy String - kompilator wstawi wczesniej zdefiniowana konwersje by zamienic Int na String.
    println(67.toUpperCase)

    // W przypadku wywolania metody przyjmujacej parametry innych typow niz rzeczywiste typy argumentow rowniez zostanie
    // wstawiona konwersja.
    println(concat(12, 34))

    // Metoda double nalezy do ImprovedString (implicit class) wiec kompilator automatycznie wstawi kod tworzacy jej instancje.
    println("hello".double)
  }

  def concat(s1: String, s2: String) = s1 + "::" + s2

  /**
   * Implicit class - jej glowny konstruktor staje sie metoda wykonujaca konwersje.
   * Ograniczenia:
   * - moze byc zdefiniowana tylko wewnatrz trait/class/object
   * - moze przyjmowac doklanie jeden argument nie bedacy implicit
   * - nie moze istniec w scopie inna metoda/pole/obiekt o tej samej nazwie (czyli case class nie moze byc implicit class
   * gdyz dla niej automatycznie jest tworzony companion object o takiej samej nazwie)
   */
  implicit class ImprovedString(s: String) {
    println("creating ImprovedString for: " + s)

    def double = s + s
  }

}
