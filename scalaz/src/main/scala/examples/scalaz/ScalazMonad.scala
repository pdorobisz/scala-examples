package examples.scalaz

import scalaz._, Scalaz._
import scalaz.Kleisli._

object ScalazMonad {

  case class Make(id: Int, name: String)

  case class Part(id: Int, name: String)

  def main(args: Array[String]) {
    val make: (Int) => Option[Make] =
      (x: Int) => (x == 1).option(Make(1, "Suzuki"))

    val parts: Make => List[Part] = {
      case Make(1, _) => List(Part(1, "Gear Box"), Part(2, "Clutch cable"))
      case _ => Nil
    }

    val parts2: (Make) => Option[NonEmptyList[Part]] =
      (x: Make) => (x.id == 1).option(NonEmptyList(Part(1, "Gear Box"), Part(2, "Clutch cable")))

    val intToMaybeList: (Int) => Option[NonEmptyList[Part]] = make(_) flatMap parts2


    def str(x: Int): Option[String] = Some(x.toString)
    def toInt(x: String): Option[Int] = Some(x.toInt)
    def double(x: Int): Option[Double] = Some(x * 2)

    val funky: ReaderT[Option, Int, Int] = kleisli(str _) >=> kleisli(toInt _)// >=> (double _)
      println(funky.apply(123))

  }
}
