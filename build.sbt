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
  val sprayV = "1.3.3"
  val upickleVersion = "0.3.4"

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

    "org.iq80.leveldb"    %  "leveldb"                  % "0.7",
    "org.fusesource.leveldbjni" % "leveldbjni-all"      % "1.8",
    "org.scalatest"       %% "scalatest"                % "2.2.1"  % "test",

    ("ch.qos.logback"     %  "logback-classic"          % "1.1.3").exclude("org.slf4j", "slf4j-api"),
    "com.typesafe.scala-logging" %% "scala-logging"     % "3.1.0"
  )
}


assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

//test in assembly := {}

Revolver.settings