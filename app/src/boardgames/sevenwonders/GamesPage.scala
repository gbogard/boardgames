package boardgames.sevenwonders

import boardgames.*
import boardgames.bindings.*
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
        ^.className := "bg-gray-50 bg-opacity-85 my-4 mx-2 p-2 drop-shadow-md rounded-md",
        <.table(
          ^.className := "table-auto text-left",
          <.thead(
            <.th(),
            <.th("T")
          ),
          <.tbody(
            game.players.values.toList
              .sortBy(-_.totalScore)
              .toVdomArray(ps =>
                <.tr(
                  ^.key := ps.player.id.toString,
                  <.th(^.className := "w-full", ps.player.name),
                  <.td(^.className := "whitespace-nowrap", ps.totalScore)
                )
              )
          )
        ),
        NextLink(
          s"/7wonders/games/${game.id}",
          <.button(
            ^.className := "bg-purple-300 rounded-md p-2 mt-2 w-full drop-shadow-sm",
            "Edit game"
          )
        )
          .when(game.state == GameState.Pending)
      )

    def render(state: State) =
      ReactFragment(
        PageBackground(PageBackground.SevenWonders),
        Header(
          "Last games",
          leftSide = Header.SideItem.BackButton("/").some,
          rightSide = Header.SideItem.PlusButton("/7wonders/new").some,
          style = Header.Style.Marble
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
