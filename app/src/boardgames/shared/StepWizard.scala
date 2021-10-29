package boardgames.shared

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*

object StepWizard:

  type StepComponent = ScalaComponent[StepProps, Unit, Unit, CtorType.Props]
  type Props = List[StepComponent]

  final case class State(
      currentStepIndex: Int = 0
  )
  final case class StepProps(
      currentStepIndex: Int,
      nbOfSteps: Int,
      goToStep: Int => Callback
  ):
    val goNext = goToStep(currentStepIndex + 1)
    val goPrevious = goToStep(currentStepIndex - 1)
    val prevStep = if currentStepIndex == 0 then None else Some(currentStepIndex - 1)
    val nextStep = if currentStepIndex + 1 > nbOfSteps then None else Some(currentStepIndex + 1)

  class Backend($ : BackendScope[Props, State]):

    def goToStep(stepIndex: Int): Callback = $.propsChildren.flatMap(children =>
      val newStepIndex = Math.min(Math.max(0, stepIndex), children.count - 1)
      $.setState(State(currentStepIndex = newStepIndex))
    )

    def render(props: Props, state: State) =
      val stepProps = StepProps(state.currentStepIndex, props.size, goToStep _)
      props.lift(state.currentStepIndex).map(_(stepProps))

  val component =
    ScalaComponent
      .builder[Props]
      .initialState(State())
      .renderBackend[Backend]
      .build

  def apply(steps: StepComponent*) = component(steps.toList)
