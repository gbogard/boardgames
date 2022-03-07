package boardgames.shared.components.scoring

import japgolly.scalajs.react.*
import japgolly.scalajs.react.feature.ReactFragment
import japgolly.scalajs.react.vdom.html_<^.*
import boardgames.shared.{given, *}
import boardgames.bindings.*
import boardgames.shared.components.*
import boardgames.shared.components.Header.SideItem.*
import cats.implicits.*

private object ScoreWizardStep:
  type ScoreWizardStep[Game] = StepWizard.StepComponent[Props[Game]]

  final case class Props[Game](
      game: Game,
      updateGame: Game => Callback,
      finishGame: AsyncCallback[Unit]
  )

  def build[Score, Game, PlayerState](
      title: String,
      getPlayers: Game => Map[PlayerId, PlayerState],
      getPlayer: PlayerState => Player,
      getScore: PlayerState => Score,
      upsertPlayer: (Game, PlayerState) => Game,
      updateScore: (PlayerState, Score) => PlayerState,
      scoreInput: ScoreInput.Component[Score],
      isLastStep: Boolean = false
  ): ScoreWizardStep[Game] =
    ScalaComponent
      .builder[StepWizard.StepProps[Props[Game]]]
      .render_P(props =>
        val leftBtn =
          val prevStepBtn = props.prevStep.as(BackButton(props.goPrevious))
          val prevPageBtn = BackButton(Routes.sevenWondersLastGames)
          prevStepBtn.getOrElse(prevPageBtn)
        val rightBtn = props.nextStep.as(NextButton(props.goNext))
        val Props(game, updateGame, finishGame) = props.data

        ReactFragment(
          PageBackground(PageBackground.SevenWonders),
          Header(title, leftSide = leftBtn.some, rightSide = rightBtn),
          getPlayers(game).values.toList.toVdomArray({ case ps =>
            val player = getPlayer(ps)
            val scoreInputProps = ScoreInput.Props(
              player,
              getScore(ps),
              newScore => updateGame(upsertPlayer(game, updateScore(ps, newScore)))
            )
            scoreInput.withKey(player.id.toString)(scoreInputProps)
          }),
          <.div(
            ^.display.none.when(!isLastStep),
            ^.className := "my-4 px-2",
            <.button(
              ^.className := "bg-purple-300 rounded-md p-2 mt-2 w-full shadow-md",
              ^.onClick --> finishGame,
              "Finish game"
            )
          )
        )
      )
      .build
