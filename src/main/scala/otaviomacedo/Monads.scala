package otaviomacedo

import scala.concurrent.{ExecutionContext, Future}
import scalaz.Monad

object Monads {
  implicit def futureMonad(implicit ctx: ExecutionContext) = new Monad[Future] {
    override def point[A](a: => A): Future[A] =
      Future(a)

    override def bind[A, B](fa: Future[A])(f: A => Future[B]): Future[B] =
      fa.flatMap(f)
  }
}
