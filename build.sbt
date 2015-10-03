name := "triee"

version := "0.1"

scalaVersion := "2.11.7"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-Xcheckinit",
  "-Xlint",
  "-Xverify",
  "-Yclosure-elim",
  "-Yinline",
  "-Yno-adapted-args",
  "-encoding", "utf8")

resolvers += Resolver.sonatypeRepo("public")

libraryDependencies := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    // if scala 2.11+ is used, add dependency on scala-xml module
    case Some((2, scalaMajor)) if scalaMajor >= 11 =>
      libraryDependencies.value ++ Seq(
        "org.scala-lang.modules" %% "scala-xml" % "1.0.3"
        , "com.github.scopt" %% "scopt" % "3.3.0"
      )
    case _ =>
      // or just libraryDependencies.value if you don't depend on scala-swing
      libraryDependencies.value
  }
}
