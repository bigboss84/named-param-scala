name := "named-param-scala"

version := "1.1.1"

scalaVersion := "2.12.4"

scalacOptions ++= Seq(
  "-feature", "-language:postfixOps"
)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test
