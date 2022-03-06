package boardgames.shared

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*

/** StepWizard is a generic "wizard" component that accepts a list of steps and some data, and takes
  * care of keeping track of the user's current step. Each step is a React component that will
  * receive the data provided to the wizard iteslf, as well as the wizard's current state, and
  * functions to navigate between steps from the step itself.
  */
object StepWizard:

  type StepComponent[Data] = ScalaComponent[StepProps[Data], Unit, Unit, CtorType.Props]

  final case class State(
      currentStepIndex: Int = 0
  )
  final case class Props[Data](
      data: Data,
      steps: StepComponent[Data]*
  )
  final case class StepProps[Data](
      currentStepIndex: Int,
      nbOfSteps: Int,
      goToStep: Int => Callback,
      data: Data
  ):
    val isFirst = currentStepIndex == 0
    val goNext = goToStep(currentStepIndex + 1)
    val goPrevious = goToStep(currentStepIndex - 1)
    val prevStep = if currentStepIndex == 0 then None else Some(currentStepIndex - 1)
    val nextStep = if currentStepIndex + 1 >= nbOfSteps then None else Some(currentStepIndex + 1)

  class Backend[Data]($ : BackendScope[Props[Data], State]):

    def goToStep(stepIndex: Int): Callback =
      $.propsChildren.flatMap(children => $.setState(State(currentStepIndex = stepIndex)))

    def render(props: Props[Data], state: State) =
      val stepProps = StepProps(state.currentStepIndex, props.steps.size, goToStep _, props.data)
      props.steps.lift(state.currentStepIndex).map(_(stepProps))

  def component[Data] =
    ScalaComponent
      .builder[Props[Data]]
      .initialState(State())
      .renderBackend[Backend[Data]]
      .build
