package boardgames.shared

import boardgames.*
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*

object NewPlayerForm:
  final case class Props(
      onAdd: Player => Callback,
      nextColor: Color
  )

  class Backend($ : BackendScope[Props, String]):
    val handleAdd = for
      playerName <- $.state
      props <- $.props
      player = Player(playerName, props.nextColor)
      _ <- props.onAdd(player)
      _ <- $.setState("")
    yield ()

    def handleChange(e: ReactEventFromInput) = $.setState(e.target.value)

    def render(props: Props, playerName: String) =
      <.div(
        ^.className := "my-6 flex",
        <.input(
          ^.className := "h-14 bg-opacity-30 bg-black flex-grow pl-4 text-white",
          ^.placeholder := "New player...",
          ^.value := playerName,
          ^.onChange ==> handleChange
        ),
        <.button(
          ^.hidden := playerName.isEmpty,
          ^.className := List(
            "h-14",
            "px-6",
            "text-white",
            "bg-green-500"
          ).mkString(" "),
          ^.onClick --> handleAdd,
          Icons.AddPerson(Icons.Props("1.6rem"))
        )
      )

  val component =
      ScalaComponent
        .builder[Props]
        .initialState("")
        .renderBackend[Backend]
        .build

  def apply(onAdd: Player => Callback, nextColor: Color) =
    component(Props(onAdd, nextColor))
