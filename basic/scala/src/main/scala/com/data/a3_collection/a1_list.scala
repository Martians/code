package com.data.a3_collection

/**
  * list是immutable类型，对应于mutable的array
  *
  * list被定义为只能像stack一样使用
  *     head：第一个元素
  *     tail: 除第一个元素之外的list， head + tail = list
  *     init：除最后一个元素之外的list
  *     last：最后一个元素
  *
  *     head + init 构成完整list
  *
  * Nil是空列表
  *
  */
object a1_list {

    def operate_list() {
        println("\noperate_list:")
        val a = List(1, 2, 3)
        val b = List(4, 5, 6)

        /**
          * 初始化：结尾必须有个Nil，只有Nil有::方法
          */
        val c = 1::2::3::Nil

        // 1) con item
        /**
          * :: 是右操作数的方法
          *
          * immutable类型也可以追加，是因为将生成一个新的list
          */
        val c1 = 5 :: a
        val c2 = a.::(5)
        println("con item: " + c1 + ", " + c2)

        // 2) con list
        val d = a ::: b
        println("con list: " + d)

        // 3) string
        println("mkString: " + d.mkString(" + "))

        // 4) reverse
        println("reverse:  " + a.reverse)
    }

    def main(args: Array[String]): Unit = {
        operate_list()
    }
}
