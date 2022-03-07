package boardgames.shared

import boardgames.shared.*

object Routes:
  opaque type Route = String

  val home: Route = "/"
  val sevenWondersLastGames: Route = "/7wonders"
  val sevenWondersNewGame: Route = "/7wonders/new"
  def sevenWondersGame(id: GameId): Route = s"/7wonders/$id"

  val hallertauLastGames: Route = "/hallertau"
