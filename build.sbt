enablePlugins(JmhPlugin)

version:= "0.0.0"

scalaVersion := "2.12.7"

name := "random-benchmarks"

scalacOptions in Global ++= (
  "-deprecation" ::
  "-encoding" :: "UTF-8" ::
  "-unchecked" ::
  "-feature" ::
  "-target:jvm-1.8" ::
  "-Xlint" ::
  "-Xfuture" ::
  //"-Xlog-implicits" ::
  //"-Yno-predef" ::
  //"-Yno-imports" ::
  "-Xfatal-warnings" ::
  "-Yno-adapted-args" ::
  "-Ywarn-dead-code" ::
  "-Ywarn-nullary-override" ::
  "-Ywarn-nullary-unit" ::
  "-Ywarn-numeric-widen" ::
  "-Ywarn-value-discard" ::
  Nil)

javaOptions in Global ++= (
  "-server" ::
    //"-verbose:gc" ::
    //"-Xms512M" ::
    //"-Xmx512M" ::
    //"-XX:NewRatio=1" ::
    //"-XX:CompileThreshold=100" ::
    //"-XX:+PrintCompilation" ::
    //"-XX:+PrintGCDetails" ::
    //"-XX:+UseParallelGC" ::
    Nil)

resolvers ++= (Nil)

