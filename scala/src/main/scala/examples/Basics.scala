package examples

object Basics {
  def main(args: Array[String]) {
    // uzywanie operatorow to wywolanie metody; 2.1 * 4.3 mozna zapisac jako:
    println(2.1.*(4.3))

    // Jednak nie jest to do konca prawda gdyz Scala w pewnym zakresie "rozumie" operatory. W przypadku, gdy wyrazenie jest zapisane
    // w postaci "operator syntax" (spacje zamiast kropek), a nazwy metod zaczynaja sie od symboli operatorow to bedzie respektowane
    // pierwszenstwo operatorow. Np. ponizsze wyrazenie zwraca 14, a nie 20 gdyby traktowac operatory jak zwykle metody.
    println(2 + 3 * 4)

    // operatory konczace sie na ":" sa metodami prawego operanda, prawostronne wiazanie;
    // 1 :: List(2, 3) mozna zapisac jako:
    println(List(2, 3).::(1))

    // gdy obiekt posiada metode apply to mozna jej uzyc w nastepujacy sposob:
    println(Hello("Janek"))

    val nothing1: Unit = nothing()
    println(nothing1)

    // symbole - zawsze istnieje tylko jedna instancja (sa internowane)
    println('hi eq Symbol("hi"))
  }

  object Hello {
    def apply(name: String) = "hello " + name
  }

  // zwraca Unit gdyz nie ma "="
  def nothing() {
    "hello"
  }
}
