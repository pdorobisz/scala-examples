package pdorobisz.scala.examples.akka.streams

import java.time.LocalTime
import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.stream.scaladsl.{RestartSource, Sink, Source}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Random

/**
  * Example of stream that collects, processes and stores logs in database. Steps:
  *
  * 1. Periodically generate "tick" that triggers whole process (source)
  * 2. Fetch list of currently logged in users from external server
  * 3. Get stream of logs from external server for every logged in user and combine it into text
  * 4. Create object that stores logs for all currently logged in users.
  * 5. Save logs in database
  * 6. Print logs object (sink)
  *
  * Stream handles following issues:
  * - delays in processing (applies back-pressure to stop processing new "tick" while previous one is not finished)
  * - failure when getting user's log stream (restarts)
  * - failure when getting currently logged in users or writing to database (ignores and waits for next "tick")
  */
object StreamExample extends App {

  implicit val system = ActorSystem("TestSystem")
  implicit val materializer = ActorMaterializer()
  var stateId = 0

  def nextStateId = {
    stateId = stateId + 1
    stateId
  }

  def log(s: String) = println(s"${LocalTime.now()}: $s")


  /////////////// Data model ///////////////

  case class WebsiteState(id: Int, time: LocalTime, loggedInUsers: Set[String])

  case class WebsiteLogs(id: Int, time: LocalTime, logs: Set[UserLog])

  case class UserLog(userId: String, logs: String)

  /////////////// Methods simulating calling external services ///////////////

  def fetchCurrentState(id: Int) = Future {
    Thread.sleep(200)
    if (Random.nextFloat() < 0.1) {
      log(s"failed to get state: $id")
      throw new Exception(s"failed to get state: $id")
    } else {
      log(s"fetched state: $id")
      val currentUsers = List.tabulate(4)(e => s"usr${id}_$e").toSet
      WebsiteState(id, LocalTime.now(), currentUsers)
    }
  }

  def fetchUserLogs(userId: String) = Future {
    Thread.sleep(600)
    if (Random.nextFloat() < 0.2) {
      log(s"failed to get logs for user: $userId")
      throw new Exception(s"failed to get logs for user: $userId")
    } else {
      log(s"fetched logs for user: $userId")
      Source(List(s"$userId-action1", s"$userId-action2", s"$userId-action3"))
    }
  }

  def saveInDatabase(w: WebsiteLogs) = Future {
    log(s"start writing logs to db: ${w.id}")
    Thread.sleep(2000)
    log(s"finished writing logs to db: ${w.id}")
    w
  }

  /////////////// Stream building blocks ///////////////

  val src = Source
    .tick(0.seconds, FiniteDuration(1, TimeUnit.SECONDS), ())
    .map { _ =>
      val id = nextStateId
      log(s"TICK: $id")
      id
    }

  val sink = Sink.foreach[Any](x => log("sink: " + x))

  def websiteLogsSource(s: WebsiteState) = {
    def userLogsSource(userId: String) = {
      log(s"getting logs for user $userId")
      RestartSource.onFailuresWithBackoff(minBackoff = 100.millis, maxBackoff = 300.millis, randomFactor = 0.2) { () =>
        Source.fromFuture(fetchUserLogs(userId))
          .flatMapConcat(identity)
          .reduce((a, b) => s"$a, $b")
          .map(s => UserLog(userId, s"[$s]"))
      }
    }

    log(s"start processing website state: $s")
    Source.single(s)
      .mapConcat(_.loggedInUsers)
      .flatMapMerge(2, userLogsSource)
      .fold(List.empty[UserLog])((list, element) => element :: list)
      .map(list => {
        log(s"collected all logs for state ${s.id}")
        WebsiteLogs(s.id, s.time, list.toSet)
      })
  }

  val loggingMaterializer = ActorMaterializer(ActorMaterializerSettings(system).withSupervisionStrategy { e =>
    log("An exception has occurred, resuming: " + e)
    Supervision.resume
  })

  /////////////// Stream definition ///////////////

  src
    .mapAsync(1)(fetchCurrentState)
    .flatMapConcat(websiteLogsSource)
    .mapAsync(1)(saveInDatabase)
    .runWith(sink)(loggingMaterializer)
}
