package examples.puzzles

/*
W przypadku argumentu typu Any Scala wykonuje "auto-tupling"
 */
object AnyArgs extends App {

  // pusta lista argumentow - argument typu BoxedUnit
  m()

  // jeden argument - argument typu Int
  m(1)

  // wiele argumentow - traktowane jako Tuple
  m(1, "a")

  def m(x: Any) = {
    println("got: " + x + ", class: " + x.getClass)
  }
}
