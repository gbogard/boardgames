package boardgames.shared

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import japgolly.scalajs.react.*
import scala.quoted.*

object Icons:
  object BackArrow:
    @js.native
    @JSImport("react-icons/io", "IoIosArrowBack")
    val raw: js.Object = js.native
    val component = JsComponent[Props, Children.None, Null](raw)
    def apply(props: Props = Props()) = component(props)

  object Plus:
    @js.native
    @JSImport("react-icons/io", "IoIosAdd")
    val raw: js.Object = js.native
    val component = JsComponent[Props, Children.None, Null](raw)
    def apply(props: Props = Props()) = component(props)

  object AddPerson:
    @js.native
    @JSImport("react-icons/io5", "IoPersonAdd")
    val raw: js.Object = js.native
    val component = JsComponent[Props, Children.None, Null](raw)
    def apply(props: Props = Props()) = component(props)

  object RemoveCircle:
    @js.native
    @JSImport("react-icons/io5", "IoRemoveCircle")
    val raw: js.Object = js.native
    val component = JsComponent[Props, Children.None, Null](raw)
    def apply(props: Props = Props()) = component(props)

  @js.native
  trait Props extends js.Object:
    var size: String = js.native

  object Props:
    def apply(size: String = "1rem"): Props =
      val p = (new js.Object).asInstanceOf[Props]
      p.size = size
      p