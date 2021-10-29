ThisBuild / scalaVersion := "3.1.0"
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
    npm := "/Users/gbogard/.nix-profile/bin/npm",
    libraryDependencies ++= Seq(
      "dev.guillaumebogard" %%% "scalajs-idb-core" % "0.2.0-SNAPSHOT",
      "dev.guillaumebogard" %%% "scalajs-idb-java-time" % "0.2.0-SNAPSHOT"
    )
  )
