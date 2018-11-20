package com.data.a3_collection

/**
  * 1. array
  *     数组内容是可变的，不是推荐使用的结构
  *     创建后长度不可变
  *
  * 2. ArrayBuffer P218
  *     空间自增长
  */
object a1_array {
    val data = new Array[String](3)

    def operate_base(): Unit = {
        /**
          * 对象的值参数应用（对象的值）
          *     array是通过（）访问的，而不是[], scals中没有操作符，都是方法调用
          *     用括号将一个、多个参数传递给变量时，实际调用的是 变量的.apply方法调用 P24
          */
        println(data(1))
        data.apply(1)

        /**
          * 用括号传递给变量，并且带若干参数的变量赋值，将被转换为 .update 调用
          */
        data(1) = "abc"
        data.update(1, "new value")

        /**
          * 初始化方法：
          *     1. 编译器根据值类型，推断出了数组类型
          *     2. 这里实际调用了 伴生对象 Array 的 apply 方法（是个工厂方法），可以有不定个数的参数
          */
        val num = Array("one", "two", "three")
    }

    def operate_array() {
        println("\noperate_array:")

        /**
          * str是val，但是其指向的值可以变更
          */
        val str = new Array[String](3)
        str(0) = "1"
        str(1) = "2"
        str.foreach(println)

        val x = Array("1", "2", "3")
        val t = Array[String]("1", "2", "3")

        val y = Array.apply("1", "2", "3")
        val z = Array.apply[String]("1", "2", "3")

        // 错误，Array class并没有这个构造函数
        //val x = new Array[String]("1", "2", "3")
    }

    def main(args: Array[String]): Unit = {
        operate_base
        operate_array
    }
}
