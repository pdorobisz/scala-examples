package examples.scalaz

import scalaz.Scalaz._
import scalaz._

/**
  * State przechowuje stan i wartosc. Nowa wartosc jest obliczana przy pomocy przekazanej funkcji.
  */
object ScalazState {
  def main(args: Array[String]): Unit = {
    val s = State[Int, String](i => (i + 1, "str" + i))
    println(s.eval(5)) // nowy stan zostaje odrzucony, a wartosc zwrocona (str5)

    println(s.exec(5)) // nowy stan zostaje zwrocony (6), a wartosc odrzucona

    println(s(5)) // zwrocenie nowej wartosci i stanu

    // komponowanie stanow
    val composedState = for {
      s1 <- newS()
      s2 <- newS()
    } yield (s1, s2)
    println(composedState.eval(6))
  }

  def newS() = State[Int, String](i => (i + 1, "str" + i))
}
