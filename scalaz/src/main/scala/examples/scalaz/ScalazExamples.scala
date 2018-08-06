package examples.scalaz

import scalaz._
import Scalaz._

object ScalazExamples {
  def main(args: Array[String]) {
    println("---Memoization---")
    // Memo dostarcza rozne warianty cache'u
    val cache: Int => Int = Memo.weakHashMapMemo {
      // wartosc bedzie obliczona tylko raz
      case n: Int =>
        println(s"calculating value for $n")
        n * 10
    }

    println(cache(5))
    println(cache(5))

    println("\n---Style---")
    val add2: Int => Int = n => n + 2
    val multiply10: Int => Int = n => n * 10
    // thrush operator
    println(5 |> add2 |> multiply10) // to samo co multiply10(add2(5))

    // ternary operator
    val b: Boolean = 1 > 2
    println(b ? "yes" | "no")

    val option: Option[Int] = None
    println(option | 0) // to samo co option.getOrElse(0)

    val option2: Option[String] = "abc".some // zwraca Option, nie Some
    val option3: Option[String] = none[String] // zwraca Option, nie None

    validations()
  }

  def validations() = {
    println("\n---Validation---")
    val success1: Validation[String, Int] = Validation.success[String, Int](123)
    val success2: Validation[String, Int] = Validation.success[String, Int](456)
    val failure1: Validation[String, Int] = Validation.failure[String, Int]("some failure1")
    val failure2: Validation[String, Int] = Validation.failure[String, Int]("some failure2")

    // mozna zamienic Validation na ValidationNel, ktore przechowuje bledy w postaci listy (Validation[NonEmptyList[E], X])
    val failure1Nel: ValidationNel[String, Int] = failure1.toValidationNel
    val failure2Ne: ValidationNel[String, Int] = failure2.toValidationNel
    println(failure1)
    println(failure1Nel)

    // polaczenie walidacji i zebranie bledow
    val combinedFailures: Validation[NonEmptyList[String], Int] = (failure1Nel |@| failure2Ne) { (a, b) => a + b }
    val combinedSuccess: Validation[NonEmptyList[String], Int] = (success1.toValidationNel |@| success2.toValidationNel) { (a, b) => a + b }
    println(combinedFailures)
    println(combinedSuccess)
  }
}
