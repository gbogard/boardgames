ThisBuild / scalaVersion := "3.0.1"
ThisBuild / resolvers +=
  "Sonatype OSS Snapshots" at "https://s01.oss.sonatype.org/content/repositories/snapshots"

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
      "dev.guillaumebogard" %%% "scalajs-idb-core" % "0.2.0-SNAPSHOT"
    )
  )
