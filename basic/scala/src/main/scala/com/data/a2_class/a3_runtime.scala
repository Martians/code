package com.data.a2_class

/**
  * http://hongjiang.info/scala-type-and-class/
  * http://blog.csdn.net/a2011480169/article/details/52960101
  * http://blog.csdn.net/sinat_25306771/article/details/52004355
  */
object a3_runtime {

    def class_runtime(): Unit = {
        case class AType(d: Int)

        val a = new AType(1)
        println(a.getClass())
        println(classOf[AType])


        val x = Array[String]()
        println("cury: " + x.getClass)
        println(x.getClass.getSimpleName)
        println(x.asInstanceOf[AnyRef].getClass.getSimpleName)
        println(classOf[Array[String]])
        /*
            import scala.reflect.runtime.universe._
            import scala.reflect.api.TypeTags
            def paramInfo[T](x: T)(implicit tag: TypeTag[T]): Unit = {
              val targs = tag.type match { case TypeRef(_, _, args) => args }
              println(s"type of $x has type arguments $targs")
            }

            paramInfo(42)
            paramInfo(List(1, 2))
            */
    }
}
