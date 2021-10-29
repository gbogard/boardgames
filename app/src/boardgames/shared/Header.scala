package boardgames.shared

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import boardgames.bindings.*

object Header:
  type Action = String | AsyncCallback[Unit] | Callback
  case class Props(
      title: String,
      rightSide: Option[SideItem] = None,
      leftSide: Option[SideItem] = None,
      style: Style = Style.Wooden
  )

  enum SideItem:
    case BackButton(action: Action)
    case NextButton(action: Action)
    case PlusButton(action: Action)

  enum Style:
    case Wooden
    case Marble

  object Style:
    @js.native
    @JSImport(
      "@src/boardgames/shared/Header.module.css",
      JSImport.Namespace
    )
    protected val sheet: js.Dictionary[String] = js.native

    def apply(style: Style) = style match
      case Wooden => s"${sheet("header")} ${sheet("wooden")}"
      case Marble => s"${sheet("header")} ${sheet("marble")}"

  private def renderSideItem(item: SideItem) =
    def renderAction(act: Action, node: VdomNode): VdomNode =
      act match
        case to: String              => NextLink(to, node)
        case cb: Callback            => <.span(^.onClick --> cb, node)
        case cb: AsyncCallback[Unit] => <.span(^.onClick --> cb, node)

    item match
      case SideItem.BackButton(action) =>
        renderAction(action, Icons.BackArrow(Icons.Props("3rem")))
      case SideItem.PlusButton(action) =>
        renderAction(action, Icons.Plus(Icons.Props("3rem")))
      case SideItem.NextButton(action) =>
        renderAction(action, Icons.ForwardArrow(Icons.Props("3rem")))

  private val component =
    ScalaFnComponent[Props](props =>
      <.nav(
        ^.className := Style(props.style),
        <.div(props.leftSide.map(renderSideItem)),
        <.div(<.h1(^.className := "m-0 font-bold text-3xl", props.title)),
        <.div(props.rightSide.map(renderSideItem))
      )
    )

  def apply(props: Props) = component(props)
  def apply(
      title: String,
      rightSide: Option[SideItem] = None,
      leftSide: Option[SideItem] = None,
      style: Style = Style.Wooden
  ) = component(Props(title, rightSide, leftSide, style))
