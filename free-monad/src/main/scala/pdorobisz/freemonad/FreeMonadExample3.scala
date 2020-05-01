package pdorobisz.freemonad

import cats._
import cats.data._
import cats.free.Free
import pdorobisz.freemonad.Util._

import scala.language.higherKinds

/**
 * Composing different algebras.
 */
object FreeMonadExample3 extends App {

  case class Article(authorId: Int, title: String, content: String)

  object Articles {

    sealed trait ArticlesAlgebra[T]

    case class Save(a: Article) extends ArticlesAlgebra[Unit]

    case class Publish(a: Article) extends ArticlesAlgebra[Article]

    class ArticlesI[F[_]](implicit I: InjectK[ArticlesAlgebra, F]) {
      // Now we have to use Free.inject instead of Free.liftF. Internally Free.inject converts ArticlesAlgebra to F (composed algebra)
      // using InjectK and then lifts (Free.liftF) it to Free.
      // InjectK[F, G] - transforms monad F to monad G
      def save(a: Article): Free[F, Unit] = Free.inject[ArticlesAlgebra, F](Save(a))

      def publish(a: Article): Free[F, Article] = Free.inject[ArticlesAlgebra, F](Publish(a))
    }

    object ArticlesI {
      implicit def articlesI[F[_]](implicit I: InjectK[ArticlesAlgebra, F]): ArticlesI[F] = new ArticlesI[F]()
    }

    object ArticlesInterpreter extends (ArticlesAlgebra ~> Id) {
      override def apply[A](fa: ArticlesAlgebra[A]): Id[A] = fa match {
        case Save(a) =>
          log(s"save article $a").asInstanceOf[A]
        case Publish(a) =>
          log(s"publish article $a").asInstanceOf[A]
      }
    }

  }

  object Followers {

    sealed trait FollowersAlgebra[T]

    case class GetFollowedUsers(id: Int) extends FollowersAlgebra[List[Int]]

    case class GetFollowedOrganisations(id: Int) extends FollowersAlgebra[List[Int]]

    class FollowersI[F[_]](implicit I: InjectK[FollowersAlgebra, F]) {
      def getFollowedUsers(id: Int): Free[F, List[Int]] = Free.inject[FollowersAlgebra, F](GetFollowedUsers(id))

      def getFollowedOrganisations(id: Int): Free[F, List[Int]] = Free.inject[FollowersAlgebra, F](GetFollowedOrganisations(id))
    }

    object FollowersI {
      implicit def followersI[F[_]](implicit I: InjectK[FollowersAlgebra, F]): FollowersI[F] = new FollowersI[F]()
    }

    object FollowersInterpreter extends (FollowersAlgebra ~> Id) {
      override def apply[A](fa: FollowersAlgebra[A]): Id[A] = fa match {
        case GetFollowedUsers(id) =>
          log(s"get followed users for $id")
          List(id + 1, id + 2, id + 3).asInstanceOf[A]
        case GetFollowedOrganisations(id) =>
          log(s"get followed organisations for $id")
          List(id + 11, id + 12, id + 13).asInstanceOf[A]
      }
    }

  }

  object Notifications {

    sealed trait NotificationsAlgebra[T]

    case class NotifyUsers(ids: List[Int], msg: String) extends NotificationsAlgebra[Unit]

    case class NotifyOrganisations(ids: List[Int], msg: String) extends NotificationsAlgebra[Unit]

    class NotificationsI[F[_]](implicit I: InjectK[NotificationsAlgebra, F]) {
      def notifyUsers(ids: List[Int], msg: String): Free[F, Unit] = Free.inject[NotificationsAlgebra, F](NotifyUsers(ids, msg))

      def notifyOrganisations(ids: List[Int], msg: String): Free[F, Unit] = Free.inject[NotificationsAlgebra, F](NotifyOrganisations(ids, msg))
    }

    object NotificationsI {
      implicit def notificationsI[F[_]](implicit I: InjectK[NotificationsAlgebra, F]): NotificationsI[F] = new NotificationsI[F]()
    }

    object NotificationsInterpreter extends (NotificationsAlgebra ~> Id) {
      override def apply[A](fa: NotificationsAlgebra[A]): Id[A] = fa match {
        case NotifyUsers(ids, msg) => log(s"notify users $ids, message: $msg").asInstanceOf[A]
        case NotifyOrganisations(ids, msg) => log(s"notify organisations $ids, message: $msg").asInstanceOf[A]
      }
    }

  }

  // Compose 2 algebras

  type FollowersNotificationsApp[T] = EitherK[Followers.FollowersAlgebra, Notifications.NotificationsAlgebra, T]

  val composedInterpreter: FollowersNotificationsApp ~> Id = Followers.FollowersInterpreter or Notifications.NotificationsInterpreter

  // Program using composed algebras
  def program(userId: Int)(implicit
                           F: Followers.FollowersI[FollowersNotificationsApp],
                           N: Notifications.NotificationsI[FollowersNotificationsApp]): Free[FollowersNotificationsApp, Unit] = {

    import F._
    import N._

    for {
      followedUsers <- getFollowedUsers(userId)
      followedOrganisations <- getFollowedOrganisations(userId)
      _ <- notifyUsers(followedUsers, "to all users")
      _ <- notifyOrganisations(followedOrganisations, "to all organisations")
    } yield ()
  }

  // Run program
  val result: Unit = program(123).foldMap(composedInterpreter)

  // Compose 3 algebras

  type ArticlesFollowersNotificationsApp[T] = EitherK[Articles.ArticlesAlgebra, FollowersNotificationsApp, T]

  val composedInterpreter2: ArticlesFollowersNotificationsApp ~> Id = Articles.ArticlesInterpreter or composedInterpreter

  // Program using composed algebras
  def program2(a: Article)(implicit
                           A: Articles.ArticlesI[ArticlesFollowersNotificationsApp],
                           F: Followers.FollowersI[ArticlesFollowersNotificationsApp],
                           N: Notifications.NotificationsI[ArticlesFollowersNotificationsApp]): Free[ArticlesFollowersNotificationsApp, Unit] = {

    import A._
    import F._
    import N._

    for {
      _ <- save(a)
      _ <- publish(a)
      followedUsers <- getFollowedUsers(a.authorId)
      followedOrganisations <- getFollowedOrganisations(a.authorId)
      _ <- notifyUsers(followedUsers, s"article '${a.title}' published")
      _ <- notifyOrganisations(followedOrganisations, s"article '${a.title}' published")
    } yield ()
  }

  // Run program (not working because of some problems with implicits)
  //  val result2: Unit = program2(Article(456, "test article", "some content")).foldMap(composedInterpreter2)

}
