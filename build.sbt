val dottyVersion = "0.22.0-RC1"

lazy val root = project
  .in(file("."))
  .settings(
    organization := "com.phenan",
    name := "magic-mirror",
    version := "0.8.4",
    licenses += ("MIT", url("http://opensource.org/licenses/MIT")),

    scalaVersion := dottyVersion,

    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
  )
