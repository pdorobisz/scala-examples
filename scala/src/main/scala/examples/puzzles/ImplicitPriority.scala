package examples.puzzles

object ImplicitPriority extends App {

  case class Card(n: Int) {
    def description = s"card nr $n"
  }

  // Implicit definiowany przez val (lub var) ma priorytet przed implicit definiowanym przez def.
  // Nie skompiluje sie gdy bedzie implicit val i implicit var.
  implicit val intToCardFunc = (n: Int) => Card(n + 10)

  //  implicit var intToCardFunc2 = (n: Int) => Card(n + 12)

  implicit def intToCard(n: Int): Card = Card(n)

  println(2.description) // wypisze 12 (uzyte zostanie intToCardFunc)

}
