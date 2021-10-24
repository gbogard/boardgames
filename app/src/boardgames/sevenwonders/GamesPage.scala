package boardgames.sevenwonders

import boardgames.*
import boardgames.bindings.NextRouter
import boardgames.sevenwonders.*
import boardgames.shared.*
import cats.implicits.*
import japgolly.scalajs.react.*
import japgolly.scalajs.react.feature.ReactFragment
import japgolly.scalajs.react.vdom.html_<^.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.annotation.*

object GamesPage:

  final case class Props(router: NextRouter.Facade, repo: GamesRepository)
  final case class State(games: List[Game] = Nil)

  final class Backend($ : BackendScope[Props, State]):
    def renderGame(game: Game) =
      <.div(
        ^.key := game.id.toString,
        ^.className := "my-4 p-4 bg-black bg-opacity-40",
        <.ul(game.players.values.toList.toVdomArray(renderPlayer))
      )

    def renderPlayer(ps: PlayerState) =
      <.li(^.key := ps.player.id.toString, ps.player.name)

    def render(state: State) =
      ReactFragment(
        PageBackground(PageBackground.GreenFelt),
        Header(
          "Last games",
          leftSide = Header.SideItem.BackButton("/").some,
          rightSide = Header.SideItem.PlusButton("/7wonders/new").some
        ),
        state.games.toVdomArray(renderGame)
      )

    val componentDidMount: AsyncCallback[Unit] =
      for
        props <- $.props.async
        lastGames <- AsyncCallback.fromFuture(props.repo.listGames)
        _ <- $.modState(_.copy(games = lastGames)).async
      yield ()

  val component = ScalaComponent
    .builder[Props]
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(_.backend.componentDidMount)
    .build

  trait JSProps extends NextRouter.JSPropsWithRouter

  @JSExportTopLevel("GamesPage", "GamesPage")
  val GamesPageJS =
    GamesPage.component
      .cmapCtorProps[JSProps](p => Props(p.router.facade, GamesRepositoryImpl))
      .toJsComponent
      .raw
