package pdorobisz.freemonad

import java.util.UUID

import cats._
import cats.free.Free
import pdorobisz.freemonad.Util._

import scala.collection.mutable
import scala.concurrent.Future

object FreeMonadExample2 extends App {

  case class Article(id: Int, title: String, content: String)

  // Algebra definition

  sealed trait ArticleAlgebra[T]

  case class Find(id: Int) extends ArticleAlgebra[Option[Article]]

  case class Save(a: Article) extends ArticleAlgebra[Unit]

  case class Validate(a: Article) extends ArticleAlgebra[Either[String, Article]]

  case class Publish(id: Int) extends ArticleAlgebra[Future[Article]]

  // Free type

  type ArticleF[A] = Free[ArticleAlgebra, A]

  // DSL (to map our classes to Free)

  def find(id: Int): ArticleF[Option[Article]] = Free.liftF(Find(id))

  def save(a: Article): ArticleF[Unit] = Free.liftF(Save(a))

  def validate(a: Article): ArticleF[Either[String, Article]] = Free.liftF(Validate(a))

  def publish(id: Int): ArticleF[Future[Article]] = Free.liftF(Publish(id))

  // Interpreter

  val interpreter: ArticleAlgebra ~> Id = new (ArticleAlgebra ~> Id) {

    private val store = mutable.Map.empty[UUID, Any]

    override def apply[A](fa: ArticleAlgebra[A]): Id[A] = fa match {
      case Find(id) =>
        log(s"find $id")
        Some(Article(id, "some article", "hello world")).asInstanceOf[A]
      case Save(a) =>
        log(s"save $a")
        ().asInstanceOf[A]
      case Validate(a) =>
        log(s"validate $a")
        (if (a.title.isEmpty) Left("title is empty") else Right(a)).asInstanceOf[A]
      case Publish(id) =>
        log(s"publish $id")
        Future.successful(Article(id, "some article", "hello world")).asInstanceOf[A]
    }
  }

  // Programs

  def updateTitle(id: Int, title: String): ArticleF[Future[Article]] = for {
    a <- find(id)
    v <- validate(a.get.copy(title = title))
    _ <- save(v.right.get)
    p <- publish(id)
  } yield p

  def saveArticle(a: Article): ArticleF[Unit] = for {
    v <- validate(a)
    s <- save(v.right.get)
  } yield s

  // Run programs

  val result1 = updateTitle(123, "new title").foldMap(interpreter)
  println(result1)

  println("--------")
  val result2 = saveArticle(Article(321, "some article", "hello world")).foldMap(interpreter)
  println(result2)
}
