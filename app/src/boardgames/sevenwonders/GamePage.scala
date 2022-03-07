package boardgames.sevenwonders

import boardgames.bindings.*
import boardgames.shared.*
import boardgames.shared.components.scoring.*
import boardgames.shared.components.*
import boardgames.shared.components.Header.SideItem.*
import boardgames.shared.ExternalEntity.*
import cats.implicits.*
import japgolly.scalajs.react.*
import japgolly.scalajs.react.feature.ReactFragment
import japgolly.scalajs.react.vdom.html_<^.*
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.concurrent.ExecutionContext.Implicits.global

object GamePage:

  val wizardShape = ScoreWizardShape[Game, PlayerState](_.players, _.player, _.upsertPlayer(_))
    .step("Wonder", ScoreInput.int, _.wonder, (p, s) => p.copy(wonder = s))
    .step("Military", ScoreInput.int, _.military, (p, s) => p.copy(military = s))
    .step("Science", ScienceScoreInput.component, _.science, (p, s) => p.copy(science = s))
    .step("Treasury", ScoreInput.int, _.treasury, (p, s) => p.copy(treasury = s))
    .step(
      "Civilian",
      ScoreInput.int,
      _.civilianStructures,
      (p, s) => p.copy(civilianStructures = s)
    )
    .step("Commerce", ScoreInput.int, _.commerce, (p, s) => p.copy(commerce = s))
    .step("Guilds", ScoreInput.int, _.guilds, (p, s) => p.copy(guilds = s))
    .step("City", ScoreInput.int, _.cities, (p, s) => p.copy(cities = s))
    .step("Leaders", ScoreInput.int, _.leaders, (p, s) => p.copy(leaders = s))

  val wizard = ScoreWizard.build(wizardShape)

  type State = ExternalEntity[Game]
  final case class Props(router: NextRouter, repo: GamesRepository[Game])

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

    def render(gameEntity: State, props: Props): VdomNode =
      wizard(ScoreWizard.Props(gameEntity, game => $.modState(_.as(game)), finishGame))

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
