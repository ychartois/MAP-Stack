name := "MAP-Stack"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  cache,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2",
  "com.github.athieriot" %% "specs2-embedmongo" % "0.6.0" % "test"
)     

play.Project.playScalaSettings
