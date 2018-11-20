package com.data.a3_collection

/**
  *     1. 可以省掉创建类（类名、类成员）等工作
  *        Scala tuples which are a special case of case classes
  *
  *     2. tuple的索引是基于1的；这是继承与 haskell、ML语言的传统
  *
  *     3. tuple不能使用apply方法：因为apply方法要返回相同的类型，因此不能用 tuple(1)、tuple(2)
  *        tuple也不能混入Iterator
  *
  */
object a3_tuple {

    def operate_tuple() = {
        val pair = (1, "abc")
        println(pair._1, pair._2, pair.getClass)
    }

    def main(args: Array[String]): Unit = {
        operate_tuple()
    }
}
