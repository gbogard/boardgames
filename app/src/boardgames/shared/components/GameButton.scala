package boardgames.shared.components

import boardgames.*
import boardgames.bindings.*
import boardgames.shared.*
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import scalajs.js
import scala.scalajs.js.annotation.JSImport

object GameButton:
  case class Props(game: GameType)

  @js.native
  @JSImport(
    "@src/boardgames/shared/components/GameButton.module.css",
    JSImport.Namespace
  )
  protected val style: js.Dictionary[String] = js.native

  private def image(game: GameType) = game match
    case GameType.SevenWonders => "/7wonders-btn.png"

  private val component =
    ScalaFnComponent[Props](props =>
      NextLink(
        Routes.sevenWondersLastGames,
        <.a(
          ^.href := "#",
          ^.className := style("button"),
          <.img(^.src := image(props.game))
        )
      )
    )

  def apply(game: GameType) = component(Props(game))
