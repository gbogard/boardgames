package boardgames.shared

import cats.Monad

enum ExternalEntity[+T]:
  case Loading
  case NotFound
  case Loaded(data: T) extends ExternalEntity[T]

object ExternalEntity:
  extension [A](opt: Option[A])
    def loadedOrNotFound: ExternalEntity[A] = opt.fold(NotFound)(Loaded(_))

  given Monad[ExternalEntity] with
    def pure[A](a: A): ExternalEntity[A] = Loaded(a)

    def flatMap[A, B](fa: ExternalEntity[A])(f: A => ExternalEntity[B]): ExternalEntity[B] =
      fa match
        case Loaded(data) => f(data)
        case Loading      => Loading
        case NotFound     => NotFound

    @annotation.tailrec
    def tailRecM[A, B](a: A)(f: A => ExternalEntity[Either[A, B]]): ExternalEntity[B] =
      f(a) match
        case Loaded(Right(b)) => Loaded(b)
        case Loaded(Left(a))  => tailRecM(a)(f)
        case Loading          => Loading
        case NotFound         => NotFound
