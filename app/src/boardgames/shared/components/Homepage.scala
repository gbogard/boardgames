package boardgames.shared.components

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import boardgames.*
import boardgames.shared.*
import japgolly.scalajs.react.feature.ReactFragment

val Homepage = ScalaComponent
  .builder[Unit]
  .renderStatic(
    ReactFragment(
      PageBackground(PageBackground.GreenFelt),
      Header("Games"),
      GameButton(GameType.SevenWonders),
      GameButton(GameType.Hallertau),
    )
  )
  .build

@JSExportTopLevel("Homepage", "Homepage")
val HomePageJS = Homepage.toJsComponent.raw
