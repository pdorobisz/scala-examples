package examples.functions

object ByNameVsByValue {

  // Wartosc argumentu jest ewaluowana za kazdym razem gdy wewnatrz metody wystepuje odwolanie do argumentu.
  def byName(a: => Unit) = {
    for (i <- 0 until 5) {
      print(a) // wywola obliczenie wartosci argumentu a
      print(a) // jeszcze raz wywola obliczenie a
    }
    println()
  }

  // Przekazywanie przez wartosc jest domyslnym mechanizmem w Scali i Javie.
  // Przed wywolaniem metody jest obliczana wartosc argumentu i tak obliczona wartosc jest uzywana wewnatrz metody.
  def byValue(a: Unit) = {
    for (i <- 0 until 5) {
      print(a)
      print(a)
    }
    println()
  }

  // Argument przekazywany przez nazwe staje sie metoda. Ta metode mozna zamienic na funkcje:
  def byName2(a: => String): Function0[String] = a _

  def main(args: Array[String]) {
    var i = 1

    println("----by value----")
    byValue(i = i + 1)
    println(i) // wypisze 2 bo inkrementacja zostanie wywolana tylko raz, przed wywolaniem metody

    i = 1
    println("----by name----")
    byName(i = i + 1)
    println(i) // wypisze 11 bo i zostanie 10 razy inkrementowane

    //
    println("----by name to function----")
    // Blok przekazany przez nazwe zamieniamy na funkcje.
    val f: () => String = byName2({ println("evaluating"); "abc"})
    // Przy kazdym wywolaniu tej funkcji zostanie wypisane "evaluating" (blok jest ewaluowany za kazdym razem).
    println(f())
    println(f())
  }
}
