package boardgames.sevenwonders

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
          ^.min := 0,
          ^.className := "block w-full h-12 px-2",
          ^.value := props.score,
          ^.onChange ==> ((e: ReactEventFromInput) =>
            Try(e.target.value.toInt).fold(_ => Callback.empty, props.updateScore)
          )
        )
      )
    )
    .build

  val science: Component[ScientificScore] =
    ScalaComponent
      .builder[Props[ScientificScore]]
      .render_P(props =>
        <.div(
          ^.className := "my-5",
          playerName(props.player),
          <.div(
            ^.className := "grid grid-cols-3 gap-4 bg-white bg-opacity-75 shadow-md ",
            scienceCounter(
              "/7wonders-icons/cog.png",
              props.score.cogSymbols,
              s => props.updateScore(props.score.copy(cogSymbols = s))
            ),
            scienceCounter(
              "/7wonders-icons/compass.png",
              props.score.compassSymbols,
              s => props.updateScore(props.score.copy(compassSymbols = s))
            ),
            scienceCounter(
              "/7wonders-icons/tablet.png",
              props.score.tabletSymbols,
              s => props.updateScore(props.score.copy(tabletSymbols = s))
            )
          )
        )
      )
      .build

  private def playerName(player: Player) =
    <.div(
      ^.className := "mb-2 px-2 flex items-center",
      <.div(
        ^.className := "h-4 w-4 mr-2 rounded-sm",
        ^.backgroundColor := player.color.hex
      ),
      player.name
    )

  private def scienceCounter(
      icon: String,
      value: Int,
      setValue: Int => Callback
  ) = <.div(
    ^.className := "flex justify-center items-center",
    <.div(
      ^.className := "flex flex-col items-center",
      <.div(Icons.UpArrow(Icons.Props("2.5rem")), ^.onClick --> setValue(value + 1)),
      <.img(^.src := icon, ^.className := "h-10"),
      <.div(Icons.DownArrow(Icons.Props("2.5rem")), ^.onClick --> setValue(Math.max(value - 1, 0)))
    ),
    <.p(^.className := "text-xl ml-2", value)
  )

end ScoreInput
