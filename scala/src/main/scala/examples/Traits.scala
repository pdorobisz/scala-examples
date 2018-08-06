package examples

object Traits {

  // podstawowy trait
  trait Engine {
    def work = "wrum"
  }

  // trait modyfikujacy zachowanie zaimplementowane w podstawowym trait
  trait BigEngine extends Engine {
    override def work = "BRUM BRUM " + super.work
  }

  // klasa z pewnym zachowaniem, ktore moze zostac zmodyfikowane przez dodanie innego traitu
  class Car extends Engine {
    def drive = "engine is working: " + work
  }

  def main(args: Array[String]) {
    // podstawowa klasa
    val car1: Car = new Car
    // modyfikujemy oryginalne zachowanie dodajac (mixin) dodatkowy trait
    val car2 = new Car with BigEngine

    println("car1: " + car1.drive)
    println("car2: " + car2.drive)
  }
}

