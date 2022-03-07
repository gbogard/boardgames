package boardgames.shared

import scala.concurrent.Future

trait GamesRepository[Game]:
  def getGame(id: GameId): Future[Option[Game]]
  def listGames: Future[List[Game]]
  def getLastGame: Future[Option[Game]]
  def upsertGame(game: Game): Future[Game]
  def finishGame(id: GameId): Future[Unit]
