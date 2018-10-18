package com.data

object Collect {

  // list 相当于stack
  def operate_list() {
    println("\noperate_list:")
    val a = List(1, 2, 3)
    val b = List(4, 5, 6)

    // 1) con item
    val c1 = 5 :: a; val c2 = a.::(5)
    println("con item: " + c1 + ", " + c2)

    // 2) con list
    val d = a ::: b
    println("con list: " + d)

    // 3) string
    println("mkString: " + d.mkString(" + "))

    // 4) reverse
    println("reverse:  " + a.reverse)
  }

   def operate_map() {
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
  }

  def operate_work(): Unit = {
    println("\noperate_work:")

    //  1) 修改后生成新的集合，所以这里使用 var，jetSet指向了新的集合，原集合不变
    //      如果使用mutable对象，将还是原来的对象
    var jetSet = Set("Boeing", "Airbus")
    jetSet += "Lear"
  }

  def main(args: Array[String]): Unit = {
    operate_list()
    operate_map ()

    operate_work()
  }
}
