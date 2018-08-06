package examples.collections

/**
 * Przyklady korzystania z kolekcji immutable.
 */
object ImmutableCollections {
  def main(args: Array[String]) {

    // podstawowe operacje dla Seq (domyslna implementacja to List)
    val seq = Seq(40, 30, 20, 10)
    println("seq = " + seq)
    println("seq(1) = " + seq(1)) // odczytanie elementu o indeksie 1
    println("seq.indices = " + seq.indices) // indeksy (0, 1, 2, 3)
    println("123 +: seq = " + (123 +: seq)) // stworzenie nowej listy z nowym elementem na poczatku
    println("seq :+ 123 = " + (seq :+ 123)) // stworzenie nowej listy z nowym elementem na koncu
    println("seq ++ Seq(123, 456) = " + (seq ++ Seq(123, 456))) // stworzenie nowej listy z druga lista dodana na koncu
    println("seq updated (1, 100) = " + (seq updated(1, 100))) // stworzenie nowej listy ze zmienionym elementem o indeksie 1
    println("seq.sorted = " + seq.sorted) // stworzenie nowej listy z posortowanymi elementami
    println("seq.reverse = " + seq.reverse) // stworzenie nowej listy z elementami w odwrotnej kolejnosci
    println()

    // podstawowe operacje dla Set
    val set = Set(1, 2, 3, 4)
    println("set = " + set)
    println("set contains 3 = " + (set contains 3)) // sprawdzenie czy zbior zawiera element 3
    println("set(3) = " + set(3)) // sprawdzenie czy zbior zawiera element 3
    println("set ++ Set(4, 5) = " + (set ++ Set(4, 5))) // stworzenie nowego zbioru zawierajacego elementy z drugiego zbioru
    println("set + 10 = " + (set + 10)) // stworzenie nowego zbioru zawierajacego dodatkowo 10
    println("set - 1 = " + (set - 1)) // stworzenie nowego zbioru bez 1
    println()

    // podstawowe operacje dla Map
    val map = Map(1 -> "one", 2 -> "two") // to samo co: Map((1, "one"), (2, "two"))
    println("map = " + map)
    println("map get 1 = " + (map get 1)) // odczytanie wartosci dla klucza jako Option
    println("map(1) = " + map(1)) // odczytanie wartosci dla klucza (rzucany wyjatek gdy nie ma takiego klucza)
    println("map contains 3 = " + (map contains 3)) // sprawdzenie czy mapa zawiera klucz
    println("map ++ Map(3 -> \"three\", 4 -> \"four\") = " + (map ++ Map(3 -> "three", 4 -> "four"))) // dodanie wartosci
    println("map + (3 -> \"three\" = " + (map + (3 -> "three"))) // dodanie (lub aktualizacja) wartosci dla klucza (mozna tez przekazac wiecej par)
    println("map updated(2, \"dwa\") = " + (map updated(2, "dwa"))) // to samo
    println("map - 2 = " + (map - 2)) // usuniecie klucza 2
    println("map.keys = " + map.keys) // klucze
    println("map.values = " + map.values) // wartosci


  }
}
