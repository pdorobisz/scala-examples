package examples

/**
  * For-comprehensions korzystaja z:
  * - foreach - do iterowania po kolekcji
  * - map - zwrocenie tego co jest w yield
  * - flatMap - uzycie wartosci tworzonych prze jeden generator w nastepnym
  * - withFilter (gdy brak to filter) - warunki
  *
  * withFilter - lazy, nie tworzy posredniej kolekcji, wydajniejsze
  * filter - tworzy nowa kolekcje
  */
object ForComprehension {
  def main(args: Array[String]) {
    val seq1 = Seq(1, 2, 3)
    val seq2 = Seq("A", "B", "C")
    val seq3 = Seq("X", "Y", "Z")
    val seq4 = Seq(Seq("a", "b", "c"), Seq("d", "e"), Seq("f", "g", "h"))

    // Zeby moc iterowac po kolekcji przy pomocy "<-" typ musi implementowac metode foreach.
    // Jej implementacja znajduje sie w Iterable, ktore implementuje foreach przy pomocy iteratora (Iterable wymaga jedynie
    // zdefiniowania metody 'iterate' zwracajacej iterator).


    //// 1. For-comprehension z jednym generatorem - nalezy stosowac okragle nawiasy "(" i ")".
    val compr1a = for (x <- seq1) yield x * 10
    println("compr1a=" + compr1a)

    // to samo co:
    val compr1b = seq1 map (x => x * 10) // prostszy zapis: map (_ * 10)
    println("compr1b=" + compr1b)


    //// 2. For-comprehension z dwoma lub wiecej generatorami - nalezy stosowac nawiasy klamrowe "{" i "}".
    val compr2a = for {
      x <- seq1
      y <- seq1
    } yield x * y
    println("compr2a=" + compr2a)

    val compr2b = seq1 flatMap (x => seq1 map (y => x * y))
    println("compr2b=" + compr2b)

    val compr3a = for {
      x <- seq1
      y <- seq2
      z <- seq3
    } yield z + y + x
    println("compr3a=" + compr3a)

    val compr3b = seq1 flatMap (x => seq2 flatMap (y => seq3 map (z => z + y + x)))
    println("compr3b=" + compr3b)


    // 3. Filtrowanie
    val compr4a = for {
      x <- seq1
      if x != 2
      y <- seq2
      if y != "A"
    } yield y + x
    println("compr4a=" + compr4a)

    val compr4b = seq1 withFilter (x => x != 2) flatMap (x => seq2 withFilter (y => y != "A") map (y => y + x))
    // gdy nie ma withFilter to uzywana jest metoda filter:
    // val compr4b = seq1 filter (x => x != 2) flatMap (x => (seq2 withFilter (y => y != "A") map (y => y + x)))
    println("compr4b=" + compr4b)
    println()


    //// Gdy brak yield to mamy do czynienia z petla. Zawsze wtedy korzystamy z okraglych nawiasow, nawet gdy mamy kilka generatorow.
    for (x <- seq1 if x != 2; y <- seq2) {
      println("y+x=" + y + x)
    }
    println()

    //// For-comprehension mozna uzyc tez z klasami nie bedacymi kolekcjami, ale implementujacymi wymagane metody, np. Option:
    val option1 = Some(123)
    val option12 = for (x <- option1) yield x * 10
    println("option12=" + option12)
    println()

    //// Gdy for-comprehension zawiera pattern matching to dodatkowo stosowany jest jeszcze filtr:
    val compr5a = for (Seq(a, b, c) <- seq4) yield a + b + c
    val compr5b = seq4 withFilter { case Seq(a, b, c) => true; case _ => false } map { case Seq(a, b, c) => a + b + c }
    println("compr5a=" + compr5a)
    println("compr5b=" + compr5b)
  }
}
