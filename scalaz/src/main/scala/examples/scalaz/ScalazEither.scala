package examples.scalaz

import scalaz._
import Scalaz._

object ScalazEither {
  // typ Either (dysjunkcja dwoch typow), to samo co \/[String, Int]
  type Result = String \/ Int

  def main(args: Array[String]): Unit = {
    val errorResult: Result = "this is error".left
    val correctResult: Result = 42.right

    // domyslnie jest "right biased" wiec wszystkie operacje (map, flatMap itp.) operuja na prawej stronie
    correctResult.foreach(println(_))

    // nie ma prawej strony wiec nic nie zostanie wypisane
    errorResult.foreach(println(_))

    // mozna zamienic strony (left staje sie right i na odwrot)
    val swappedErrorResult: Int \/ String = ~errorResult // to samo co errorResult.swap

    swappedErrorResult.foreach(println(_))
  }

}
