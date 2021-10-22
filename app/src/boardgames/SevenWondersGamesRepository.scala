package boardgames

import scala.concurrent.*
import dev.guillaumebogard.idb.api.*
import SevenWonders.*
import cats.data.NonEmptyList
import cats.implicits.*

trait SevenWondersGamesRepository[F[_]]:

  def listGames: F[List[Game]]

  def getLastGame: F[Option[Game]]

  def upsertGame(game: Game): F[Game]

  def removeGame(id: GameId): F[Unit]

end SevenWondersGamesRepository

class SevenWondersGamesRepositoryImpl(db: Future[Database[Future]])(using
    ExecutionContext
) extends SevenWondersGamesRepository[Future]:
  import SevenWondersGamesRepositoryImpl.*

  def listGames: Future[List[Game]] =
    val tx = games.getAll().map(_.toList.sorted)
    db.flatMap(_.readOnly(NonEmptyList.of(games.name))(tx))

  def getLastGame: Future[Option[Game]] =
    listGames.map(_.sorted.headOption)

  def upsertGame(game: Game): Future[Game] =
    db.flatMap(
      _.readWrite(NonEmptyList.of(games.name))(games.put(game) as game)
    )

  def removeGame(id: GameId): Future[Unit] =
    db.flatMap(_.readWrite(NonEmptyList.of(games.name))(???))

end SevenWondersGamesRepositoryImpl

object SevenWondersGamesRepositoryImpl:
  val games =
    ObjectStore.withInlineKeys[Game]("games", KeyPath.Identifier("id"))
