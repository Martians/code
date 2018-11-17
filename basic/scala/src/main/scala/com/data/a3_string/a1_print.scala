package com.data.a3_string

object a1_print extends App {

    /**
      * mkString 可以指定 prefix、sep、suffix
      */
    def operate_print() = {
        println("\noperate_print:")

        val x = List(1, 2, 3, 4)
        println(x.mkString("==", ", ", "<<"))
    }

    operate_print
}
