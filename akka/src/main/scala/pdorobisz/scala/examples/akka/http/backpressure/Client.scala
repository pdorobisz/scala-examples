package pdorobisz.scala.examples.akka.http.backpressure

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, ResponseEntity}
import akka.stream.ActorMaterializer

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Client extends App {

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val system = ActorSystem("client-system")
  implicit val materializer = ActorMaterializer()

  val response: Future[HttpResponse] = Http().singleRequest(
    HttpRequest(uri = s"http://localhost:8080/test")
  )

  val entity = Await.result(response.map(r => r.entity), Duration.Inf)
  println(entity)
  System.exit(0)
}
