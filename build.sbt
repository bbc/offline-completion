name := "offline-completion"

version := "0.1"

scalaVersion := "2.11.12"
scalacOptions ++= Seq("-feature", "-language:implicitConversions", "-deprecation")

libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.7.0-M4"

libraryDependencies ++= Seq (
  "org.json4s" %% "json4s-jackson" % "3.7.0-M4",
  "info.cukes" %% "cucumber-scala" % "1.2.5",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test"
)

// Cucumber settings
enablePlugins(CucumberPlugin)
CucumberPlugin.monochrome := false
CucumberPlugin.features := List("cucumber")
CucumberPlugin.glues := List("steps")