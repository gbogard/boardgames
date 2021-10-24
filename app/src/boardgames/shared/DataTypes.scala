package boardgames.shared

import java.time.Instant
import scala.util.Random
import dev.guillaumebogard.idb.api.*
import dev.guillaumebogard.idb.time.given
import cats.implicits.*

final case class Player(name: String, color: Color) derives ObjectEncoder, Decoder:
  val id: PlayerId = PlayerId(name)

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

opaque type GameId = Double

object GameId:
  def fromNow(): GameId = Instant.now().toEpochMilli.toDouble
  given Encoder[GameId] = Encoder.double
  given Decoder[GameId] = Decoder.double

opaque type PlayerId = String

extension (playerId: PlayerId) def toString: String = playerId

object PlayerId:
  def apply(playerName: String): PlayerId = playerName.toLowerCase

  given Encoder[PlayerId] = Encoder.string
  given Decoder[PlayerId] = Decoder.string
  given ObjectKeyEncoder[PlayerId] = ObjectKeyEncoder.string
  given ObjectKeyDecoder[PlayerId] = ObjectKeyDecoder.string
