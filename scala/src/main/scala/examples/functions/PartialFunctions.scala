package examples.functions

object PartialFunctions {
  def main(args: Array[String]) {

    // Partial Function - funkcja zdefiniowana tylko dla okreslonych wartosci
    val pf: PartialFunction[Int, String] = {
      case i if i > 0 => "pf " + i.toString
    }

    // mozna zdefiniowac tez tak:
    val pf2 = new PartialFunction[Int, String] {
      def apply(arg: Int) = arg match {
        case arg if arg > -10 => "pf2 " + arg.toString
      }

      def isDefinedAt(arg: Int) = if (arg > -10) true else false
    }

    // sprawdzenie czy funkcja jest zdefiniowana dla podanych argumentow
    println("pf.isDefinedAt(0)=" + pf.isDefinedAt(0))
    println("pf.isDefinedAt(1)=" + pf.isDefinedAt(1))

    // lift - zamiana Partial Function na funkcje zwracajaca None dla argumentow dla ktorych nie jest zdefiniowana (unlift - odwrotnosc)
    val lift: (Int) => Option[String] = pf.lift
    println(lift(0)) // wypisze None
    //println(pf(0)) // rzuci wyjatkiem MatchError

    // stworzenie nowej funkcji czesciowej
    val pfOrPf2 = pf orElse pf2
    println("pfOrPf2(-1)=" + pfOrPf2(-1))

    // mozna to tez zrobic bez tworzenia nowej funkcji czesciowej wywolujac funkcje w nastepujacy sposob:
    println("pf.applyOrElse(0, pf2)=" + pf.applyOrElse(0, pf2))
  }
}
