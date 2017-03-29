import xerial.sbt.Pack.{packExtraClasspath, packJvmOpts, packMain}

resolvers += Resolver.sonatypeRepo("snapshots")



//val scalaV = "2.12.1"
val scalaV = "2.11.8"


val projectName = "flowShow"
val projectVersion = "1.0.0"


val slickV = "3.2.0-M2"

val scalaXmlV = "1.0.6"
val akkaV = "2.4.16"
val akkaHttpV = "10.0.1"
val hikariCpV = "2.5.1"
val logbackV = "1.1.7"
val scalikeJdbcV = "2.5.0"
val nscalaTimeV = "2.14.0"
val codecV = "1.10"
val postgresJdbcV = "9.4.1208"
val asyncHttpClientV = "2.0.24"
val ehCacheV = "2.10.3"


val scalaJsDomV = "0.9.1"
val scalatagsV = "0.6.2"
val circeVersion = "0.6.1"
val diodeV = "1.1.0"

val projectMainClass = "com.neo.sk." + projectName + ".Boot"

//val playComponentV = "2.5.9"


def commonSettings = Seq(
  version := projectVersion,
  scalaVersion := scalaV,
  scalacOptions ++= Seq(
    //"-deprecation",
    "-feature"
  )
)


lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
  .settings(name := "shared")
  .settings(commonSettings: _*)


lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js


// Scala-Js frontend
lazy val frontend = (project in file("frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(name := "frontend")
  .settings(commonSettings: _*)
  .settings(inConfig(Compile)(
    Seq(
      fullOptJS,
      fastOptJS,
      packageScalaJSLauncher,
      packageJSDependencies,
      packageMinifiedJSDependencies
    ).map(f => (crossTarget in f) ~= (_ / "sjsout"))
  ))
  .settings(skip in packageJSDependencies := false)
  .settings(
    persistLauncher in Compile := true,
    persistLauncher in Test := false,
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion,
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomV,
      "me.chrons" %%% "diode" % diodeV,
      //"com.lihaoyi" %%% "upickle" % upickleV,
      "com.lihaoyi" %%% "scalatags" % scalatagsV
      //"com.lihaoyi" %%% "utest" % "0.3.0" % "test"
    )
  )
  .dependsOn(sharedJs)


// Akka Http based backend
lazy val backend = (project in file("backend"))
  .settings(commonSettings: _*)
  .settings(
    Revolver.settings.settings,
    mainClass in Revolver.reStart := Some(projectMainClass)
  )
  .settings(name := "backend")
  .settings(
    //pack
    // If you need to specify main classes manually, use packSettings and packMain
    packSettings,
    // [Optional] Creating `hello` command that calls org.mydomain.Hello#main(Array[String])
    packMain := Map(projectName -> projectMainClass),
    packJvmOpts := Map(projectName -> Seq("-Xmx256m", "-Xms64m")),
    packExtraClasspath := Map(projectName -> Seq("."))
  )
  .settings(
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      //"org.scala-lang" %% "scala-reflect" % scalaV,
      "org.scala-lang.modules" %% "scala-xml" % scalaXmlV,
      "com.typesafe.akka" %% "akka-actor" % akkaV withSources(),
      "com.typesafe.akka" %% "akka-slf4j" % akkaV,
      "com.typesafe.akka" %% "akka-stream" % akkaV,
      "com.typesafe.akka" %% "akka-http-core" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
      "com.typesafe.slick" %% "slick" % slickV withSources(),
      "com.typesafe.slick" %% "slick-codegen" % slickV,
      "org.scalikejdbc" %% "scalikejdbc" % scalikeJdbcV,
      "org.scalikejdbc" %% "scalikejdbc-config" % scalikeJdbcV,
      "com.lihaoyi" %% "scalatags" % scalatagsV,
      "com.github.nscala-time" %% "nscala-time" % nscalaTimeV,
      "com.zaxxer" % "HikariCP" % hikariCpV,
      "ch.qos.logback" % "logback-classic" % logbackV withSources(),
      "commons-codec" % "commons-codec" % codecV,
      "org.postgresql" % "postgresql" % postgresJdbcV,
      "org.asynchttpclient" % "async-http-client" % asyncHttpClientV,
      "net.sf.ehcache" % "ehcache" % ehCacheV
    )
  )
  .settings {
    (resourceGenerators in Compile) += Def.task {
      val fastJsOut = (fastOptJS in Compile in frontend).value.data
      val fastJsSourceMap = fastJsOut.getParentFile / (fastJsOut.getName + ".map")
      Seq(
        fastJsOut,
        fastJsSourceMap
      )
    }.taskValue
  }
  //  .settings(
  //    (resourceGenerators in Compile) += Def.task {
  //      val fullJsOut = (fullOptJS in Compile in frontend).value.data
  //      val fullJsSourceMap = fullJsOut.getParentFile / (fullJsOut.getName + ".map")
  //      Seq(
  //        fullJsOut,
  //        fullJsSourceMap
  //      )
  //    }.taskValue)
  .settings(
  (resourceGenerators in Compile) += Def.task {
    Seq(
      (packageScalaJSLauncher in Compile in frontend).value.data,
      (packageJSDependencies in Compile in frontend).value
      //(packageMinifiedJSDependencies in Compile in frontend).value
    )
  }.taskValue)
  .settings(
    (resourceDirectories in Compile) += (crossTarget in frontend).value,
    watchSources ++= (watchSources in frontend).value
  )
  .dependsOn(sharedJvm)



lazy val root = (project in file("."))
  .aggregate(frontend, backend).settings(name := projectName)




    