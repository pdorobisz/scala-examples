package pdorobisz.scala.examples.cats.core.examples

import cats._
import cats.data.EitherT
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

/**
  * Sequential iteration over Futures containing Either (EitherT used). Next future is executed only when previous one
  * completes. If previous one returns an error wrapped in Left computation stops and subsequent futures are not executed.
  */
object Example2 extends App {

  case class ServiceError(m: String)

  def sendEmail(address: String): EitherT[Future, ServiceError, Unit] =
    if (address.isEmpty || !address.contains("@"))
      EitherT.leftT[Future, Unit](ServiceError(s"invalid address: $address"))
    else {
      val f = Future {
        println(s"${System.currentTimeMillis()}: sending to $address...")
        Thread.sleep(address.length * 100)
        println(s"${System.currentTimeMillis()}: $address completed!")
      }
      EitherT.liftF(f)
    }

  // invalid address commented out
  val addresses = List("someverylongemailtosend@some.very.long.domain.com", "a@x.y", "medium@mail.com", /*"invalid",*/ "last@mail.com")

  val result: EitherT[Future, ServiceError, Unit] = addresses.foldLeft(EitherT.rightT[Future, ServiceError](())) { (previous, address) =>
    println(s"${System.currentTimeMillis()}: received: $address")
    previous.flatMap(_ => sendEmail(address))
  }

  // If invalid email is provided than it will return Left with an error inside.
  println(Await.result(result.value, Duration.Inf))
}
