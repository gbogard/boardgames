package boardgames.bindings

import scala.scalajs.js.annotation.JSImport
import scalajs.js
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.VdomNode
import boardgames.shared.Routes.Route

object NextLink:
  @js.native
  @JSImport("next/link", JSImport.Default)
  protected val raw: js.Object = js.native
  protected val component = JsComponent[Props, Children.Varargs, Null](raw)

  def apply(href: Route, children: VdomNode*) =
    component(Props(href))(children*)

  @js.native
  trait Props extends js.Object:
    var href: Route = js.native

  object Props:
    def apply(href: Route): Props =
      val p = (new js.Object).asInstanceOf[Props]
      p.href = href
      p
