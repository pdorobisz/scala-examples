package examples

import scala.language.implicitConversions

/**
 * Values can be defined either in lexical scope or in implicit scope.
 * Lexical scope - an implicit value is defined in current scope or in enclosing scope.
 * Implicit scope - implicits defined in type's companion object. When implicit of type T is required scope includes T's
 * companion object. If F[T] implicit is required then scope includes companion objects for F and T. Scope also includes
 * bases classes of F and T
 */
object ImplicitsResolution extends App {

  // Implicits defined in lexical scope
  def lexicalScopeExample() = {
    implicit val s: String = "hello"

    object Test {
      implicit val i: Int = 23
    }
    def greet(name: String)(implicit s: String, i: Int) = println(s"$s $name, $i")

    // compiler finds implicit String defined in the current scope and imported implicit Int and uses them
    import Test.i
    greet("John")
  }

  // Implicits defined in Implicit scope

  def implicitScopeExample1(): Unit = {
    // Using companion object of parameter's type
    class A(val n: Int)
    object A {
      implicit def toInt(a: A) = a.n
    }
    def plus(x: Int, y: Int) = println(x + y)

    // compiler looks into A's companion object for implicit conversion from A to Int
    plus(1, new A(2))
  }

  def implicitScopeExample2(): Unit = {
    // Using implicit type's companion object
    class A[T](val n: T)
    object A {
      implicit val defaultIntA = new A(42)
    }

    def test[T](i: T)(implicit a: A[T]) = println(s"$i: ${a.n}")

    // implicit value of A[Int] is required and compiler finds it in A's companion object
    test(1)
    // this doesn't compile as there's no implicit A[String] defined in the implicit scope
    // test("a")
  }

  def implicitScopeExample3(): Unit = {
    // Using implicit type argument's companion object
    class A(val n: Int)
    object A {
      implicit val ord = new Ordering[A] {
        override def compare(x: A, y: A): Int = implicitly[Ordering[Int]].compare(x.n, y.n)
      }
    }
    // sorted needs implicit Ordering[A] which can be found in A's companion object (A is a type argument for Ordering)
    val sorted = List(new A(5), new A(2)).sorted
    println(sorted.map(_.n))
  }

  lexicalScopeExample()
  implicitScopeExample1()
  implicitScopeExample2()
  implicitScopeExample3()
}
