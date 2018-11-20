package com.data.a3_collection

/**
  *     1. 常见操作：
  *         ++ List[T]
  *         keys、keySet
  *
  *     2. 可以重载 default 函数，设置默认返回值
  */
object a3_map {

    def operate_map(): Unit = {
        println("\noperate_map:")
        import scala.collection.immutable.Map

        /**
          * map应该定义为 var map
          *     1. map是immutable无法就地修改
          *     2. += 需要对map重新赋值，如果是 val将无法 成功
          *
          *     这里实际是一个语法糖 P228
          */
        var map = Map(1 -> "go to", 2 -> "Dig", 3 -> "find")
        map += 4 -> "bb"
        println(map)
        println(map(4))
    }

    def mutable_map() {
        println("\nmutable_map:")

        /**
          * 默认是不可变集合，要使用可变集合，需要引入
          */
        import scala.collection.mutable.Map

        /**
          * 初始化：
          *     1. 对整形调用了 -> 方法，(1).->("go to")
          *        任何对象都能调用 -> , 转换为包含键值对的二元组
          *     2. 然后调用了object Map的apply工厂方法
          */
        val map = Map(1 -> "go to", 2 -> "Dig", 3 -> "find")

        /**
          * 这里执行了隐式转换，scala中允许任何对象用 ->
          *     使用 +=, 覆盖Key之前已经存在的value
          */
        map += (3 -> "get it")
        println(map)
    }

    def operate_map2() {
        println("\noperate_map:")
        import scala.collection.mutable.Map

        val a = Map("a" -> 1, "2" -> 3, "b" -> 5)
        val d = a - "a" + ("b" -> 3)
        println("map dec inc: ")
        d.foreach(x => println("\t" + x))

        if (a.contains("2")) {
            val c = a.get("2").get
            val b = a - "2" + ("2" -> (c + 1))
            println("map inc value: ")
            b.foreach(x => println("\t" + x))

        } else {
            println("nothing")
        }

        /**
          * 初始化2：
          *     创建空map
          */
        val em = Map.empty[String, Int]
        em += "a" -> 1

        /**
          * 直接添加List
          *     1. 键值对方式写出，("b", 2)
          *     2. 更普遍的："c" -> 3
          */
        em ++= List(("b", 2), "c" -> 3)
        println("mutable: " + em)

        println("key set: " + em.keySet)

        println("key iterator: " + em.keys)
        println("value iterator: " + em.values)

    }

    def main(args: Array[String]): Unit = {
        operate_map()

        mutable_map()

        operate_map2()
    }
}
