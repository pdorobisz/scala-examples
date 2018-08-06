package examples.types

/**
 * Value Classes (wprowadzone w Scali 2.10) umozliwiaja optymalizacje polegajaca na tym, ze podczas dzialania
 * nie jest tworzona instancja tej klasy opakowujaca obiekt innego typu, a zamiast niej JVM posluguje sie bezposrednio
 * opakowywanym typem. Pozwala to zwiekszyc wydajnosc i zmniejszyc uzycie pamieci.
 * Nie unikniemy stworzenie obiektu value class gdy:
 * - obiekt typu value class jest traktowany jako inny typ
 * - obiekt typu value class jest wstawiany do tablicy
 * - wystepuje sprawdzenie typu w runtimie (asInstanceOf) lub pattern matching
 */
object ValueClasses {
  def main(args: Array[String]) {

    // nie wystapi zainstancjonowanie klasy Meter, zamiast tego zostana tu uzyte wartosci 2 i 3.
    val meter: Meter = add(new Meter(2), new Meter(3))

    val d: Distance = new Meter(2)
    // To wywolanie bedzie wymagac zainstancjonowania klasy Meter, gdyz traktuje value class (Meter) jako inny typ (Distance).
    val meter2: Distance = add2(d, d)
  }

  // po skompilowaniu ta metoda zostanie zamieniona na metode przyjmujaca double, a nie Meter
  def add(a: Meter, b: Meter): Meter = a + b

  def add2(a: Distance, b: Distance): Distance = a

  // Universal trait (trait rozszerzajacy Any i posiadajacy tylko def-y)
  trait Distance extends Any

  // Value Class;
  // - moze tylko rozszerzac universal trait (ale nie musi) i musi dziedziczyc z AnyVal
  // - moze miec tylko glowny konstruktor z dokladnie jednym polem
  // - moze miec tylko def-y
  class Meter(val value: Double) extends AnyVal with Distance {
    def +(m: Meter): Meter = new Meter(value + m.value)
  }

}
