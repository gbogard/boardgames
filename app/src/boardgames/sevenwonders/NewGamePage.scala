package boardgames.sevenwonders

import cats.implicits.*
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import scala.scalajs.js.annotation.*
import scala.scalajs.js
import boardgames.shared.*
import boardgames.bindings.*
import japgolly.scalajs.react.feature.ReactFragment
import scala.concurrent.ExecutionContext.Implicits.global

object NewGamePage:

  type State = List[Player]
  final case class Props(router: NextRouter, repo: GamesRepository)

  class Backend($ : BackendScope[Props, State]):
    def onAdd(player: Player): Callback =
      $.state.flatMap(players =>
        if players.exists(_.id == player.id) then Callback.empty
        else $.modState(_ :+ player)
      )

    def onRemove(player: Player): Callback =
      $.modState(_.filterNot(_.id == player.id))

    val onCreateGame: AsyncCallback[Unit] =
      for
        props <- $.props.async
        players <- $.state.async
        game = Game(players)
        _ <- AsyncCallback.fromFuture(props.repo.upsertGame(game))
        _ <- props.router.push(Routes.sevenWondersGame(game.id)).async
      yield ()

    def renderPlayer(player: Player) =
      <.div(
        ^.className := "flex items-center justify-between backdrop-blur-sm",
        ^.key := player.id.toString,
        <.div(
          ^.className := "flex items-center",
          <.div(
            ^.className := "w-6 h-6 m-3 rounded",
            ^.backgroundColor := player.color.hex
          ),
          <.h3(player.name)
        ),
        <.button(
          ^.className := "text-red-500 mr-4",
          ^.onClick --> onRemove(player),
          Icons.RemoveCircle(Icons.Props("2rem"))
        )
      )

    def render(players: List[Player]) =
      val nextColor = Color.values.lift(players.size).getOrElse(Color.Black)
      ReactFragment(
        PageBackground(PageBackground.SevenWonders),
        Header(
          "New game",
          leftSide = Header.SideItem.BackButton(Routes.sevenWondersLastGames).some,
          rightSide =
            if (players.size > 1) then Header.SideItem.PlusButton(onCreateGame).some else None,
          style = Header.Style.Marble
        ),
        NewPlayerForm(onAdd, nextColor),
        players.toVdomArray(renderPlayer)
      )

  val component = ScalaComponent
    .builder[Props]
    .initialState(List.empty[Player])
    .renderBackend[Backend]
    .build

object NewGamePageJS:
  trait JSProps extends NextRouter.JSPropsWithRouter

  @JSExportTopLevel("NewGamePage", "NewGamePage")
  val NewGamePageJS =
    NextRouter.withRouter(
      NewGamePage.component
        .cmapCtorProps[JSProps](p => NewGamePage.Props(p.router.facade, GamesRepositoryImpl))
        .toJsComponent
        .raw
    )
