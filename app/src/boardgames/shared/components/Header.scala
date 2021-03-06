package boardgames.shared.components

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import boardgames.bindings.*
import boardgames.shared.Routes.*

object Header:
  type Action = Route | AsyncCallback[Unit] | Callback

  enum SideItem:
    case BackButton(action: Action)
    case NextButton(action: Action)
    case PlusButton(action: Action)

  case class Props(
      title: String,
      rightSide: Option[SideItem] = None,
      leftSide: Option[SideItem] = None
  )

  object Style:
    @js.native
    @JSImport(
      "@src/boardgames/shared/components/Header.module.css",
      JSImport.Namespace
    )
    protected val sheet: js.Dictionary[String] = js.native
    val className = sheet("header")

  // Map every action to a DOM element
  private def renderAction(act: Action, node: VdomNode): VdomNode =
    act match
      // Routes are matched to the Link component provided by next.js
      case to: Route => NextLink(to, node)
      // Callbacks are matched to spans with onClick listeners
      case cb: Callback            => <.span(^.onClick --> cb, node)
      case cb: AsyncCallback[Unit] => <.span(^.onClick --> cb, node)

  // Map every header item to a DOM element
  private def renderSideItem(item: SideItem) =
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
        ^.className := Style.className,
        <.div(props.leftSide.map(renderSideItem)),
        <.div(<.h1(^.className := "m-0 font-bold text-3xl", props.title)),
        <.div(props.rightSide.map(renderSideItem))
      )
    )

  def apply(props: Props) = component(props)
  def apply(
      title: String,
      rightSide: Option[SideItem] = None,
      leftSide: Option[SideItem] = None
  ) = component(Props(title, rightSide, leftSide))
