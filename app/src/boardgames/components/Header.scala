package boardgames.components

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*

enum Style:
  case Wooden

object Style:
  @js.native
  @JSImport("@src/boardgames/components/Header.module.css", JSImport.Namespace)
  protected val sheet: js.Dictionary[String] = js.native

  def apply(style: Style) = style match
    case Wooden => s"${sheet("header")} ${sheet("wooden")}"

object Header:
  case class Props(style: Style = Style.Wooden)

  private val component =
    ScalaFnComponent[Props](props => <.nav(^.className := Style(props.style)))

  def apply(props: Props = Props()) = component(props)
