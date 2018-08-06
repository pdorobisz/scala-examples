package examples.types

/**
 * Phantom Type - poniewaz typ wystepuje w definicji, ale nie wystepuje instancja tego typu.
 * Dzieki tej technice mozna uniemozliwic wywolywanie metod w pewnych stanach.
 */
object PhantomTypes {

  sealed trait ServiceState

  final class Started extends ServiceState

  final class Stopped extends ServiceState

  class Service[State <: ServiceState] private() {
    // Typ T to "phantom type" gdyz nie ma zadnej instancji tego typu.

    // T - nadtyp State i podtyp Stopped
    def start[T >: State <: Stopped]() = this.asInstanceOf[Service[Started]]

    def stop[T >: State <: Started]() = this.asInstanceOf[Service[Stopped]]
  }

  object Service {
    def create() = new Service[Stopped]
  }

  def main(args: Array[String]) {
    val initiallyStopped: Service[Stopped] = Service.create()

    // ok (State = Stopped), spelniony jest warunek: T >: Stopped <: Stopped
    val started: Service[Started] = initiallyStopped.start()

    // ok (State = Started), spelniony jest warunek: T >: Started <: Started
    val stopped: Service[Stopped] = started.stop()

    // nie skompiluje sie (State = Stopped), nie ma typu spelniajacego warunek: T >: Stopped <: Started
    // stopped.stop()

    // nie skompiluje sie (State = Started), nie ma typu spelniajacego warunek: T >: Started <: Stopped
    // started.start()
  }
}
