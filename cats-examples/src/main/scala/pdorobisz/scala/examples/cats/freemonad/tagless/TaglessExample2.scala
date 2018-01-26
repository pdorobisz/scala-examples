package pdorobisz.scala.examples.cats.freemonad.tagless

import cats.implicits._
import cats.{Id, Monad}
import pdorobisz.scala.examples.cats.freemonad.util.Util._

import scala.language.higherKinds

/**
  * Composing algebras
  */
object TaglessExample2 extends App {

  case class Article(authorId: Int, title: String, content: String)

  // Algebra definitions
  sealed trait ArticlesAlgebra[F[_]] {

    def save(a: Article): F[Unit]

    def publish(a: Article): F[Article]
  }

  sealed trait FollowersAlgebra[F[_]] {

    def getFollowedUsers(id: Int): F[List[Int]]

    def getFollowedOrganisations(id: Int): F[List[Int]]
  }

  sealed trait NotificationsAlgebra[F[_]] {

    def notifyUsers(ids: List[Int], msg: String): F[Unit]

    def notifyOrganisations(ids: List[Int], msg: String): F[Unit]
  }

  // Interpreters

  val articlesInterpreter = new ArticlesAlgebra[Id] {

    override def save(a: Article): Id[Unit] = log(s"save article $a")

    override def publish(a: Article): Id[Article] = {
      log(s"publish article $a")
      a
    }
  }

  val followersInterpreter = new FollowersAlgebra[Id] {
    override def getFollowedUsers(id: Int): Id[List[Int]] = {
      log(s"get followed users for $id")
      List(id + 1, id + 2, id + 3)
    }

    override def getFollowedOrganisations(id: Int): Id[List[Int]] = {
      log(s"get followed organisations for $id")
      List(id + 11, id + 12, id + 13)
    }
  }

  val notificationsInterpreter = new NotificationsAlgebra[Id] {

    override def notifyUsers(ids: List[Int], msg: String): Id[Unit] = log(s"notify users $ids, message: $msg")

    override def notifyOrganisations(ids: List[Int], msg: String): Id[Unit] = log(s"notify organisations $ids, message: $msg")
  }

  // Program
  class PublishArticleProgram[F[_] : Monad](aAlg: ArticlesAlgebra[F], fAlg: FollowersAlgebra[F], nAlg: NotificationsAlgebra[F]) {
    def run(a: Article) = for {
      _ <- aAlg.save(a)
      _ <- aAlg.publish(a)
      followedUsers <- fAlg.getFollowedUsers(a.authorId)
      followedOrganisations <- fAlg.getFollowedOrganisations(a.authorId)
      _ <- nAlg.notifyUsers(followedUsers, s"article '${a.title}' published")
      _ <- nAlg.notifyOrganisations(followedOrganisations, s"article '${a.title}' published")
    } yield ()
  }

  // run
  val result = new PublishArticleProgram(articlesInterpreter, followersInterpreter, notificationsInterpreter)
    .run(Article(456, "test article", "some content"))
  println(result)

}
