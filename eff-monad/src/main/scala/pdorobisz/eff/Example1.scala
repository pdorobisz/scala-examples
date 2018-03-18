package pdorobisz.eff

import cats.data.{NonEmptyList, Reader, Writer}
import com.esv.eff.domain.{Article, User}
import org.atnos.eff.ErrorEffect._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.future._
import org.atnos.eff.syntax.all._
import org.atnos.eff.syntax.future._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * Eff monad is able to behave like a combination of other monads.
  */
object Example1 extends App {

  implicit val scheduler = ExecutorServices.schedulerFromGlobalExecutionContext

  import scala.concurrent.ExecutionContext.Implicits.global

  // 1. Stack of all effects used by program
  type AppStack = Fx.fx7[Validate[String, ?], Option[?], Either[String, ?], ErrorOrOk[?], Reader[String, ?], Writer[String, ?], TimedFuture[?]]

  // declare that effects (Either, Validate, etc.) are members in an arbitrary stack R of effects
  type _either[R] = MemberIn[Either[String, ?], R]
  type _reader[R] = MemberIn[Reader[String, ?], R]
  type _writer[R] = MemberIn[Writer[String, ?], R]
  type _validate[R] = MemberIn[Validate[String, ?], R]

  // 2. Methods returning different effects. All effects are lifted to Eff
  // Eff provides methods to lift effects to Eff (.e.g fromFuture)

  def returnFuture[R: _future](): Eff[R, User] = fromFuture(Future(User(1234, "John Smith")))

  def returnEither[R: _either](): Eff[R, User] =
  // left("some problem")
    right(User(5678, "John Doe"))

  def returnOption[R: _option](title: String, content: String, users: Seq[User]): Eff[R, Article] =
  // none
    some(Article(title, content, users))

  def returnValidate[R: _validate](s: String): Eff[R, Unit] = validateCheck(s.nonEmpty, "input is empty")

  def returnValidateValue[R: _validate](s: String): Eff[R, String] = validateValue(s.length > 5, s"$s Bye bye.", "input too short")

  // represents Eval effect which can fail with either an exception or somete failure F: Either[Either[Throwable, F], A]
  // ErrorEffect - default implementation with String as a failure type
  def returnException[R: _errorOrOk](): Eff[R, String] =
  //    ErrorEffect.exception(new IllegalArgumentException("illegal argument given"))
  //    fail("something went wrong")
  ok("Hello World")

  // ask returns value provided by Reader
  def returnReader[R: _reader](): Eff[R, String] = ask[R, String].map(text => text.toUpperCase)

  // tell adds value to Writer
  def returnWriter[R: _writer](): Eff[R, Unit] = tell("some message")

  // 3. Compose program.
  // Eff[R, A] - R - set of effects, A - value returned by computation
  val program: Eff[AppStack, Article] = for {
    usr1 <- returnFuture[AppStack]()
    usr2 <- returnEither[AppStack]()

    text <- returnReader[AppStack]()
    title <- returnException[AppStack]()

    // if not valid errors are accumulated and next statement is executed
    content <- returnValidateValue[AppStack](text)
    _ <- returnValidate[AppStack](title)
    _ <- returnValidate[AppStack](content)
    _ <- returnWriter[AppStack]()

    // won't be executed if validations failed
    article <- returnOption[AppStack](title, content, Seq(usr1, usr2))
  } yield article

  // 4. Interpret all effects, future has to be interpreted as last effect
  // Order in which effects are interpreted is important.
  val result1: Future[Either[Error, Either[NonEmptyList[String], Option[Either[String, Article]]]]] = program
    .runEither[String]
    .runOption
    .runNel
    .runError
    .runReader("This is a great article.")
    .runWriterUnsafe[String](s => println(s"log: $s"))
    .runAsync

  val result2: Future[Either[Error, Either[NonEmptyList[String], Either[String, Option[Article]]]]] = program
    .runOption
    .runEither[String]
    .runNel
    .runError
    .runReader("This is a great article.")
    .runWriterUnsafe[String](s => println(s"log: $s"))
    .runAsync

  println(Await.result(result1, Duration.Inf))
  println(Await.result(result2, Duration.Inf))
}
