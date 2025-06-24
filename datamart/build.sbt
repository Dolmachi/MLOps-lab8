/* ───── базовые настройки ─────────────────────────────── */

name         := "DataMart"
version      := "1.0"
scalaVersion := "2.12.15"

val sparkVer = "3.5.6"
val catsVer  = "2.12.0"
val circeVer = "0.14.9"

/* ───── зависимости ───────────────────────────────────── */

libraryDependencies ++= Seq(
  // --- Spark
  (
    "org.apache.spark" %% "spark-sql"   % sparkVer % Provided
  ).exclude("org.slf4j", "slf4j-simple")
   .exclude("org.slf4j", "slf4j-log4j12"),

  (
    "org.apache.spark" %% "spark-mllib" % sparkVer % Provided
  ).exclude("org.slf4j", "slf4j-simple")
   .exclude("org.slf4j", "slf4j-log4j12"),

  "org.apache.spark" %% "spark-kubernetes" % sparkVer % Provided,

  // --- Mongo
  "org.mongodb.spark" %% "mongo-spark-connector" % "10.5.0",
  "org.mongodb"        %  "mongodb-driver-sync"  % "5.1.1",   // версия, которую требует коннектор

  // --- Логи (SLF4J 1.7 + Log4j 2)
  "org.slf4j"                % "slf4j-api"        % "1.7.36",
  "org.apache.logging.log4j" % "log4j-core"       % "2.24.1",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.24.1",

  // --- Akka HTTP
  (
    "com.typesafe.akka" %% "akka-http" % "10.2.10"
  ).exclude("org.slf4j", "slf4j-simple")
   .exclude("org.slf4j", "slf4j-log4j12"),

  (
    "com.typesafe.akka" %% "akka-stream" % "2.6.20"
  ).exclude("org.slf4j", "slf4j-simple")
   .exclude("org.slf4j", "slf4j-log4j12"),

  (
    "com.typesafe.akka" %% "akka-actor-typed" % "2.6.20"
  ).exclude("org.slf4j", "slf4j-simple")
   .exclude("org.slf4j", "slf4j-log4j12"),

  // --- Circe + glue
  (
    "de.heikoseeberger" %% "akka-http-circe" % "1.39.2"
  ).exclude("org.slf4j", "slf4j-simple")
   .exclude("org.slf4j", "slf4j-log4j12"),

  "io.circe" %% "circe-generic" % circeVer,

  // --- Cats (единственная версия)
  "org.typelevel" %% "cats-core"   % catsVer,
  "org.typelevel" %% "cats-kernel" % catsVer,
  "org.typelevel" %% "cats-free"   % catsVer
)

/* ───── фиксируем нужные версии ────────────────────────── */

dependencyOverrides ++= Seq(
  "org.typelevel" %% "cats-core"   % catsVer,
  "org.typelevel" %% "cats-kernel" % catsVer,
  "org.typelevel" %% "cats-free"   % catsVer,
  "org.slf4j"     %  "slf4j-api"   % "1.7.36",
  "org.mongodb"   %  "mongodb-driver-sync" % "5.1.1",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "2.3.0"
)

libraryDependencySchemes +=
  "org.scala-lang.modules" %% "scala-parser-combinators" % VersionScheme.EarlySemVer

/* ───── точка входа ────────────────────────────────────── */

Compile / mainClass := Some("DataMartServer")

/* ───── sbt-assembly ───────────────────────────────────── */

import sbtassembly.{MergeStrategy, PathList}

lazy val keepCatsJars = Set(
  s"cats-core_2.12-$catsVer.jar",
  s"cats-kernel_2.12-$catsVer.jar",
  s"cats-free_2.12-$catsVer.jar"
)

assembly / assemblyExcludedJars := {
  val cp = (assembly / fullClasspath).value
  cp.filter(jar =>
    jar.data.getName.startsWith("cats-") &&
    !keepCatsJars.contains(jar.data.getName)
  )
}

assembly / assemblyMergeStrategy := {
  case PathList("cats", _ @_*)                               => MergeStrategy.last
  case "module-info.class"                                   => MergeStrategy.discard
  case PathList("org","apache","commons","logging", _ @_*)   => MergeStrategy.first
  case PathList("META-INF","services", _ @_*)                => MergeStrategy.concat
  case PathList("META-INF", _ @_*)                           => MergeStrategy.discard
  case "reference.conf" | "application.conf"                 => MergeStrategy.concat
  case x if x.endsWith(".proto") || x.endsWith(".properties")=> MergeStrategy.first
  case x if x.endsWith(".class")                             => MergeStrategy.deduplicate
  case PathList("org","slf4j","impl", _ @_*)                 => MergeStrategy.discard
  case PathList("org","apache","log4j", xs @ _*)
       if xs.contains("Log4j2Plugins.dat")                   => MergeStrategy.first
  case _                                                     => MergeStrategy.first
}

assembly / assemblyOption := (assembly / assemblyOption).value
  .withIncludeScala(false)
  .withIncludeDependency(true)
