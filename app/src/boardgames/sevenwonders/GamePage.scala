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

object GameWizardStep:

  case class Data(game: Game, updateGame: Game => Callback, finishGame: AsyncCallback[Unit])

  def apply[Score](
      title: String,
      getScore: PlayerState => Score,
      updateScore: (PlayerState, Score) => PlayerState,
      scoreInput: ScoreInput.Component[Score] = ScoreInput.int,
      isLastStep: Boolean = false
  ) =
    ScalaComponent
      .builder[StepWizard.StepProps[Data]]
      .render_P(props =>
        val leftBtn =
          val prevStepBtn = props.prevStep.as(BackButton(props.goPrevious))
          val prevPageBtn = BackButton(Routes.sevenWondersLastGames)
          prevStepBtn.getOrElse(prevPageBtn)
        val rightBtn = props.nextStep.as(NextButton(props.goNext))
        val Data(game, updateGame, finishGame) = props.data

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

    val finishGame: AsyncCallback[Unit] =
      for
        props <- $.props.async
        _ <- $.modState(_.map(_.copy(state = GameState.Finished))).async
        _ <- saveGame
        _ <- props.router.push(Routes.sevenWondersLastGames).async
      yield ()

    def componentDidUpdate(
        prevState: State,
        newState: State,
        prevProps: Props,
        newProps: Props
    ): AsyncCallback[Unit] =
      loadGame.when_(prevProps.router.query != newProps.router.query) >>
        saveGame.when_(prevState != newState)

    private val steps = Seq(
      GameWizardStep("Wonder", _.wonder, (p, s) => p.copy(wonder = s)),
      GameWizardStep("Military", _.military, (p, s) => p.copy(military = s)),
      GameWizardStep("Science", _.science, (p, s) => p.copy(science = s), ScoreInput.science),
      GameWizardStep("Treasury", _.treasury, (p, s) => p.copy(treasury = s)),
      GameWizardStep(
        "Civilian",
        _.civilianStructures,
        (p, s) => p.copy(civilianStructures = s)
      ),
      GameWizardStep("Commerce", _.commerce, (p, s) => p.copy(commerce = s)),
      GameWizardStep("Guilds", _.guilds, (p, s) => p.copy(guilds = s)),
      GameWizardStep("City", _.cities, (p, s) => p.copy(cities = s)),
      GameWizardStep("Leaders", _.leaders, (p, s) => p.copy(leaders = s), isLastStep = true)
    )

    def render(gameEntity: State, props: Props): VdomNode = gameEntity match
      case ExternalEntity.Loaded(game) =>
        GameStepWizard(
          StepWizard.Props(
            GameWizardStep.Data(game, g => $.setState(g.loaded), finishGame),
            steps*
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
