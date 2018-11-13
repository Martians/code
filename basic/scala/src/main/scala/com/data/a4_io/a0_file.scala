package com.data.a4_io

/**
  * P34
  */
object a0_file {

  def operate_file(): Unit = {
    val filelist = (new java.io.File(".")).listFiles
    for (file <- filelist) println(file)
  }

  def operate_source(): Unit = {
    import scala.io.Source

    for (line <- Source.fromFile("pom.xml").getLines())
      println(line)
  }

  def main(args: Array[String]): Unit = {
    operate_file()
  }
}
