name := "SearchEngine"

scalaVersion := "2.10.4"
version := "1.0"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.5.1"
libraryDependencies +="org.apache.spark" % "spark-streaming_2.10" % "1.5.1"
libraryDependencies += "org.apache.spark" % "spark-mllib_2.10" % "1.6.0"
