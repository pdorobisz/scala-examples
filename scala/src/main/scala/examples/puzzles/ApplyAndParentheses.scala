package examples.puzzles

/*
Using parentheses when calling method without argument list can change the result.
 */
object ApplyAndParentheses extends App {

  // m has no argument list so this calls apply on value returned by m.
  // Same as: (new A().m).apply // new A().m returns value and we call apply on it, () is not used to call m itself
  new A().m()

  // If m has empty argument list semantics is different - now we're calling m method only, apply isn't called
  new B().m()


  class A {
    // no argument list
    def m: A = {
      println("called A.m")
      this
    }

    def apply(): Unit = {
      println("called A.apply")
    }
  }

  class B {
    // empty argument list
    def m(): B = {
      println("called B.m")
      this
    }

    def apply(): Unit = {
      println("called B.apply")
    }
  }

}
