package pdorobisz.examples.catseffect.mtl

import cats.data.*
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.*
import cats.mtl.*
import cats.{Monad, MonadError}

object MtlExample2 extends IOApp:
  case class InvalidInputError(value: Int)

  case class Result(value: Int)

  type Stack[A] = WriterT[[Y] =>> ReaderT[[X] =>> EitherT[IO, InvalidInputError, X], Int, Y], Chain[String], A]

  def program[F[_] : Monad](using A: Ask[F, Int], R: MonadError[F, InvalidInputError], T: Tell[F, Chain[String]]): F[Result] =
    for
      x <- A.ask
      _ <- R.raiseError(InvalidInputError(x)).whenA(x < 1)
      (x1, x2, x3) = (x - 1, x, x + 1)
      _ <- T.tell(Chain.one(s"adding value $x1"))
      _ <- T.tell(Chain.one(s"adding value $x2"))
      _ <- T.tell(Chain.one(s"adding value $x3"))
    yield Result(x1 + x2 + x3)

  override def run(args: List[String]): IO[ExitCode] =
    val materializedProgram = program[Stack]
    materializedProgram.run(10).value.flatMap(IO.println).as(ExitCode.Success)
