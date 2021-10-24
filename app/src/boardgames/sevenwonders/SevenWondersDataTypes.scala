package boardgames.sevenwonders

import boardgames.shared.{given, *}
import dev.guillaumebogard.idb.time.given
import dev.guillaumebogard.idb.api.*

final case class PlayerState(
    player: Player,
    military: Int = 0,
    treasury: Int = 0,
    wonder: Int = 0,
    civilianStructures: Int = 0,
    science: ScientificScore = ScientificScore(),
    commerce: Int = 0,
    guilds: Int = 0,
    cityCards: Int = 0,
    leaders: Int = 0
) derives ObjectEncoder,
      Decoder:
  def totalScore: Int =
    military +
      treasury +
      wonder +
      civilianStructures +
      science.score +
      commerce +
      guilds +
      cityCards +
      leaders
end PlayerState

given Ordering[PlayerState] with
  val scoreOrdering = summon[Ordering[Int]]
  def compare(x: PlayerState, y: PlayerState): Int =
    scoreOrdering.compare(x.totalScore, y.totalScore)

final case class ScientificScore(
    tabletSymbols: Int = 0,
    compassSymbols: Int = 0,
    cogSymbols: Int = 0
) derives ObjectEncoder,
      Decoder:

  private def tabletScore = tabletSymbols * tabletSymbols
  private def compassScore = compassSymbols * compassSymbols
  private def cogScore = cogSymbols * cogSymbols
  private def allSymbols = List(tabletSymbols, compassSymbols, cogSymbols)
  private def groupsScore =
    (1 to allSymbols.max).foldLeft(0)({
      case (score, symbolCount) if allSymbols.forall(_ >= symbolCount) =>
        score + 1
      case (score, _) => score
    })
  def score: Int = groupsScore + tabletScore + compassScore + cogScore
end ScientificScore

final case class Game(
    id: GameId = GameId.fromNow(),
    createdAt: java.time.Instant = java.time.Instant.now(),
    state: GameState = GameState.Pending,
    players: Map[PlayerId, PlayerState] = Map.empty
) derives ObjectEncoder,
      Decoder:
  def winner: Option[PlayerState] = players.values.maxOption

  def addPlayer(player: Player): Game =
    copy(players = players + ((player.id, PlayerState(player))))

  def removePlayer(player: Player): Game =
    copy(players = players - player.id)
end Game

object Game:
  def apply(players: List[Player]): Game =
    Game(players = players.map(p => (p.id, PlayerState(p))).toMap)

given Ordering[Game] with
  val instantOrdering = summon[Ordering[java.time.Instant]].reverse
  def compare(x: Game, y: Game) =
    instantOrdering.compare(x.createdAt, y.createdAt)
