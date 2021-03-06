import sbtassembly.AssemblyPlugin.autoImport._
import com.typesafe.sbt.SbtNativePackager.autoImport._

name          := "gitlab-api-akka-proxy"
version       := "0.1"
scalaVersion  := "2.11.8"
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")
maintainer    := "reinno reinno@126.com"

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
                  Resolver.bintrayRepo("hseeberger", "maven"))

libraryDependencies ++= {
  val akkaV = "2.4.3"
  val json4sV = "3.3.0"

  Seq(
    "com.typesafe.akka"   %% "akka-actor"               % akkaV,
    "com.typesafe.akka"   %% "akka-testkit"             % akkaV    % "test",
    ("com.typesafe.akka"  %% "akka-slf4j"               % akkaV).exclude("org.slf4j", "slf4j-api"),
    "com.typesafe.akka"   %% "akka-persistence"         % akkaV,
    "com.typesafe.akka"   %% "akka-http-experimental"   % akkaV,
    "com.typesafe.akka"   %% "akka-http-core"           % akkaV,
    "com.typesafe.akka"   %% "akka-stream"              % akkaV,
    "com.typesafe.akka"   %% "akka-http-testkit"        % akkaV    % "test",
    "com.typesafe.akka"   %% "akka-http-spray-json-experimental" % akkaV,


    "org.json4s"          %% "json4s-native"            % json4sV,
    "org.json4s"          %% "json4s-jackson"           % json4sV,
    "de.heikoseeberger"   %% "akka-http-json4s"         % "1.6.0",

    "org.iq80.leveldb"    %  "leveldb"                  % "0.7",
    "org.fusesource.leveldbjni" % "leveldbjni-all"      % "1.8",
    "org.scalatest"       %% "scalatest"                % "2.2.1"  % "test",

    ("ch.qos.logback"     %  "logback-classic"          % "1.1.3").exclude("org.slf4j", "slf4j-api"),
    "com.typesafe.scala-logging" %% "scala-logging"     % "3.1.0"
  )
}

assemblyMergeStrategy in assembly := {
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case PathList("META-INF", "CHANGES.txt")           => MergeStrategy.discard
  case PathList("META-INF", "LICENSES.txt")          => MergeStrategy.discard
  case "application.conf"                            => MergeStrategy.concat
  case "unwanted.txt"                                => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

val classPath = Seq(
  "."
)

packageOptions += Package.ManifestAttributes(
  "Class-Path" -> classPath.mkString(" ")
)

//test in assembly := {}

Revolver.settings