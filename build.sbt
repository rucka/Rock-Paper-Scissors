organization  := "mycoachfootball"

name := "Rock, Paper, Scissors"

version := "1.0"

scalaVersion  := "2.11.7"

scalacOptions := Seq("-language:postfixOps", "-feature", "-deprecation", "-language:implicitConversions")

libraryDependencies ++= {
  val ScalaTest     = "2.2.4"
  val ScalaCheck    = "1.12.5"
  Seq(
    "org.scalatest" %% "scalatest" % ScalaTest % "test",
    "org.scalacheck" %% "scalacheck" % ScalaCheck % "test"
  )
}