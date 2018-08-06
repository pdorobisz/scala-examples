package examples.scalaz

import scalaz.Scalaz._
import scalaz.{Writer, _}

/**
  * Writer monad pozwala na zbieranie wartosci - funkcja moze zapisac do niego wartosc, ktora
  * bedzie pozniej uzyta w jakis sposob.
  */
object ScalazWriterMonad {

  // Writer[W, A]: W - typ zbierajacy wartosci zapisywane przez funkcje, A - typ zwracany przez funkcje
  // W - musi byc monoidem (element zerowy i operator konkatenacji)
  type Result[T] = Writer[List[String], T]

  def action1(): Result[Int] = {
    val res = 10
    // stworzenie Writera poprzez wywolanie set
    res.set(List(s"action1 invoked: $res"))
  }

  def action2(x: Int): Result[String] = {
    val res = s"<$x>"
    res.set(List(s"action2 invoked: $res"))
  }

  def action3(x: String): Result[Option[String]] = {
    val res = x.some
    res.set(List(s"action3 invoked: $res"))
  }

  def action21(): Int = 10

  def action22(x: Int): String = s"<$x>"

  def action23(x: String): Option[String] = x.some

  def main(args: Array[String]): Unit = {
    val result = for {
    // wartosci dodawane do writera sa w nim agregowane
      res1 <- action1()
      res2 <- action2(res1)
      res3 <- action3(res2)
    } yield res3

    // run wywoluje obliczenie, rezultat zawiera wynik obliczenia jak i wszystkie zebrane przez Writera wartosci
    val runResult: (List[String], Option[String]) = result.run
    println(runResult)

    // zamiast zwracac Writera w funkcji mozna wywolac set (stworzenie Writera) juz po jej wywolaniu
    val result2 = for {
      res1 <- action21() set "action21 invoked"
      res2 <- action22(res1) set "action22 invoked"
      res3 <- action23(res2) set "action23 invoked"
    } yield res3
    println(result2)
  }
}
