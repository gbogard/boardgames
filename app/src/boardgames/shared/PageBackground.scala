package boardgames.shared

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import japgolly.scalajs.react.vdom.*

enum PageBackground:
  case GreenFelt
  case SevenWonders

object PageBackground:

  private val render: PageBackground => VdomNode =
    case GreenFelt =>
      <.styleTag(
        """
          body { 
            background-image: url(/green-felt-texture.jpg); 
            background-size: 300px 300px;
          }
          """
      )
    case SevenWonders =>
      <.styleTag(
        """
          body { 
            background-image: url(/7wonders-bg.png); 
            background-color: #7B8CC0;
            background-size: contain;
            background-repeat: no-repeat;
            background-position: center 150%;
            background-attachment: fixed;
          }
          """
      )

  private val component = ScalaComponent
    .builder[PageBackground]
    .render_P(render)
    .build

  def apply(bg: PageBackground) = component(bg)
