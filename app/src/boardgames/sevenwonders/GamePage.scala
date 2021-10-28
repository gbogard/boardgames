package boardgames.sevenwonders

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import boardgames.shared.*
import boardgames.shared.Header.SideItem.*
import scala.scalajs.js.annotation.JSExportTopLevel
import cats.implicits.*

object GenericGamePageStep:

  def apply(title: String) = ScalaComponent
    .builder[StepWizard.StepProps]
    .render_P(props =>
  

      Header(title)
    )
    .build

object GamePage:
  class Backend($ : BackendScope[Unit, Unit]):
    val wizardRef = Ref.toScalaComponent(StepWizard.component)

    def render() = StepWizard(
      GenericGamePageStep("Wonder"),
      GenericGamePageStep("Military"),
      GenericGamePageStep("Treasury"),
      GenericGamePageStep("Civilian"),
      GenericGamePageStep("Science"),
      GenericGamePageStep("Commerce"),
      GenericGamePageStep("Guilds"),
      GenericGamePageStep("City"),
      GenericGamePageStep("Leaders")
    )

  val component = ScalaComponent
    .builder[Unit]
    .renderBackend[Backend]
    .build

  @JSExportTopLevel("GamePage", "GamePage")
  val jsComponent = component.toJsComponent.raw
