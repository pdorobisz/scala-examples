package examples.functions

object Functions {
  def main(args: Array[String]) {
    val f = (x: Int) => x + 1
    val g = (x: Int) => x * 10

    val h1 = f compose g // zlozenie funkcji: h1 = f(g(x))
    val h2 = f andThen g // h2 = g(f(x))
    val h3 = Function.chain(Seq(f, g)) // h3 = g(f(x))

    println("h1(10)=" + h1(10))
    println("h2(10)=" + h2(10))
    println("h3(10)=" + h3(10))

    // ***Partial application***
    // Mozna podac tylko czesc argumentow i w ten sposob powstaje nowa funkcja.
    val fm: (Int, Boolean) => Double = m(_, "xxx", _)

    // W szczegolnym przypadku niepodajac zadnego argumentu zamienimy metode na funkcje:
    val funM: (Int, String, Boolean) => Double = m
    // lub:
    // val funM = m _

    // "Partial application" moze tez dotyczyc funkcji:
    val fm2: Int => Double = fm(_, false)

    // ***Currying***
    val ffm2: Int => String => Boolean => Double = m2
    val ffm2Curried: String => Boolean => Double = ffm2(2) // to juz NIE jest "partial application"

    // Przeksztalcenie funkcji do postaci curried przy uzyciu metody curried (odwrotnosc: uncurried):
    val funMCurried: Int => String => Boolean => Double = funM.curried

    // ***Partial application vs Currying***
    def evaluate[T, V](x: T, f: T => V) = f(x)
    def evaluateCurried[T, V](x: T)(f: T => V) = f(x)
    evaluate(1, (x: Int) => x + 1) // trzeba podac typ argumentu funkcji
    evaluateCurried(1)(x => x + 1) // nie trzeba podawac, kompilator sam okresli jaki jest typ
    // mozna tez zastosowac notacje z nawiasami klamrowymi:
    evaluateCurried(1) {
      x => x + 1
    }


    val abc = xx _
    abc

    // _ vs (_) podczas przeksztalcania metody w funkcje
    val add1 = add({
      println("add1 argument"); 1
    }) _ // pierwszy argument jest obliczany w momencie definiowania nowej funkcji

    val add2 = add({
      println("add2 argument"); 2
    }) (_) // pierwszy argument jest obliczany za kazdym razem gdy funkcja jest wywolywana

    println("----")
    add1(10)
    add1(11)
    add2(20)
    add2(21)
  }

  def xx(x: Int) = x

  def add(x: Int)(y: Int) = x + y

  def m(i: Int, s: String, b: Boolean): Double = {
    println(s"method m: $i, $s, $b")
    1.2
  }

  // metoda moze miec kilka list argumentow co ulatwia currying
  def m2(i: Int)(s: String)(b: Boolean): Double = {
    println(s"method m2: $i, $s, $b")
    1.2
  }
}
