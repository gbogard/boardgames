package boardgames.sevenwonders

import boardgames.*
import boardgames.bindings.*
import boardgames.sevenwonders.*
import boardgames.shared.components.*
import boardgames.shared.*
import cats.implicits.*
import japgolly.scalajs.react.*
import japgolly.scalajs.react.feature.ReactFragment
import japgolly.scalajs.react.vdom.html_<^.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.annotation.*
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.ZoneId

object GamesPage:

  final case class Props(repo: GamesRepository)
  final case class State(games: List[Game] = Nil)

  private def renderDate(instant: Instant) =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")
      .withZone(ZoneId.of("UTC"))
      .format(instant)

  final class Backend($ : BackendScope[Props, State]):
    def renderPlayer(ps: PlayerState) =
      <.tr(
        ^.key := ps.player.id.toString,
        <.th(
          ^.className := "w-full",
          <.div(
            ^.className := " flex items-center",
            <.div(
              ^.className := "h-4 w-4 mr-2 rounded-sm",
              ^.backgroundColor := ps.player.color.hex
            ),
            ps.player.name
          )
        ),
        <.td(^.className := "whitespace-nowrap", ps.totalScore)
      )

    def renderGame(game: Game) =
      <.div(
        ^.key := game.id.toString,
        ^.className := "bg-gray-50 bg-opacity-85 my-4 mx-2 p-2 drop-shadow-md rounded-md",
        <.table(
          ^.className := "table-auto text-left",
          <.thead(
            <.th(renderDate(game.createdAt)),
            <.th("T")
          ),
          <.tbody(
            game.players.values.toList
              .sortBy(-_.totalScore)
              .toVdomArray(renderPlayer)
          )
        ),
        NextLink(
          Routes.sevenWondersGame(game.id),
          <.button(
            ^.className := "bg-purple-300 rounded-md p-2 mt-2 w-full shadow-lg",
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
          leftSide = Header.SideItem.BackButton(Routes.home).some,
          rightSide = Header.SideItem.PlusButton(Routes.sevenWondersNewGame).some
        ),
        state.games.toVdomArray(renderGame)
      )

    def componentDidMount(props: Props): AsyncCallback[Unit] =
      AsyncCallback
        .fromFuture(props.repo.listGames)
        .flatMap(lastGames => $.modState(_.copy(games = lastGames)).async)

  end Backend

  def component = ScalaComponent
    .builder[Props]
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount($ => $.backend.componentDidMount($.props))
    .build
    .cmapCtorProps[Unit](_ => Props(GamesRepositoryImpl))

  @JSExportTopLevel("GamesPage", "GamesPage")
  val GamesPageJS =
    GamesPage.component
      .toJsComponent
      .raw
