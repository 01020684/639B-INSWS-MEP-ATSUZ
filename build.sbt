name := """atsi"""
organization := "com.eternalsh.okno"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.8"

libraryDependencies ++= Seq(guice, evolutions, jdbc)
libraryDependencies +=  "com.h2database"           %  "h2"                      % "1.4.192"
libraryDependencies +=  "org.playframework.anorm"  %% "anorm"                   % "2.6.7"
libraryDependencies +=  "org.scalatestplus.play"   %% "scalatestplus-play"      % "5.0.0"    % Test

herokuAppName in Compile := "stormy-ridge-04573"
herokuJdkVersion in Compile := "11"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.eternalsh.okno.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.eternalsh.okno.binders._"
