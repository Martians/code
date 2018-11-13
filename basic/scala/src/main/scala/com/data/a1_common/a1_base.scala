package com.data.a1_common

/**
  * 小结：
  *     1. val、var
  *     2. 操作符实际上就是调用函数，前缀、中缀、后缀
  *        变量上执行()，实际是调用apply方法
  *
  * 每个文件都隐含包含了 java.lang、Predef的成员引用（包含println、assert）
  *
  * 应用程序编写：
  *     1. 常规：编写main方法
  *     2. 简化：object混入 App 特质接口；但不可访问命令行参数了；通常只用于简单程序时使用 P45
  */
object a1_base extends App {
    /**
      * val：是指变量本身不可改变，但是其指向的对象，内部内容是可以修改的
      *     仅表示，变量不可再次赋值
      */
    def operate_primary() = {
        val a = 1
        val b = 2
        println(a.max(b))

        /**
          * 基本类型，定位为抽象、final的，不能直接new
          * 1）new Int(5)：调用的将是 object.apply
          * 2) c = 5：调用的是？
          */
        //val c = new Int
    }

    /**
      * 没有int，只有Int
      */
    def operate_type() = {

        val x: Int = 100

        /**
          * 两种定义方式一样
          */
        //    val x = new util.HashMap[Int, String]()
        //    val y: Map[Int, String] = new util.HashMap()
    }

    /**
      * 变量名覆盖
      *     在内部范围中，可以定义与内部范围相同的变量名字
      */
    def operate_scope(): Unit = {
        val a = 1;
        {
            val a = 2
            println(a)
        }
        println(a)
    }

    def syntax_operator() = {
        /**
          * 任何方法都可以是操作符
          *     使用何种形式，方法和操作符，取决于如何使用：s.indexOf(5)，或者 s indexOf 5
          *
          * 前缀、中缀、后缀操作符 P53
          */
        if (true) {
            println("\nsyntax_operator:")
            /**
              * 中缀操作符
              *     为1的int对象，调用了+方法，参数是Int对象2
              */
            val x1 = 1 + 2
            val y1 = (1).+(2)

            /**
              * 任何带参数方法都可以是中缀操作符
              */
            val s = "it is a good day"
            val s0 = s indexOf 'a' //

            /**
              * 后缀操作符
              *     1. 没有副作用，可以不使用()，惯例
              *     2. 方法当后缀做操作符使用
              */
            val s1 = s.toLowerCase()
            val s2 = s.toLowerCase
            val s3 = s toLowerCase

            /**
              * 前缀操作符
              *     只有：+ - ! ~ 四种，注意，这里没有*；转换为相应的函数调用 P53
              */
            val x2 = -0.2
            val y2 = (0.2).unary_-

            /**
              * 特例：以：结尾的操作符，由其右侧操作数调用，传入左侧操作数
              */
            val a = List(1, 2, 3)
            val b = List(4, 5, 6)
            println("list cons a:::b:  " + (a ::: b))
            println("equal - b.:::(a): " + (b.:::(a)))
        }

        /**
          * 在对象后跟()，实际执行的转换方式
          */
        if (true) {
            println("")
            val x = Array("1", "2", "3")

            /**
              * 任何对某些在括号中的参数的对象的应用,将都被转换为对 apply 方法的调用
              */
            val y1 = x(1)
            val y2 = x.apply(1)

            /**
              * 当对带有括号，并包括一到若干参数的变量赋值时，带有括号里参数和等号右边的对象的 update 方法的调用
              */
            x(1) = "1"
            x.update(1, "2")
        }

        /**
          * 相等性比较；对于引用而言：java中比较的是参考相等性（是否是同一个对象）
          */
        if (true) {
            println("\nequality:")

            // 值相等性
            println("equality: " + (1 == 1.0)) // true，精度取决于第一个参数, 1.equal(1.0)
            println("equality: " + (1 == List(1, 2, 3))) // false，任何对象都可以比较，都是从Unit继承的？
        }
    }

    /**
      * package (P176)
      */
    def syntax_package(): Unit = {
        import java.util.{Formatter => _} // 排除 Formatter
        import java.util.{Formatter => abc} // 重命名
        //import Fruits.{Apple => McIntosh, _}
        //import Fruits.{Pear => _, _}
    }

    operate_primary
    operate_type
    syntax_operator
    syntax_package
}
