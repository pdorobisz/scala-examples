package examples.scalaz

import scalaz.Scalaz._
import scalaz._


/**
  * Validation umozliwia reprezentacje Success i Failure.
  * ValidationNel reprezentuje Validation przechowujace bledy w postaci listy
  */
object ScalazValidation {
  def main(args: Array[String]): Unit = {

    val success1: ValidationNel[String, String] = "success 1 ok".successNel[String]
    val success2: ValidationNel[String, String] = "success 2 ok".successNel[String]
    val success3: ValidationNel[String, String] = "success 3 ok".successNel[String]
    val error1: ValidationNel[String, String] = "error 1".failureNel[String]
    val error2: ValidationNel[String, String] = "error 2".failureNel[String]
    val error3: ValidationNel[String, String] = "error 3".failureNel[String]

    val result1: ValidationNel[String, String] = (success1 |@| success2 |@| success3) { _ + _ + _}
    val result2: ValidationNel[String, String] = (success1 |@| error2 |@| error3) { _ + _ + _}

    println(result1) // Success zawierajacy wynik (skonkatenowane napisy)
    println(result2) // Failure zawierajace liste bledow

    ////////
    val successInt1 = 1.success[String]
    val successInt2 = 2.success[String]
    val successInt3 = 3.success[String]
    val errorInt1 = "error 1".failure[Int]
    val errorInt2 = "error 2".failure[Int]

    val resultInt1: Validation[String, Int] = (successInt1 |@| successInt2 |@| successInt3){ _ + _ + _}
    val resultInt2: Validation[String, Int] = (errorInt1 |@| successInt2 |@| errorInt2){ _ + _ + _}
    println(resultInt1) // Success zawierajacy wynik (suma)
    println(resultInt2) // Failure zawierajace skonkatenowane bledy
  }
}
