package examples.scalaz

import scalaz.Reader

/**
  * ReaderMonad umozliwia przekazywanie jakiejs wartosci pomiedzy wywolaniami funkcji. Jest to dependency injection
  */
object ScalazReaderMonad {

  case class Context(name: String) {
    def log(msg: String): Unit = println(s"$name: $msg")
  }

  // Reader zawiera funkcje Context => A.
  type Action[A] = Reader[Context, A]

  def action1(x: List[Int]): Action[Int] = Reader((context: Context) => {
    context.log("action1, " + x)
    x.size
  })

  def action2(x: Int): Action[String] = Reader((context: Context) => {
    context.log("action2, " + x)
    s"<$x>"
  })

  def action3(x: String): Action[Option[String]] = Reader((context: Context) => {
    context.log("action3, " + x)
    Some(x)
  })

  def main(args: Array[String]): Unit = {
    // "skladanie" kolejnych akcji pomiedzy ktorymi przekazywany jest Context
    val result = for {
      res1 <- action1(List(1, 2, 3))
      res2 <- action2(res1)
      res3 <- action3(res2)
    } yield res3

    // wywolanie funkcji zawartej w Reader nastepuje dopiero po "wstrzyknieciu" Context poprzez "run"
    result.run(Context("context1"))
    result.run(Context("context2"))
  }
}
