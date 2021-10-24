package boardgames.orleans

import dev.guillaumebogard.idb.api.*

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

  case class Well(scholar: Boolean, technology: Boolean) derives ObjectEncoder, Decoder:
    def isComplete: Boolean = scholar && technology
  case class Baptistery(monk: Boolean, brocade: Boolean) derives ObjectEncoder, Decoder:
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
