name := """DeviceMonitor"""
organization := "wasp.se"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  guice,
  javaJpa,
  javaJdbc,
  "com.h2database" % "h2" % "1.4.199",
  "org.hibernate" % "hibernate-core" % "5.4.9.Final",
  "mysql" % "mysql-connector-java" % "5.1.41",
  javaWs % "test",
  "org.awaitility" % "awaitility" % "4.0.1" % "test",
  "org.assertj" % "assertj-core" % "3.14.0" % "test",
  "org.mockito" % "mockito-core" % "3.1.0" % "test",
)
