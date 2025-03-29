package pdorobisz.examples.catseffect.mtl

import cats.data.{EitherT, ReaderT}
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.*
import cats.mtl.*
import cats.{Monad, MonadError}

/* Cats MTL makes it easier to work with composed monad transformers. It provides type classes encoding the effects
 of most common monad transformers.
*/
object MtlExample1 extends IOApp:
  case class InvalidInputError(value: Int)

  case class Result(value: Int)

  // same transformers but composed in different order
  type Stack1[A] = ReaderT[[X] =>> EitherT[IO, InvalidInputError, X], Int, A]
  type Stack2[A] = EitherT[[X] =>> ReaderT[IO, Int, X], InvalidInputError, A]

  // Ask - type class representing ReaderT
  def program[F[_] : Monad](using A: Ask[F, Int], R: MonadError[F, InvalidInputError]): F[Result] =
    for
      x <- A.ask
      _ <- R.raiseError(InvalidInputError(x)).whenA(x < 1)
      (x1, x2, x3) = (x - 1, x, x + 2)
    yield Result(x1 + x2 + x3)

  override def run(args: List[String]): IO[ExitCode] =
    // we can use different stacks to materialize same program
    val materializedProgram1 = program[Stack1]
    val materializedProgram2 = program[Stack2]

    for
      _ <- materializedProgram1.run(0).value.flatMap(IO.println)
      _ <- materializedProgram2.value.run(0).flatMap(IO.println)
    yield ExitCode.Success
