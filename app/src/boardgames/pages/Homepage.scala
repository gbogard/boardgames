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
        <.styleTag(
          """
          body { 
            background-image: url(./green-felt-texture.jpg); 
            background-size: 300px 300px;
          }
          """
        ),
        Header(),
        <.h1("My app", ^.className := "font-serif"),
        GameButton(Game.SevenWonders)
      )
    )
    .build

  def apply() = component()

@JSExportTopLevel("Homepage", "Homepage")
val HomePageJS = Homepage.component.toJsComponent.raw
