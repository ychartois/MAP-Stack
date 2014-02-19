name := "MAP-Stack"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  cache,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2"
)     

play.Project.playScalaSettings
