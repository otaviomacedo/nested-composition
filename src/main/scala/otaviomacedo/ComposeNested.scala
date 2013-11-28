package otaviomacedo

import scala.concurrent.ExecutionContext.Implicits.global
import scalaz._
import Scalaz._
import scala.language.higherKinds
import Monads.futureMonad
import ComposeNested.apply
import otaviomacedo.Api._
import scala.languageFeature.postfixOps

class ComposeNested[A, B, F[_], G[_]](f: A => F[G[B]])
                                     (implicit fMonad: Monad[F], gMonad: Monad[G], gTraverse: Traverse[G]) {

  def ->>[C](g: B => F[G[C]]): A => F[G[C]] = composeN(f, g)

  private def composeN[C](f: A => F[G[B]], g: B => F[G[C]]): A => F[G[C]] = {
    a => fMonad.bind(f(a)) {
      gb => fMonad.map(fMonad.sequence(gMonad.map(gb)(g))) {
        ggc => gMonad.bind(ggc)(identity)
      }
    }
  }
}

object ComposeNested {
  implicit def apply[A, B, F[_], G[_]](f: A => F[G[B]])
                                      (implicit fMonad: Monad[F], gMonad: Monad[G], gTraverse: Traverse[G]) =
    new ComposeNested(f)
}

object Demo extends App {
  val usersWeather = getAddress ->> getGeolocation ->> getCurrentWeather

  val futureWeather = usersWeather(User("John Doe"))

  futureWeather onFailure {
    case e: Exception =>
      println(s"Computation failed with the message: ${e.getMessage}")
  }

  futureWeather onSuccess {
    case result => println(s"Computation result: $result")
  }

  Thread.sleep(500)
}