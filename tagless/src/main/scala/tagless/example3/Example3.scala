package tagless.example3

import cats.implicits._
import cats.{Id, Monad}

// Extending algebra without changing already existing code.
object Example3 extends App {

  // Some operations are defined here
  object Algebra1 {

    trait Exp[F[_]] {
      def neg(x: Int): F[Int]

      def add(x: Int, y: Int): F[Int]
    }

    object ExpSyntax {
      def neg[F[_]](x: Int)(implicit e: Exp[F]): F[Int] = e.neg(x)

      def add[F[_]](x: Int, y: Int)(implicit e: Exp[F]): F[Int] = e.add(x, y)
    }

    implicit val evalExp: Exp[Id] = new Exp[Id] {
      override def neg(x: Int): Id[Int] = -x

      override def add(x: Int, y: Int): Id[Int] = x + y
    }

  }

  // We want to extend Algebra1 by adding some more operations
  object Algebra2 {

    trait Mult[F[_]] {
      def mul(x: Int, y: Int): F[Int]
    }

    object MultSyntax {
      def mul[F[_]](x: Int, y: Int)(implicit m: Mult[F]): F[Int] = m.mul(x, y)
    }

    implicit val evalMult: Mult[Id] = new Mult[Id] {
      override def mul(x: Int, y: Int): Id[Int] = x * y
    }

  }

  import Algebra1.Exp
  import Algebra1.ExpSyntax._
  import Algebra2.Mult
  import Algebra2.MultSyntax._

  // Program that uses operations from both algebras - we have to specify them as constraints for the program
  def program[F[_] : Exp : Mult : Monad](a: Int, b: Int, c: Int): F[Int] = for {
    squareB <- mul(b, b)
    ac <- mul(a, c)
    ac4 <- mul(4, ac)
    minusAc4 <- neg(ac4)
    delta <- add(squareB, minusAc4)
  } yield delta

  val result: Id[Int] = program[Id](3, 4, 1)
  println(result)
}
