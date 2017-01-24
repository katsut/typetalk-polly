// #### serverless framework template

import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._
import sbt.Keys._
import sbt._
import sbtrelease.Version


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
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.8.4",
  "net.codingwell" %% "scala-guice" % "4.1.0"
  //  ,
  //  "com.jsuereth" %% "scala-arm" % "2.0"
).map{
  _.exclude("commons-logging", "commons-logging")
}

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xfatal-warnings"
)

// deduplicate: different file contents found in the following:
// - java.util.zip.ZipException: duplicate entry: META-INF/MANIFEST.MF
// - commons-logging & jcl-over-slf4j2e
assemblyMergeStrategy in assembly := {
  case PathList("reference.conf") => MergeStrategy.concat
  case m if m.startsWith("META-INF") => MergeStrategy.discard
  case _ => MergeStrategy.first
}

SbtScalariform.scalariformSettings ++ Seq(
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentClassDeclaration, true)
)
