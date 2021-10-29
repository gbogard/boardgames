package boardgames.shared

import boardgames.sevenwonders.*
import dev.guillaumebogard.idb.api

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.Failure
import scala.util.Success

object ObjectStores:

  val sevenWondersGames =
    api.ObjectStore.withInlineKeys[Game]("games", api.KeyPath.Identifier("id"))

object Database:
  given ExecutionContext = ExecutionContext.global
  val db: Future[api.Database[Future]] =
    api.Database.open[Future](
      api.Database.Name("boardgames"),
      api
        .Schema()
        .createObjectStore(ObjectStores.sevenWondersGames)
    )

  extension [T](f: Future[T])
    def logErrors: Future[T] =
      f.onComplete {
        case Success(value) => ()
        case Failure(err)   => js.Dynamic.global.console.error(err.asInstanceOf)
      }
      f
