package boardgames.shared.components.scoring

import boardgames.shared.*
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import scala.util.Try

object ScoreInput:
  type Component[Score] = ScalaComponent[ScoreInput.Props[Score], Unit, Unit, CtorType.Props]
  final case class Props[Score](player: Player, score: Score, updateScore: Score => Callback)

  val int: Component[Int] = ScalaComponent
    .builder[Props[Int]]
    .render_P(props =>
      <.div(
        ^.className := "my-4",
        playerName(props.player),
        <.input(
          ^.tpe := "number",
          ^.step := 1,
          ^.className := "block w-full h-12 px-2",
          ^.onChange ==> ((e: ReactEventFromInput) =>
            Try(e.target.value.toInt).fold(_ => Callback.empty, props.updateScore)
          ),
          ^.defaultValue := props.score
        )
      )
    )
    .build

  def playerName(player: Player) =
    <.div(
      ^.className := "mb-2 px-2 flex items-center",
      <.div(
        ^.className := "h-4 w-4 mr-2 rounded-sm",
        ^.backgroundColor := player.color.hex
      ),
      player.name
    )