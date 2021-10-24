package boardgames.sevenwonders

import boardgames.*
import boardgames.shared.*
import cats.implicits.*
import japgolly.scalajs.react.*
import japgolly.scalajs.react.feature.ReactFragment
import japgolly.scalajs.react.vdom.html_<^.*
import scala.scalajs.js
import scala.scalajs.js.annotation.*

object GamesPage:

  val component = ScalaComponent
    .builder[Unit]
    .renderStatic(
      ReactFragment(
        Header(
          "Last games",
          leftSide = Header.SideItem.BackButton("/").some,
          rightSide = Header.SideItem.PlusButton("/7wonders/new").some
        ),
        PageBackground(PageBackground.GreenFelt)
      )
    )
    .build

  def apply() = component()

@JSExportTopLevel("GamesPage", "GamesPage")
val GamesPageJS = GamesPage.component.toJsComponent.raw
