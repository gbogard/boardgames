package boardgames.bindings

import japgolly.scalajs.react.*

import boardgames.shared.Routes.*
import scala.scalajs.js
import js.JSConverters.*
import scala.scalajs.js.annotation.JSImport

/** A Scala representation of a nextjs router, with "pure" callbacks
  */
final class NextRouter(router: NextRouter.JS):
  lazy val query: Map[String, String] = router.query.toMap
  def push(url: Route, as: Option[String] = None): Callback = Callback(
    router.push(url, as.orUndefined)
  )
  def replace(url: Route, as: Option[String] = None): Callback = Callback(
    router.replace(url, as.orUndefined)
  )

object NextRouter:
  /** The [[withRouter]] method wraps a JS React Component and injects the router into its props. It
    * is supposed to be used with JS Components whose props are [[js.Object]] that extends
    * [[JSPropsWithRouter]].
    *
    * The typical way of using the next router in a scalajs-react component is to:
    *
    * 1) Create a scala component whose props includes [[NextRouter]]
    *
    * 2) Create a JS object representation of yout component's props, as described in
    * [[https://github.com/japgolly/scalajs-react/blob/master/doc/INTEROP.md scalajs-react's documentation]].
    * This JS Object is supposed to extends [[JSPropsWithRouter]]
    *
    * 3) Transform your Scala component into a Javascript component by using "cmapCtorProps" to
    * transform JS props to Scala props. You can transforme a [[NextRouter.JS]] into a
    * [[NextRouter]] by calling [[facade]] on it.
    *
    * 4) Export your JS component and use it in a next.js page.
    */
  @js.native
  @JSImport("next/router", "withRouter")
  def withRouter(component: js.Object): js.Object = js.native

  trait JSPropsWithRouter extends js.Object:
    val router: JS

  /** The Javascript representation of a nextjs router, a straightforward binding to nextjs' API
    */
  @js.native
  trait JS extends js.Object:
    def push(url: Route, as: js.UndefOr[String]): Unit = js.native
    def replace(url: Route, as: js.UndefOr[String]): Unit = js.native
    val query: js.Dictionary[String] = js.native

  extension (router: JS) def facade = new NextRouter(router)
