package boardgames

enum Game:
  case SevenWonders

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

    case class Well():
      def isComplete: Boolean = ???
    case class Baptistery():
      def isComplete: Boolean = ???
    case class Delegation():
      def isComplete: Boolean = ???
    case class Harbour():
      def isComplete: Boolean = ???
    opaque type Citizens = Int

    extension (citizens: Citizens) def isComplete: Boolean = ???
