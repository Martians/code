
object Mutable {

  def operate_array() {
    println("\noperate_array:")

    val str = new Array[String](3)
    str(0) = "1"    // str是val，但是其指向的值可以变更
    str(1) = "2"
    str.foreach(println)

    val x = Array("1", "2", "3")
    val y = Array.apply("1", "2", "3")
    val z = Array.apply[String]("1", "2", "3")

    // 错误，Array class并没有这个构造函数
    //val x = new Array[String]("1", "2", "3")

  }

  def operate_map() {
    println("\noperate_map:")

    import scala.collection.mutable.Map
    // 对整形调用了 -> 方法，(1).->("go to")
    val map = Map(1 -> "go to", 2 -> "Dig", 3 -> "find")

    // 这里执行了隐式转换，scala中允许任何对象用 ->
    map += (3 -> "get it")    // 使用 +=, 覆盖Key之前已经存在的value
    println(map)
  }

  def main(args: Array[String]): Unit = {
    operate_array()
    operate_map()

    val ss = (1-> 2)
    //ss += (3 -> 4)
  }
}
