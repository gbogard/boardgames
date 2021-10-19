package boardgames.bindings

import scala.scalajs.js.annotation.JSImport
import scalajs.js
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.VdomNode

object NextLink:
  @js.native
  @JSImport("next/link", JSImport.Default)
  protected val raw: js.Object = js.native
  protected val component = JsComponent[Props, Children.Varargs, Null](raw)

  def apply(href: String, children: VdomNode*) =
    component(Props(href))(children: _*)

  @js.native
  trait Props extends js.Object {
    var href: String = js.native
  }

  object Props:
    def apply(href: String): Props = {
      val p = (new js.Object).asInstanceOf[Props]
      p.href = href
      p
    }
