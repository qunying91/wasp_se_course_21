name := """DeviceMonitor"""
organization := "wasp.se"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  guice,
  javaJpa,
  "org.hibernate" % "hibernate-core" % "5.4.9.Final",
)
