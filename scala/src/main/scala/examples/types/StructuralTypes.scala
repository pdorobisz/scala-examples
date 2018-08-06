package examples.types

/**
 * Structural Typing jest podobny do Duck Typing, ale wystepuje w jezykach statycznie
 * typowanych, gdyz sprawdzenie zgodnosci typow odbywa sie w czasie kompilacji, a nie
 * w czasie dzialania (jak Duck Typing w jezykach dynamicznie typowanych).
 * Wywolywanie metod zdefiniowanych przy pomocy Structural Typing nie jest wydajne, gdyz
 * korzystac z refleksji. Mimo iz kompilator jest w stanie stwierdzic w czasie kompilacji,
 * ze typy sie zgadzaja, to technicznie nie ma takiej mozliwosci by w runtimie
 * wywolac metode na obiekcie nieznanego typu (invokedynamic moze tu kiedys pomoc).
 */
object StructuralTypes {
  def main(args: Array[String]) {
    val mc = new MyClass
    // przekazanie instancji klasy posiadajacej wymagane metody
    println(method(3, mc))

    // mozna bez definiowania klasy (definiujemy anonimowa klase):
    val mc2: {def foo: String; def bar(x: Int): Int} = new AnyRef {
      def foo = "hello2"

      def bar(x: Int) = x * 10
    }
    println(method(4, mc2))
  }

  // Zamiast podac konretny typ podajemy tylko metody jakie ma implementowac.
  def method(i: Int, x: {def foo: String; def bar(x: Int): Int}): String = {
    "method1 " + x.foo + ", " + x.bar(i)
  }

  class MyClass {
    def foo = "hello"

    def bar(x: Int) = x * x
  }

}
