package examples.functions

object FunctionsVsMethods {

  def main(args: Array[String]) {
    val f1 = (a: Int) => a + 1 // funkcja
    def f2 = (a: Int) => a + 1 // metoda zwracajaca przy kazdym wywolaniu nowa funkcje

    // wypisuja to samo
    println("f1(2)=" + f1(2))
    println("f2(2)=" + f2(2))

    println(f1 == f1) // true (ten sam obiekt reprezentujacy funkcje)
    println(f2 == f2) // false (za kazdym razem otrzymujemy nowy obiekt)

    //
    val myFunction = new MyFunction
    println("myFunction(10)=" + myFunction(10))
  }

  // definiowanie funkcji, ((Int) => Int) - skrot dla Function1[Int, Int]
  class MyFunction extends ((Int) => Int) {
    def apply(x: Int): Int = x * 10
  }

}
