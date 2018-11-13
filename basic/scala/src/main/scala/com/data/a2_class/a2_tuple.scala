package com.data.a2_class

/**
  *  Scala tuples which are a special case of case classes
  *
  *  tuple的索引是基于1的；这是继承与 haskell、ML语言的传统
  */
object a2_tuple {
    /**
      * tuple不能使用apply方法：因为apply方法要返回相同的类型，因此不能用 tuple(1)、tuple(2)
      *
      */
    def operate_tuple() = {
        val pair = (1, "abc")
        println(pair._1, pair._2, pair.getClass)
    }

    def main(args: Array[String]): Unit = {
        operate_tuple()
    }
}
