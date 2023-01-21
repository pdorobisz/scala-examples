package pdorobisz.examples.catseffect

import cats.effect.{IO, IOApp}

import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * There are 2 thread pools available:
 * - unbounded pool for blocking operations
 * - fixed-size work-stealing pool with same number of threads as available processors for compute-based operations
 *
 * cats.effect.unsafe.metrics.ComputePoolSampler MBean shows thread pool summary:
 * - ActiveThreadCount - number of threads in use from compute-based pool
 * - BlockedThreadCount - number of threads in use from blocking operations pool
 * - WorkerThreadCount - total number of threads available in compute-based pool (should be equal to the number of CPUs)
 */
object ThreadPools extends IOApp.Simple {

  private val cpuCount: Int = Runtime.getRuntime.availableProcessors()

  private def action(id: String) = {
    val now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    println(s"$now $id: " + Thread.currentThread())
    Thread.sleep(300000)
  }

  val run = for {
    _ <- IO.println(s"Available cores: $cpuCount")

    // start IOs in "io-compute-blocker" thread pool
    // all IOs will start immediately, even if their number is higher than number of available CPUs
    _ <- IO.blocking(action("blocking1")).start
    _ <- IO.blocking(action("blocking2")).start
    _ <- IO.blocking(action("blocking3")).start
    _ <- IO.blocking(action("blocking4")).start
    _ <- IO.blocking(action("blocking5")).start
    _ <- IO.blocking(action("blocking6")).start
    _ <- IO.blocking(action("blocking7")).start
    _ <- IO.blocking(action("blocking8")).start
    _ <- IO.blocking(action("blocking9")).start
    _ <- IO.blocking(action("blocking10")).start
    _ <- IO.blocking(action("blocking11")).start
    _ <- IO.blocking(action("blocking12")).start
    _ <- IO.blocking(action("blocking13")).start
    _ <- IO.blocking(action("blocking14")).start

    // start IOs in "io-compute" thread pool
    // only first $cpuCount IOs will be started, next ones will be started when blocked threads become available
    _ <- IO(action("compute-1")).start
    _ <- IO(action("compute-2")).start
    _ <- IO(action("compute-3")).start
    _ <- IO(action("compute-4")).start
    _ <- IO(action("compute-5")).start
    _ <- IO(action("compute-6")).start
    _ <- IO(action("compute-7")).start
    _ <- IO(action("compute-8")).start
    _ <- IO(action("compute-9")).start
    _ <- IO(action("compute-10")).start
    _ <- IO(action("compute-11")).start
    fib <- IO(action("compute-12")).start

    _ <- IO.println("Started...")
    _ <- fib.join
  } yield ()
}
