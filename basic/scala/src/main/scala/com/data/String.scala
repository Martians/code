package com.data

object String {

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

  def regex(): Unit = {

  }
  def main(args: Array[String]): Unit = {
    print_string()
  }
}
