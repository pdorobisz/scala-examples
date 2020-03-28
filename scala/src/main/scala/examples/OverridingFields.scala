package examples

// https://docs.scala-lang.org/tutorials/FAQ/initialization-order.html
object OverridingFields {

  /* Strict vals (non-lazy) are initialized in superclasses first and then in subclasses. When val is overridden it can't be initialized more than once so it
  will appear null to superclass. */

  abstract class A {
    val x1: String // null, it will be initialized in B
    val x2: String = "A.x2" // x2 remains null because C overrides it
    println("A: " + x1 + ", " + x2)
  }

  class B extends A {
    val x1: String = "B.x1" // x1 is initialized here, x2 is still null
    println("B: " + x1 + ", " + x2)
  }

  class C extends B {
    override val x2: String = "C.x2" // x2 is initialized here
    println("C: " + x1 + ", " + x2)
  }

  ///// Solutions

  // 1. Lazy vals

  abstract class A1 {
    val x1: String // initialized by B1 because B1 implements it as a lazy val
    lazy val x2: String = "A.x2" // values from C1 used as C1 overrides x2 (lazy val) with lazy val
    println("A: " + x1 + ", " + x2)
  }

  class B1 extends A1 {
    lazy val x1: String = "B.x1" // x1 (abstract strict) is implemented as a lazy val so this value will be visible to parent class
    println("B: " + x1 + ", " + x2)
  }

  class C1 extends B1 {
    override lazy val x2: String = "C.x2" // lazy val can be overridden only by lazy val
    println("C: " + x1 + ", " + x2)
  }

  // 2. Early definitions/initializers
  // Early initializer runs before superclass constructor

  abstract class A2 {
    val x1: String
    val x2: String = "A.x2"

    println("A: " + x1 + ", " + x2)
  }

  class B2 extends {
    val x1: String = "B.x1" // x1 is initialized before A2 initialization starts
  } with A2 {
    println("B: " + x1 + ", " + x2)
  }

  class C2 extends {
    override val x2: String = "C.x2" // x2 is defined before B2 and A2 initialization
  } with B2 {
    println("C: " + x1 + ", " + x2)
  }

  def main(args: Array[String]): Unit = {
    /* prints:
    A: null, null
    B: B.x1, null
    C: B.x1, C.x2 */
    new C

    println("\n==solution1==")
    new C1

    println("\n==solution2==")
    new C2
  }
}
