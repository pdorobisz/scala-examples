package examples.techniques.di

/**
 * Przyklad Cake Pattern z uzyciem self-types.
 */
object CakePattern {
  def main(args: Array[String]) {
    val userService: ComponentRegistry.UserService = ComponentRegistry.userService
    userService.create("ala", "ala123")
    userService.delete(User("ala", "ala123"))
  }

  case class User(name: String, password: String)

  // 1. Definicja klas, ktore beda wstrzykiwane (opakowujemy je w trait).
  trait UserRepositoryComponent {

    val userRepository: UserRepository

    class UserRepository {
      def create(user: User) = println("creating user: " + user)

      def delete(user: User) = println("deleting user: " + user)
    }

  }

  trait LoggerComponent {
    val logger: Logger

    class Logger {
      def log(message: String) = println(s"LOG $message")
    }

  }

  trait UserServiceComponent {

    // self-type annotation - zdefiniowanie typu "this" (oznacza to, ze klasa implementujaca ten trait bedzie musiala
    // tez implementowac UserRepositoryComponent (posiada pole userRepository, do ktorego mozemu sie juz tutaj odwolac).
    // W ten sposob definiuje sie zaleznosc od innych komponentow.
    this: UserRepositoryComponent with LoggerComponent =>

    val userService: UserService

    class UserService {
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
  // W razie potrzeby mozemy stworzyc podobny obiekt na potrzeby testow, a w nim np. zamockowac UserRepository.
  object ComponentRegistry extends UserServiceComponent with UserRepositoryComponent with LoggerComponent {
    // w testach mozna to byc: mock(classOf[UserRepository])
    val userRepository = new UserRepository
    val logger = new Logger
    val userService = new UserService
  }

}
