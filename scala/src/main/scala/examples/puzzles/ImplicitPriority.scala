package examples.puzzles

object ImplicitPriority extends App {

  case class Card(n: Int) {
    def description = s"card nr $n"
  }

  // Implicit defined by val or var has higher priority then implicit defined by def.
  // It won't compile when implicit is defined by both var and val.
  implicit val intToCardFunc = (n: Int) => Card(n + 10)

  // it wouldn't compile if this weren't commented out
  //  implicit var intToCardFunc2 = (n: Int) => Card(n + 12)

  implicit def intToCard(n: Int): Card = Card(n)

  println(2.description) // prints 12 (intToCardFunc is used because it's a val)

}
