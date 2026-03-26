
val toolkitV = "0.5.0"
val toolkit = "org.scala-lang" %% "toolkit" % toolkitV
val toolkitTest = "org.scala-lang" %% "toolkit-test" % toolkitV

ThisBuild / scalaVersion := "3.3.7"
libraryDependencies += toolkit
libraryDependencies += (toolkitTest % Test)

libraryDependencies ++= Seq(
	"com.lihaoyi" %% "mainargs" % "0.7.8",
	"com.github.haifengl" %% "smile-scala" % "5.2.2",
	"org.slf4j" % "slf4j-simple" % "2.0.17",
	"org.apache.commons" % "commons-csv" % "1.10.0"
)
