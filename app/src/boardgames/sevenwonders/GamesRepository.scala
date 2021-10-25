package boardgames.sevenwonders

import boardgames.shared.*
import cats.data.NonEmptyList
import cats.implicits.*
import scala.concurrent.*
import ObjectStores.*

trait GamesRepository:

  def listGames: Future[List[Game]]

  def getLastGame: Future[Option[Game]]

  def upsertGame(game: Game): Future[Game]

end GamesRepository

object GamesRepositoryImpl extends GamesRepository:
  import Database.{*, given}
  import dev.guillaumebogard.idb.api

  def listGames: Future[List[Game]] =
    val tx = sevenWondersGames.getAll().map(_.toList.sorted)
    db.flatMap(_.readOnly(NonEmptyList.of(sevenWondersGames.name))(tx)).logErrors

  def getLastGame: Future[Option[Game]] =
    listGames.map(_.sorted.headOption).logErrors

  def upsertGame(game: Game): Future[Game] =

    scala.scalajs.js.Dynamic.global.console.log(
      api.ObjectEncoder[Game].encode(game)
    )
    db.flatMap(
      _.readWrite(NonEmptyList.of(sevenWondersGames.name))(
        sevenWondersGames.put(game) as game
      ).logErrors
    )

end GamesRepositoryImpl
