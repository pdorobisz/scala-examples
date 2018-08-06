package examples.collections

import scala.collection.mutable

/**
 * Przyklady korzystania z kolekcji mutable.
 */
object MutableCollections {
  def main(args: Array[String]) {
    // Seq (domyslna implementacja to mutable.ArrayBuffer) - wstawianie
    val seq: mutable.ArrayBuffer[Int] = mutable.ArrayBuffer(40, 30, 20, 10)
    println("seq = " + seq)
    seq(0) = 400 // zmiana wartoscu elementu o indeksie 0
    println("seq = " + seq)
    seq += 99 // dodanie elementu na koniec
    println("seq = " + seq)
    seq +=(100, 200, 300) // dodanie kilku elementow
    println("seq = " + seq)
    seq ++= Seq(400, 500) // dodanie elementow z Seq
    println("seq = " + seq)
    0 +=: seq // dodanie elementu na poczatek
    println("seq = " + seq)
    Seq(-1, -2) ++=: seq // dodanie elementow z Seq na poczatku
    println("seq = " + seq)
    seq insert(1, 1000) // wstawienie elementu na pozycje 1 (dotychczasowy element 1 staje sie elementem 2)
    println("seq = " + seq)
    seq insertAll(2, Seq(1000, 1000)) // wstawienie elementow na pozycje 2
    println("seq = " + seq)

    // Seq - usuwanie
    println()
    seq -= 1000 // usuniecie elementu (zostanie usuniete tylko jedno 1000)
    println("seq = " + seq)
    seq -=(1000, 1000) // usuniecie kilku elementow
    println("seq = " + seq)
    seq remove 1 // usuniecie elementu o indeksie 1
    println("seq = " + seq)
    seq remove(2, 3) // usuniecie 3 elementow zaczynajac od elementu o indeksie 2
    println("seq = " + seq)
    seq trimStart 2 // usuniecie 2 pierwszych elementow
    println("seq = " + seq)
    seq trimEnd 2 // usuniecie 2 ostatnich elementow
    println("seq = " + seq)

    println()

    // Set - wstawianie
    val set = mutable.Set(1, 2, 3, 4)
    set += 5 // dodanie elementu do zbioru (i zwrocenie 'set')
    println("set = " + set)
    set +=(6, 7, 8, 9) // dodanie kilku elementow (i zwrocenie 'set')
    println("set = " + set)
    set ++= Set(20, 30, 40) // dodanie elementow innego zbioru (i zwrocenie 'set')
    println("set = " + set)
    println("set add 50 = " + (set add 50)) // dodanie 50, zwraca true gdy element jeszcze nie byl w zbiorze
    println("set = " + set)
    println()

    // Set - usuwanie
    set -= 5 // usuniecie (i zwrocenie 'set')
    println("set = " + set)
    set -=(6, 7, 8, 9) // usuniecie kilku elementow (i zwrocenie 'set')
    println("set = " + set)
    set --= Set(20, 30, 40) // usuniecie elementow innego zbioru (i zwrocenie 'set')
    println("set = " + set)
    println("set remove 50 = " + (set remove 50)) // usuniecie 50, zwraca true gdy element byl w zbiorze
    println("set = " + set)
    set(1) = false // usuniecie elementu 1, gdy true to byloby to dodanie
    println("set = " + set)
    println()

    // Map - wstawianie
    val map = mutable.Map(1 -> "one", 2 -> "two")
    println("map = " + map)
    map(3) = "three" // aktualizacja/dodanie nowego klucza (zwraca map)
    println("map = " + map)
    map put(0, "zero") //  aktualizacja/dodanie nowego klucza (zwraca poprzednia wartosc dla klucza jako Option)
    println("map = " + map)
    map +=(4 -> "four", 5 -> "five") // dodanie kilku kluczy/wartosci
    println("map = " + map)
    map ++= Map(9 -> "nine", 10 -> "ten") // dodanie elementow z innej mapy
    println("map = " + map)
    println()

    // Map - usuwanie
    map -= 3 // usuniecie klucza 3
    println("map = " + map)
    map -=(4, 5) // usuniecie kilku kluczy/wartosci
    println("map = " + map)
    map remove 0 // usuniecie wartosci dla klucza (zwraca poprzednia wartosc dla klucza jako Option)
    println("map = " + map)
  }
}
