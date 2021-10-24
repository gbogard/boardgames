package boardgames.bindings

import japgolly.scalajs.react.*

import scala.scalajs.js
import js.JSConverters.*
import scala.scalajs.js.annotation.JSImport

object NextRouter:

  @js.native
  @JSImport("next/router", "withRouter")
  def withRouter(component: js.Object): js.Object = js.native

  trait JSPropsWithRouter extends js.Object:
    val router: JS

  @js.native
  trait JS extends js.Object:
    def push(url: String, as: js.UndefOr[String]): Unit = js.native
    def replace(url: String, as: js.UndefOr[String]): Unit = js.native

  extension (router: JS) def facade = new Facade(router)

  final class Facade(router: JS):
    def push(url: String, as: Option[String] = None): Callback = Callback(
      router.push(url, as.orUndefined)
    )
    def replace(url: String, as: Option[String] = None): Callback = Callback(
      router.replace(url, as.orUndefined)
    )
