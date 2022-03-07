package boardgames.sevenwonders

import boardgames.shared.*
import cats.data.NonEmptyList
import cats.implicits.*
import scala.concurrent.*
import ObjectStores.*
import scala.concurrent.ExecutionContext.Implicits.global

object GamesRepositoryImpl extends GamesRepository[Game]:
  import Database.{*, given}
  import dev.guillaumebogard.idb.api
  import dev.guillaumebogard.idb.api.*

  def getGame(id: GameId): Future[Option[Game]] =
    val tx = sevenWondersGames.get(id.toKey)
    db.flatMap(_.readOnly(NonEmptyList.of(sevenWondersGames.name))(tx)).logErrors

  def listGames: Future[List[Game]] =
    val tx = sevenWondersGames.getAll().map(_.toList.sorted)
    db.flatMap(_.readOnly(NonEmptyList.of(sevenWondersGames.name))(tx)).logErrors

  def getLastGame: Future[Option[Game]] =
    listGames.map(_.sorted.headOption).logErrors

  def upsertGame(game: Game): Future[Game] =
    db.flatMap(
      _.readWrite(NonEmptyList.of(sevenWondersGames.name))(
        sevenWondersGames.put(game) as game
      ).logErrors
    )

  def finishGame(gameId: GameId) =
    val tx = sevenWondersGames
      .get(gameId.toKey)
      .flatMap(_.traverse_(game => sevenWondersGames.put(game.copy(state = GameState.Finished))))
    db.flatMap(_.readWrite(NonEmptyList.of(sevenWondersGames.name))(tx))

end GamesRepositoryImpl
