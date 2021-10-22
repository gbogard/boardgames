package boardgames.components

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import japgolly.scalajs.react.vdom.*

enum PageBackground:
  case GreenFelt

object PageBackground:

  private val render: PageBackground => VdomNode =
    case GreenFelt =>
      <.styleTag(
        """
          body { 
            background-image: url(./green-felt-texture.jpg); 
            background-size: 300px 300px;
          }
          """
      )

  private val component = ScalaComponent
    .builder[PageBackground]
    .render_P(render)
    .build

  def apply(bg: PageBackground) = component(bg)
