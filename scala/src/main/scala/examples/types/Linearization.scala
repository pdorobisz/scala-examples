package examples.types

//import scala.reflect.runtime.universe._

/**
 * Linearization to proces "splaszczania" hierarchii dziedziczenia, tj. liniowe ulozenie klas zgodnie
 * z hierarchia dziedziczenia.
 * Algorytm:
 * 1. Dodac typ biezacej instancji:
 * 2. Dodac linearyzacje typow z ktorych biezacy typ dziedziczy (w kolejnosci od prawej do lewej) pomijajac AnyRef i Any
 * 3. Usunac powtorzenia typow zaczynajac od lewej (ma zostac tylko to wystapienie, ktore jest najbardziej z prawej)
 * 4. Dodac AnyRef i Any
 * Ten algorytm przesuwa kazdy typ w prawo dopoki wszystkie typy z niego dziedziczace znajda sie po jego lewej stronie.
 * Po linearyzacji mozna okreslic nadklase dowolnej klasy (po lewej stronie klasa najnizej w hierarchii, po prawej - najwyzej).
 */
object Linearization {
  def main(args: Array[String]) {
    val c: C2 = new C2
    println(c.m)

    //TODO fix
//    typeOf[C2].baseClasses foreach {
//      s => println(s.fullName)
//    }
  }

  class C1 {
    def m = List("C1")
  }

  trait T1 extends C1 {
    override def m = {
      "T1" :: super.m
    }
  }

  trait T2 extends C1 {
    override def m = {
      "T2" :: super.m
    }
  }

  trait T3 extends C1 {
    override def m = {
      "T3" :: super.m
    }
  }

  class C2A extends T2 {
    override def m = {
      "C2A" :: super.m
    }
  }

  /**
   * Linearyzacja te klasy:
   * 1. Biezacy typ:
   * C2
   * 2. Dodajemy linearyzacja typu najbardziej z prawej (T3):
   * C2, T3, C1
   * 3. Dodajemy linearyzacje kolejnego typu (T2):
   * C2, T3, C1, T2, C1
   * 4. Dodajemy linearyzacje kolejnego typu (T1):
   * C2, T3, C1, T2, C1, T1, C1
   * 5. Dodajemy linearyzacje kolejnego typu (C2A):
   * C2, T3, C1, T2, C1, T1, C1, C2A, T2, C1
   * 6. Usuniecie powtorzen C1:
   * C2, T3, T2, T1, C2A, T2, C1
   * 7. Usuniecie powtorzen T2:
   * C2, T3, T1, C2A, T2, C1
   * 8. Dopisujemy AnyRef i Any:
   * C2, T3, T1, C2A, T2, C1, AnyRef, Any
   */
  class C2 extends C2A with T1 with T2 with T3 {
    override def m = {
      "C2" :: super.m
    }
  }

  // ------------------------------------------------------

  class A

  class B

  trait TA extends A

  trait TB extends B

  // Nie skompiluje sie. Do klasy mozna dolaczyc kazdy trait bezposrednio rozszerzajacy AnyRef. W przeciwnym przypadku:
  // do klasy X dziedziczacej z klasy A moga byc dolaczone tylko te traity, ktore dziedzicza z A lub z jego przodka.
  //  class X extends A with TB

  // ok, TA dziedziczy z A
  class X extends A with TA

}
