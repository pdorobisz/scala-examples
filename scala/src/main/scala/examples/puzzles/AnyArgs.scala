package examples.puzzles

/*
When there's argument of type Any Scala does "auto-tupling"
 */
object AnyArgs extends App {

  // empty argument list - argument x is of type BoxedUnit
  m()

  // single argument - argument x is of type Int
  m(1)

  // multiple arguments - argument x is treated as a Tuple
  m(1, "a")

  def m(x: Any) = {
    println("got: " + x + ", class: " + x.getClass)
  }
}
