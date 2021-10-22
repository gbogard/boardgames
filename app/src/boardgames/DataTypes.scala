package boardgames

import java.time.Instant
import scala.util.Random
import dev.guillaumebogard.idb.api.*
import dev.guillaumebogard.idb.time.given

final case class Player(name: String, color: Color)
    derives ObjectEncoder,
      Decoder

enum Color(val hex: String) derives ObjectEncoder, Decoder:
  case Green extends Color("#78e08f")
  case Yellow extends Color("#f6b93b")
  case Blue extends Color("#1e3799")
  case Red extends Color("#b71540")
  case Purple extends Color("#8c7ae6")
  case Teal extends Color("#38ada9")
  case Black extends Color("#2f3640")
  case Orange extends Color("#fa983a")

enum GameState derives ObjectEncoder, Decoder:
  case Pending
  case Finished

enum GameType:
  case SevenWonders

opaque type GameId = Long
given Encoder[GameId] = Encoder[Long]
given Decoder[GameId] = Decoder[Long]

opaque type PlayerId = String
given Encoder[PlayerId] = Encoder[String]
given Decoder[PlayerId] = Decoder[String]
given ObjectKeyEncoder[PlayerId] = ObjectKeyEncoder[String]
given ObjectKeyDecoder[PlayerId] = ObjectKeyDecoder[String]

object PlayerId:
  def random(): PlayerId = Random.alphanumeric.take(10).mkString

object GameId:
  def fromNow(): GameId = Instant.now().toEpochMilli

object OrleansStoriesFirstKingdom:

  case class Tasks(
      well: Tasks.Well,
      baptistery: Tasks.Baptistery,
      delegation: Tasks.Delegation,
      harbour: Tasks.Harbour,
      church: Boolean,
      citizens: Tasks.Citizens,
      goods: Boolean
  ) derives ObjectEncoder,
        Decoder:
    def isComplete: Boolean =
      well.isComplete &&
        delegation.isComplete &&
        harbour.isComplete &&
        church &&
        citizens.isComplete &&
        goods
  end Tasks

  object Tasks:

    case class Well(scholar: Boolean, technology: Boolean)
        derives ObjectEncoder,
          Decoder:
      def isComplete: Boolean = scholar && technology
    case class Baptistery(monk: Boolean, brocade: Boolean)
        derives ObjectEncoder,
          Decoder:
      def isComplete: Boolean = monk && brocade
    case class Delegation(
        farmer: Boolean,
        trader: Boolean,
        firstKnight: Boolean,
        secondKnight: Boolean
    ) derives ObjectEncoder,
          Decoder:
      def isComplete: Boolean = farmer && trader && firstKnight && secondKnight
    case class Harbour(
        fisherman: Boolean,
        craftsman: Boolean,
        firstWood: Boolean,
        secondWood: Boolean,
        fiveGold: Boolean
    ) derives ObjectEncoder,
          Decoder:
      def isComplete: Boolean =
        fisherman && craftsman && firstWood && secondWood && fiveGold

    opaque type Citizens = Int
    given Decoder[Citizens] = Decoder[Int]
    given Encoder[Citizens] = Encoder[Int]

    extension (citizens: Citizens)
      def goal: Citizens = 20
      def isComplete: Boolean = citizens == goal
      def addCitizen: Citizens = Math.min(goal, citizens + 1)
      def removeCitizen: Citizens = Math.max(0, citizens - 1)

object SevenWonders:
  final case class PlayerState(
      player: Player,
      military: Int,
      treasury: Int,
      wonder: Int,
      civilianStructures: Int,
      science: ScientificScore,
      commerce: Int,
      guilds: Int,
      cityCards: Int,
      leaders: Int
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
      tabletSymbols: Int,
      compassSymbols: Int,
      cogSymbols: Int
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
      id: GameId,
      createdAt: java.time.Instant,
      state: GameState,
      players: Map[PlayerId, PlayerState]
  ) derives ObjectEncoder,
        Decoder:
    def winner: Option[PlayerState] = players.values.maxOption
  end Game

  given Ordering[Game] with
    val instantOrdering = summon[Ordering[java.time.Instant]].reverse
    def compare(x: Game, y: Game) =
      instantOrdering.compare(x.createdAt, y.createdAt)
