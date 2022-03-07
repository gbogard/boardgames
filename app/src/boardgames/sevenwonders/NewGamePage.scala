package boardgames.sevenwonders

import cats.implicits.*
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import scala.scalajs.js.annotation.*
import scala.scalajs.js
import boardgames.shared.*
import boardgames.shared.components.*
import boardgames.bindings.*
import japgolly.scalajs.react.feature.ReactFragment
import scala.concurrent.ExecutionContext.Implicits.global

object NewGamePage:

  type State = List[Player]
  final case class Props(router: NextRouter, repo: GamesRepository)

  class Backend($ : BackendScope[Props, State]):

    def onCreateGame(players: List[Player]): AsyncCallback[Unit] =
      for
        props <- $.props.async
        game = Game(players)
        _ <- AsyncCallback.fromFuture(props.repo.upsertGame(game))
        _ <- props.router.push(Routes.sevenWondersGame(game.id)).async
      yield ()

    def render(players: List[Player]) =
      GenericNewGamePage(PageBackground.SevenWonders, onCreateGame)

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
