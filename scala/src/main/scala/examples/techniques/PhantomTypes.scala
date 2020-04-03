package examples.techniques

/**
 * Phantom Type - exists only in definition but there's no instance of this type.
 * Used to enforce objects state and make some methods not available in some states.
 */
object PhantomTypes {

  sealed trait ServiceState

  final class Started extends ServiceState

  final class Stopped extends ServiceState

  class Service[State <: ServiceState] private() {
    // Type T is a "phantom type" because there's no instance of it

    // T - supertype and subtype of Stopped
    def start[T >: State <: Stopped]() = this.asInstanceOf[Service[Started]]

    def stop[T >: State <: Started]() = this.asInstanceOf[Service[Stopped]]
  }

  object Service {
    def create() = new Service[Stopped]
  }

  def main(args: Array[String]) {
    val initiallyStopped: Service[Stopped] = Service.create()

    // ok (State = Stopped), condition is matched: T >: Stopped <: Stopped
    val started: Service[Started] = initiallyStopped.start()

    // ok (State = Started), condition is matched: T >: Started <: Started
    val stopped: Service[Stopped] = started.stop()

    // it's illegal to call stop on already stopped service
    // it won't compile (State = Stopped), because there's no type matching this condition: T >: Stopped <: Started
    // stopped.stop()

    // it's illegal to call start on already started service
    // it won't compile (State = Started), because there's no type matching this condition: T >: Started <: Stopped
    // started.start()
  }
}
