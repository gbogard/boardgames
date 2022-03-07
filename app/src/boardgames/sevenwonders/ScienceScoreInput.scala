package boardgames.sevenwonders

import boardgames.shared.*
import boardgames.shared.components.*
import boardgames.shared.components.scoring.ScoreInput
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import scala.util.Try

object ScienceScoreInput:
  val component: ScoreInput.Component[ScientificScore] =
    ScalaComponent
      .builder[ScoreInput.Props[ScientificScore]]
      .render_P(props =>
        <.div(
          ^.className := "my-5",
          ScoreInput.playerName(props.player),
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

end ScienceScoreInput
