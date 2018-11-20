package com.data.a3_string

/**
  * 来自于 java.lang
  *
  * 使用原生字符串，第二行不像使用前导空格，就在下一行开头加上|, P51
  */
object a0_string {

  def print_string(): Unit = {
    println("\nprint_string:")

    println("+*" * 10)

    // 使用原始字符串，并且把输出前端的空白用|隔绝，stripMargin
    println(
      """111111
        |222222
        |333333
      """.stripMargin)

    println("\\")
  }

  def convert(): Unit = {
      println("1".toInt)
  }

  def main(args: Array[String]): Unit = {
    print_string
  }
}
