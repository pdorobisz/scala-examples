package examples.scalaz


import scala.concurrent.Future
import scalaz._
import Scalaz._
import scala.concurrent.ExecutionContext.Implicits.global

// Monad transformer tworzy nowego monada.
// Monady sie nie komponuja ("Monads don't compose"):
// M[_], N[_] - monady
// M[N[_]] - nie zawsze jest monadem
object ScalazMonadTransformers {
  def main(args: Array[String]) {
    example1()
    example2()
  }

  def example1() {
    println("------ example 1 ------")
    type Result[+A] = String \/ Option[A]

    val result: Result[Int] = some(42).right
    val result2: Result[Int] = none[Int].right
    val result3: Result[Int] = "message".left
    println(result)
    result map (println(_)) // mozna sie dostac do wartosci wewnatrz

    // trzeba uzyc podwojnego map zeby zamienic Int na String
    val resultToStr: Result[String] = result map (_ map (_.toString))

    ////// wykorzystanie OptionT (monad transformer)
    // trzeba zdefiniowac pomocniczy typ dla OptionT
    type Error[+A] = \/[String, A]
    type ResultTransformed[A] = OptionT[Error, A] // transformer dla Error[Option[A]]

    val resultT = 42.point[ResultTransformed]
    val resultT2 = none[Int].point[ResultTransformed]
    val resultT3 = "message".left.point[ResultTransformed]

    // teraz wystarczy tylko jedno map zeby zamienic Int wewnatrz Option
    val resultToStrT: ResultTransformed[String] = resultT map (_.toString)
    // mozna tez uzyc flatMap or flatMapF
    val resultToStrT2: ResultTransformed[String] = resultT flatMap (_.toString.point[ResultTransformed])
    val resultToStrT3: ResultTransformed[String] = resultT flatMapF (_.toString.point[Error])

    // aby dostac sie do wartosci zawartej w transformerze nalezy uzyc run:
    println(resultToStrT.run)
  }

  def example2() {
    println("------ example 2 ------")
    // kombinacja Future i Option, nie skompiluje sie
    //    val result = for {
    //      user <- getUser(42) // Option[User]
    //      email = getEmail(user)
    //      success <- sendEmail(email) // zwroci Future[Option[Boolean]], ale flatMap oczekuje Option[?]
    //    } yield success

    val result: OptionT[Future, Boolean] = for {
      // wykorzystanie monad transformer aby wykorzystac flatMap na Future[Option[User]]
      user: User <- OptionT.optionT(Future.successful(getUser(1)))
      email: Email = getEmail(user)
      success: Boolean <- OptionT.optionT(sendEmail(email))
    } yield success
  }

  def getUser(id: Int): Option[User] = if (id == 1) some(User(id, "john")) else none

  def getEmail(user: User): Email = Email(user.name + "@abc.com")

  def sendEmail(email: Email): Future[Option[Boolean]] = Future.successful(Some(true))

  case class User(id: Int, name: String)

  case class Email(email: String)

}
