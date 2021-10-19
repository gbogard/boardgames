ThisBuild / scalaVersion := "3.0.1"

lazy val root = project
  .in(file("."))
  .aggregate(app)
  .settings(
    name := "boardgames"
  )

lazy val app = project
  .in(file("app"))
  .enablePlugins(NextApp)
  .settings(
    libraryDependencies ++= Seq(
      "dev.guillaumebogard" %%% "scalajs-idb-core" % "0.1.0"
    )
  )
