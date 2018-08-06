package examples.techniques.di

/**
 * Bardziej rozbudowana implementacja z uzyciem self-types, w ktorej mozna latwo podmienic konkretne implementacje komponentow.
 */
object CakePattern2 {
  def main(args: Array[String]) {
    val userService: ComponentRegistry.UserService = ComponentRegistry.userService
    userService.create("ala", "ala123")
    userService.delete(User("ala", "ala123"))
  }

  case class User(name: String, password: String)

  // 1. Definicja klas (ich traitow oraz konkretnych implementacji), ktore beda wstrzykiwane (opakowujemy je w trait).
  trait UserRepositoryComponent {

    val userRepository: UserRepository

    trait UserRepository {
      def create(user: User)

      def delete(user: User)
    }

  }

  // konkretna implementacja komponentu
  trait UserRepositoryComponentImpl extends UserRepositoryComponent {

    val userRepository: UserRepository = new UserRepositoryImpl

    class UserRepositoryImpl extends UserRepository {
      def create(user: User) = println("creating user: " + user)

      def delete(user: User) = println("deleting user: " + user)
    }

  }

  trait LoggerComponent {
    val logger: Logger

    trait Logger {
      def log(message: String)
    }

  }

  // konkretna implementacja komponentu
  trait LoggerComponentImpl extends LoggerComponent {
    val logger: Logger = new LoggerImpl

    class LoggerImpl extends Logger {
      def log(message: String) = println(s"LOG $message")
    }

  }

  trait UserServiceComponent {

    val userService: UserService

    trait UserService {
      def create(username: String, password: String)

      def delete(user: User)
    }

  }

  // konkretna implementacja komponentu
  trait UserServiceComponentImpl extends UserServiceComponent {

    // UWAGA!!! W kazdej implementacji dziedziczacej z tej musimy definiowac self-type gdyz ta deklaracja nie jest
    // dziedziczona.
    this: UserRepositoryComponent with LoggerComponent =>

    val userService: UserService = new UserServiceImpl

    class UserServiceImpl extends UserService {
      def create(username: String, password: String) = {
        logger.log("creating user")
        userRepository.create(new User(username, password))
      }

      def delete(user: User) = {
        logger.log("deleting user")
        userRepository.delete(user)
      }
    }

  }

  // 2. Skladamy komponenty i nastepuje automatycznie "wstrzykniecie" zaleznosci.
  // W razie potrzeby mozemy stworzyc inny obiekt na potrzeby testow, a w nim np. zamockowac UserRepository.
  object ComponentRegistry extends UserServiceComponentImpl with UserRepositoryComponentImpl with LoggerComponentImpl

}
