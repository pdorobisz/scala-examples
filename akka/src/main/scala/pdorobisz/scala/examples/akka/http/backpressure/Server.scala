package pdorobisz.scala.examples.akka.http.backpressure

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

object Server extends App {
  implicit val system = ActorSystem("server-system")
  implicit val materializer = ActorMaterializer()

  val route =
    path("test") {
      get {
        complete("test")
      }
    }

  Http().bindAndHandle(route, "localhost", 8080)
}
