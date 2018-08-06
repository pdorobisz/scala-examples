package examples.techniques

import scala.annotation.implicitNotFound

/**
 * Type class C definiuje zachowanie (w postaci operacji) jakie musza byc dostarczone przez typ T by nalezal on do klasy C.
 * Fakt przynaleznosci do klasy nie jest podawany w definicji typu, osiaga sie go przez implementacje wszystkich operacji
 * zdefiniowanych w klasie C.
 */
object TypeClasses {

  object Math {

    // 1. Definicja type class
    // Mozna zdefiniowac wlasny komunikat ktory bedzie zawieral wyjatek rzucany w przypadku braku implicit parametru tego typu.
    @implicitNotFound("No member of type class NumberLike in scope for ${T}")
    trait NumberLike[T] {
      def plus(x: T, y: T): T

      def divide(x: T, y: Int): T
    }

    // 2. Domyslne implementacje
    object NumberLike {

      implicit object NumberLikeDouble extends NumberLike[Double] {
        def plus(x: Double, y: Double): Double = x + y

        def divide(x: Double, y: Int): Double = x / y
      }

      implicit object NumberLikeInt extends NumberLike[Int] {
        def plus(x: Int, y: Int): Int = x + y

        def divide(x: Int, y: Int): Int = x / y
      }

    }

  }

  // 3. Wykorzystanie type class
  object Statistics {

    import Math.NumberLike

    def mean[T](xs: Vector[T])(implicit ev: NumberLike[T]): T = ev.divide(xs.reduce(ev.plus), xs.size)

    // Zamiast argumentu implicit mozemy wykorzystac context bound T: NumberLike (oznacza, ze metoda bedzie miala dodatkowy
    // argument typu NumberLike[T]). Aby dostac sie do parametru implicit nalezy skorzystac z metody implicitly.
    // Context bound mozna stosowac tylko w przypadku jednego parametru implicit
    // def mean[T: NumberLike](xs: Vector[T]): T = implicitly[NumberLike[T]].divide(xs.reduce(implicitly[NumberLike[T]].plus), xs.size)

  }

  // 4. Klasa ktora rowniez ma nalezec to klasy typow NumberLike
  case class Person(age: Int)

  // zdefiniowanie przynaleznosci Person do klasy typow NumberLike
  object PersonImplicits {

    import Math.NumberLike

    implicit object NumberLikePerson extends NumberLike[Person] {
      def plus(x: Person, y: Person): Person = Person(x.age + y.age)

      def divide(x: Person, y: Int): Person = Person(x.age / y)
    }

  }

  def main(args: Array[String]) {
    // import definicji metod z klasy typow NumberLike dla klasy Person
    import PersonImplicits._

    println(Statistics.mean(Vector[Int](4, 5, 6)))
    println(Statistics.mean(Vector[Double](4, 5, 6)))
    println(Statistics.mean(Vector[Person](Person(5), Person(6), Person(10))))
  }

}
