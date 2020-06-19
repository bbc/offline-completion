name := "offline-completion"

version := "0.1"

scalaVersion := "2.11.12"
scalacOptions ++= Seq("-feature", "-language:implicitConversions", "-deprecation")

libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.6.7"