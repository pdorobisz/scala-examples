package pdorobisz.eff

import com.esv.eff.domain.{Article, User}
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.future._
import org.atnos.eff.syntax.all._
import org.atnos.eff.syntax.future._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Example2 extends App {

  implicit val scheduler = ExecutorServices.schedulerFromGlobalExecutionContext

  import scala.concurrent.ExecutionContext.Implicits.global

  type _either[R] = MemberIn[Either[String, ?], R]

  type AppStack = Fx.fx3[Option[?], Either[String, ?], TimedFuture[?]]

  // Service with nested effects.
  object Service {
    def getUser(id: Int): Future[Option[User]] = Future(Some(User(1234, "John Smith")))

    def updateArticle(a: Article): Future[Either[String, Article]] = Future(Right(a))
  }

  // Service with methods lifted to Eff monad
  object EffService {
    def getUser[R: _future : _option](id: Int): Eff[R, User] = {
      val result = Service.getUser(id)
      for {
        f <- fromFuture(result)
        o <- fromOption(f)
      } yield o
    }

    def updateArticle[R: _future : _either](a: Article): Eff[R, Article] = {
      val result = Service.updateArticle(a)
      for {
        f <- fromFuture(result)
        e <- fromEither(f)
      } yield e
    }
  }

  val program: Eff[AppStack, Article] = for {
    usr <- EffService.getUser[AppStack](1234)
    article <- EffService.updateArticle[AppStack](Article("Hello World", "Some content", Seq(usr)))
  } yield article

  val result: Future[Option[Either[String, Article]]] = program
    .runEither
    .runOption
    .runAsync

  println(Await.result(result, Duration.Inf))

}
