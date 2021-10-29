package boardgames.shared

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import japgolly.scalajs.react.*

object Icons:
  @js.native
  trait Props extends js.Object:
    var size: String = js.native

  object Props:
    def apply(size: String = "1rem"): Props =
      val p = (new js.Object).asInstanceOf[Props]
      p.size = size
      p

  object BackArrow:
    @js.native
    @JSImport("@react-icons/all-files/io/IoIosArrowBack", "IoIosArrowBack")
    val raw: js.Object = js.native
    val component = JsComponent[Props, Children.None, Null](raw)
    def apply(props: Props = Props()) = component(props)

  object ForwardArrow:
    @js.native
    @JSImport("@react-icons/all-files/io/IoIosArrowForward", "IoIosArrowForward")
    val raw: js.Object = js.native
    val component = JsComponent[Props, Children.None, Null](raw)
    def apply(props: Props = Props()) = component(props)

  object Plus:
    @js.native
    @JSImport("@react-icons/all-files/io/IoIosAdd", "IoIosAdd")
    val raw: js.Object = js.native
    val component = JsComponent[Props, Children.None, Null](raw)
    def apply(props: Props = Props()) = component(props)

  object AddPerson:
    @js.native
    @JSImport("@react-icons/all-files/io5/IoPersonAdd", "IoPersonAdd")
    val raw: js.Object = js.native
    val component = JsComponent[Props, Children.None, Null](raw)
    def apply(props: Props = Props()) = component(props)

  object RemoveCircle:
    @js.native
    @JSImport("@react-icons/all-files/io5/IoRemoveCircle", "IoRemoveCircle")
    val raw: js.Object = js.native
    val component = JsComponent[Props, Children.None, Null](raw)
    def apply(props: Props = Props()) = component(props)
