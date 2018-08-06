package examples.types

/**
 * Przy pomocy Generalized Type Constraints mozna dodac do poszczegolnych metod dodatkowe ograniczenia dotyczace typu
 * argumentu.
 * Dostepne mozliwosci:
 * A =:= B - A i B musze byc tym samym typem
 * A <:< B - A musi byc podtypem B
 * Nie sa to operatory, ale klasy zdefiniowane w Predef. Notacja A =:= B wykorzystuje mozliwosc zapisania klasy
 * parametryzowanej dwoma typami w postaci infiksowej ( A =:= B oznacza =:=[A, B]).
 */
object GeneralizedTypeConstraints {

  def main(args: Array[String]) {
    // new MyClass[A].shouldConformTo("") // blad, A nie jest podtypem B
    new MyClass[B].shouldConformTo("")
    new MyClass[C].shouldConformTo("")

    // new MyClass[A].shouldMatch("") // blad, A nie jest rowne B
    new MyClass[B].shouldMatch("")
    // new MyClass[C].shouldMatch("") // blad, C nie jest rowne B
  }


  // Nie ma ograniczen dla T, ale mozna je dodac do poszczegolnych metod.
  class MyClass[T] {
    // nie da sie wywolac tej metody gdy T nie jest podtypem B
    def shouldConformTo(x: String)(implicit evidence: T <:< B) = ()

    // nie da sie wywolac tej metody gdy T nie jest rowne B
    def shouldMatch(x: String)(implicit evidence: T =:= B) = ()
  }

  class A

  class B extends A

  class C extends B

}
