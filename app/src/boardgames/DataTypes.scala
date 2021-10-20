package boardgames

import java.time.Instant
import scala.util.Random

final case class Player(name: String, color: Color)

enum Color(val hex: Int):
  case Green extends Color(???)
  case Yellow extends Color(???)
  case Blue extends Color(???)
  case Red extends Color(???)
  case Purple extends Color(???)
  case Teal extends Color(???)
  case Black extends Color(???)
  case Orange extends Color(???)

enum GameState:
  case Pending
  case Finished

enum GameType:
  case SevenWonders

opaque type GameId = Long
opaque type PlayerId = String

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
  ):
    def isComplete: Boolean =
      well.isComplete &&
        delegation.isComplete &&
        harbour.isComplete &&
        church &&
        citizens.isComplete &&
        goods

  object Tasks:

    case class Well(scholar: Boolean, technology: Boolean):
      def isComplete: Boolean = scholar && technology
    case class Baptistery(monk: Boolean, brocade: Boolean):
      def isComplete: Boolean = monk && brocade
    case class Delegation(
        farmer: Boolean,
        trader: Boolean,
        firstKnight: Boolean,
        secondKnight: Boolean
    ):
      def isComplete: Boolean = farmer && trader && firstKnight && secondKnight
    case class Harbour(
        fisherman: Boolean,
        craftsman: Boolean,
        firstWood: Boolean,
        secondWood: Boolean,
        fiveGold: Boolean
    ):
      def isComplete: Boolean =
        fisherman && craftsman && firstWood && secondWood && fiveGold
    opaque type Citizens = Int

    extension (citizens: Citizens)
      def goal: Citizens = 20
      def isComplete: Boolean = citizens == goal
      def addCitizen: Citizens = Math.min(goal, citizens + 1)
      def removeCitizen: Citizens = Math.max(0, citizens - 1)

object SevenWonders:
  final case class Game(
      id: GameId,
      state: GameState,
      players: Map[PlayerId, PlayerState]
  )

  final case class PlayerState(
      player: Player
  )
