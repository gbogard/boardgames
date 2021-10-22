package boardgames.pages

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import boardgames.*
import boardgames.components.*
import japgolly.scalajs.react.feature.ReactFragment

object Homepage:

  val component = ScalaComponent
    .builder[Unit]
    .renderStatic(
      ReactFragment(
        PageBackground(PageBackground.GreenFelt),
        Header("Games"),
        GameButton(GameType.SevenWonders)
      )
    )
    .build

  def apply() = component()

@JSExportTopLevel("Homepage", "Homepage")
val HomePageJS = Homepage.component.toJsComponent.raw
