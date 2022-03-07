package boardgames.shared.components.scoring

import boardgames.shared.*
import boardgames.shared.components.*
import japgolly.scalajs.react.*
import japgolly.scalajs.react.feature.ReactFragment
import japgolly.scalajs.react.vdom.html_<^.*

object ScoreWizard:
  final case class Props[Game](
      game: ExternalEntity[Game],
      updateGame: Game => Callback,
      finishGame: AsyncCallback[Unit]
  )

  def build[Game, PlayerState](
      shape: ScoreWizardShape.ScoreWizardShape[Game, PlayerState]
  ) =
    val wizardSteps: List[ScoreWizardStep.ScoreWizardStep[Game]] = shape.steps.map(s =>
      ScoreWizardStep.build(
        title = s.title,
        getPlayers = shape.getPlayers,
        getPlayer = shape.getPlayer,
        getScore = s.getScore,
        upsertPlayer = shape.updatePlayer,
        updateScore = s.setScore,
        scoreInput = s.scoreComponent,
        isLastStep = s.isLast
      )
    )
    ScalaComponent
      .builder[Props[Game]]
      .render_P({
        case Props(ExternalEntity.Loaded(game), updateGame, finishGame) =>
          StepWizard.component[ScoreWizardStep.Props[Game]](StepWizard.Props(
            ScoreWizardStep.Props(game, updateGame, finishGame),
            wizardSteps*
          ))
        case Props(ExternalEntity.Loading, _, _) => PageBackground(PageBackground.SevenWonders)
        case Props(ExternalEntity.NotFound, _, _) =>
          ReactFragment(
            PageBackground(PageBackground.SevenWonders),
            <.div(^.className := "text-center p-4", "Game not found")
          )
      })
      .build

object ScoreWizardShape:
  final case class Step[Game, PlayerState, Score](
      title: String,
      scoreComponent: ScoreInput.Component[Score],
      getScore: PlayerState => Score,
      setScore: (PlayerState, Score) => PlayerState,
      isLast: Boolean
  )

  final case class ScoreWizardShape[Game, PlayerState](
      getPlayers: Game => Map[PlayerId, PlayerState],
      getPlayer: PlayerState => Player,
      updatePlayer: (Game, PlayerState) => Game,
      steps: List[Step[Game, PlayerState, ?]]
  )

  def apply[Game, PlayerState](
      getPlayers: Game => Map[PlayerId, PlayerState],
      getPlayer: PlayerState => Player,
      updatePlayer: (Game, PlayerState) => Game
  ): ScoreWizardShape[Game, PlayerState] =
    ScoreWizardShape(getPlayers, getPlayer, updatePlayer, Nil)

  extension [Game, PlayerState](data: ScoreWizardShape[Game, PlayerState])
    def step[Score](
        title: String,
        scoreComponent: ScoreInput.Component[Score],
        getScore: PlayerState => Score,
        setScore: (PlayerState, Score) => PlayerState
    ): ScoreWizardShape[Game, PlayerState] =
      val newStep =
        Step[Game, PlayerState, Score](title, scoreComponent, getScore, setScore, isLast = true)
      data.copy(steps = data.steps.map(_.copy(isLast = false)) :+ newStep)
