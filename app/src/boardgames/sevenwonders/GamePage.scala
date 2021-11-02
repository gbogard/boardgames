package boardgames.sevenwonders

import boardgames.bindings.*
import boardgames.shared.*
import boardgames.shared.Header.SideItem.*
import boardgames.shared.ExternalEntity.*
import cats.implicits.*
import japgolly.scalajs.react.*
import japgolly.scalajs.react.feature.ReactFragment
import japgolly.scalajs.react.vdom.html_<^.*
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.concurrent.ExecutionContext.Implicits.global

object ScoreInput:
  type Component[Score] = ScalaComponent[ScoreInput.Props[Score], Unit, Unit, CtorType.Props]
  final case class Props[Score](player: Player, score: Score, updateScore: Score => Callback)

  val int: Component[Int] = ScalaComponent
    .builder[Props[Int]]
    .render_P(props =>
      <.div(
        ^.key := props.player.id.toString,
        ^.className := "my-4",
        <.div(
          ^.className := "mb-2 px-2 flex items-center",
          <.div(
            ^.className := "h-4 w-4 mr-2 rounded-sm",
            ^.backgroundColor := props.player.color.hex
          ),
          props.player.name
        ),
        <.input(
          ^.tpe := "number",
          ^.step := 1,
          ^.min := 0,
          ^.className := "block w-full h-12 px-2",
          ^.value := props.score,
          ^.onChange ==> ((e: ReactEventFromInput) => props.updateScore(e.target.value.toInt))
        )
      )
    )
    .build

  val science: Component[ScientificScore] =
    ScalaComponent
      .builder[Props[ScientificScore]]
      .renderStatic(<.div())
      .build

object GameWizardStep:

  case class Data(game: Game, updateGame: Game => Callback)

  def apply[Score](
      title: String,
      getScore: PlayerState => Score,
      updateScore: (PlayerState, Score) => PlayerState,
      scoreInput: ScoreInput.Component[Score] = ScoreInput.int
  ) =
    ScalaComponent
      .builder[StepWizard.StepProps[Data]]
      .render_P(props =>
        val leftBtn =
          val prevStepBtn = props.prevStep.as(BackButton(props.goPrevious))
          val prevPageBtn = BackButton(Routes.sevenWondersLastGames)
          prevStepBtn.getOrElse(prevPageBtn)
        val rightBtn = props.nextStep.as(NextButton(props.goNext))
        val Data(game, updateGame) = props.data

        ReactFragment(
          PageBackground(PageBackground.SevenWonders),
          Header(title, leftSide = leftBtn.some, rightSide = rightBtn, style = Header.Style.Marble),
          game.players.values.toList.toVdomArray({ case ps =>
            val scoreInputProps = ScoreInput.Props(
              ps.player,
              getScore(ps),
              newScore => updateGame(game.upsertPlayer(updateScore(ps, newScore)))
            )
            scoreInput.withKey(ps.player.id.toString)(scoreInputProps)
          })
        )
      )
      .build

object GamePage:
  val GameStepWizard = StepWizard.component[GameWizardStep.Data]

  type State = ExternalEntity[Game]
  final case class Props(router: NextRouter, repo: GamesRepository)

  class Backend($ : BackendScope[Props, State]):
    val loadGame: AsyncCallback[Unit] =
      for
        props <- $.props.async
        gameIdOpt = props.router.query.get("id").flatMap(GameId.fromString)
        game <- AsyncCallback.traverseOption(gameIdOpt)(gameId =>
          AsyncCallback.fromFuture(props.repo.getGame(gameId))
        )
        _ <- $.setState(game.flatten.loadedOrNotFound).async
      yield ()

    val saveGame: AsyncCallback[Unit] =
      for
        props <- $.props.async
        state <- $.state.async
        _ <- state.fold(Callback.empty.async)(game =>
          AsyncCallback.fromFuture(props.repo.upsertGame(game))
        )
      yield ()

    def componentDidUpdate(
        prevState: State,
        newState: State,
        prevProps: Props,
        newProps: Props
    ): AsyncCallback[Unit] =
      loadGame.when_(prevProps.router.query != newProps.router.query) >>
        saveGame.when_(prevState != newState)

    def render(gameEntity: State): VdomNode = gameEntity match
      case ExternalEntity.Loaded(game) =>
        GameStepWizard(
          StepWizard.Props(
            GameWizardStep.Data(game, g => $.setState(g.loaded)),
            GameWizardStep("Wonder", _.wonder, (p, s) => p.copy(wonder = s)),
            GameWizardStep("Military", _.military, (p, s) => p.copy(military = s)),
            GameWizardStep("Treasury", _.treasury, (p, s) => p.copy(treasury = s)),
            GameWizardStep(
              "Civilian",
              _.civilianStructures,
              (p, s) => p.copy(civilianStructures = s)
            ),
            GameWizardStep("Science", _.science, (p, s) => p.copy(science = s), ScoreInput.science),
            GameWizardStep("Commerce", _.commerce, (p, s) => p.copy(commerce = s)),
            GameWizardStep("Guilds", _.guilds, (p, s) => p.copy(guilds = s)),
            GameWizardStep("City", _.cities, (p, s) => p.copy(cities = s)),
            GameWizardStep("Leaders", _.leaders, (p, s) => p.copy(leaders = s))
          )
        )
      case ExternalEntity.Loading =>
        PageBackground(PageBackground.SevenWonders)
      case ExternalEntity.NotFound =>
        ReactFragment(
          PageBackground(PageBackground.SevenWonders),
          <.div(^.className := "text-center p-4", "Game not found")
        )

  val component = ScalaComponent
    .builder[Props]
    .initialState(ExternalEntity.Loading: State)
    .renderBackend[Backend]
    .componentDidMount(_.backend.loadGame)
    .componentDidUpdate(ctx =>
      ctx.backend
        .componentDidUpdate(ctx.prevState, ctx.currentState, ctx.prevProps, ctx.currentProps)
    )
    .build

  trait JSProps extends NextRouter.JSPropsWithRouter

  @JSExportTopLevel("GamePage", "GamePage")
  val jsComponent = NextRouter.withRouter(
    component
      .cmapCtorProps[JSProps](p => Props(p.router.facade, GamesRepositoryImpl))
      .toJsComponent
      .raw
  )
