import sbt.Keys._
import sbt._
import sbtrelease.Version
import AssemblyPlugin._


name := "gaya"

resolvers += Resolver.sonatypeRepo("public")
scalaVersion := "2.11.8"
releaseNextVersion := { ver => Version(ver).map(_.bumpMinor.string).getOrElse("Error") }
assemblyJarName in assembly := "gaya.jar"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-events" % "1.1.0",
  "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
  "com.amazonaws" % "aws-java-sdk-polly" % "1.11.78",
  "com.typesafe.play" % "play-json_2.11" % "2.5.10",
  "com.typesafe.play" % "play-ws_2.11" % "2.5.10",
  "org.scalaz" %% "scalaz-core" % "7.2.8",
  "net.codingwell" %% "scala-guice" % "4.1.0",
  "com.jsuereth" %% "scala-arm" % "2.0"
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xfatal-warnings"
)

// deduplicate: different file contents found in the following:
// - java.util.zip.ZipException: duplicate entry: META-INF/MANIFEST.MF
// - commons-logging & jcl-over-slf4j
assemblyMergeStrategy in assembly := {
  case m if m.startsWith("META-INF") => MergeStrategy.discard
  case PathList("reference.conf") => MergeStrategy.concat
  case _ => MergeStrategy.first
}
