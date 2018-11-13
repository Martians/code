package com.data.a1_common

/**
  * 小结：
  *     1. 函数文本、函数值
  *     2. 简化函数：目标类型化、占位符、偏应用函数
  *     3. 闭包、重复参数、尾递归
  *     4. 新建控制结构：cury、叫名参数
  *
  * 总体：
  *     1. Scala 中的函数则是一个完整的对象，Scala 中的函数其实就是继承了 Trait 的类的对象
  *
  * 设计：
  *     1. 尽量让方法没有副作用，返回值是唯一目的
  *         这样可以降低方法之间的耦合度，更加可靠，易于重用 (P26)
  *
  *     2. 尽量不在函数中明确写出返回值，将每个方法当做是创建返回值的表达式
  *         鼓励程序员，将较大的方法分解为多个更小的方法
  *
  *     3. 函数式风格和指令式风格：包含了var的，很可能就是 指令式风格
  *          函数是否有副作用：可以通过返回类型是否为Unit来判断
  *
  * 说明：
  *     1. 方法的形参，默认都是val的，不能够重新赋值
  *     2. 方法与函数：
  *             如果去掉了=, 那么就是Unit返回值，必然是为了产生副作用，调用时必须带()？
  *             函数可作为一个参数传入到方法中，而方法不行
  */
object a3_function extends App {
    println("\na3_function:")

    /**
      * P 114 scala 拥有第一类函数
      *     函数还可以写成没有名字的文本，函数文本和函数值类似于，类和对象的关系
      *     函数文本（存在于源代码中）被编译进一个类，运行期成为函数值
      */
    def operator_func()= {
        /**
          * 函数值是对象，因此可以存入对象
          *     任何函数都是扩展了 FunctionN 特性之一的实例，因此函数值可转换为 function类型 的 value P114
          */
        val h = (x: Int) => x + 1

        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        /**
          * 简化函数
          *
          *     1. 目标类型化：通过表达式的目标使用位置，决定了参数的类型，此时可以省略形参类型 P116
          *                 只有一个参数，可以省略被推断形参之外的括号
          */
        val someNumbers = List(-11, -10, -5, 0, 5, 10)
        someNumbers.filter(x => x > 0)

        /**     2. 占位符语法：每个参数在函数文本中仅出现一次, 可以一次用 _ 代替
          * */
        val d = someNumbers.filter(_ > 0)
        /**
          * f = _ + _
          *             第二个例子：
          *             这里是错误的，编译器没有足够的类型来推断（如果有目标类型化，也可以自行推断出来）
          *             一个可行的方式是，在函数执行体中，指出参数类型（而不是在形参部分）
          **/
        val f = (_: Int) + (_: Int)

        /**
          *     3. 偏应用函数 P117
          *           这是一个表达式，没有给函数提供所有参数，仅提供部分
          *
          *     示例解释：
          *           1）使用_代替所有参数，而不是仅仅一个; _作为整个参数列表的占位符
          *              另：println和_之间有一个空格，防止被当做 println_ 函数原型（防止编译器认为是定义的一个新函数名）
          *
          *           2) 如果目标位置正好是需要一个函数（而不是函数的执行结果）
          *              可以进一步简化, _可以省略 _ 符号
          *
          *              注意：如果目标位置需要一个值，如println，那么就不能省略 _, 否则println出来的是 <function> 这个类型
          *                 println(list.tail)     打印函数类型
          *                 println(list.tail _)   打印最后一个元素
          */
        someNumbers.foreach(println _)
        someNumbers.foreach(println)

        def sum(a: Int, b: Int, c: Int) = a + b + c
        /**
          *     过程说明
          *           1）a是偏应用函数，指向一个函数值对象，是从表达式 sum _自动生成的类的一个实例
          *              这个类有一个apply方法，以及三个参数，a(1, 2, 3)被翻译成 a.apply(1, 2, 3)
          *              实际上a混入了特质 Function3
          *
          *           2) b相对于sum，指定了部分参数；固定部分参数，生成新的函数值
          */
        val a = sum _
        val b = sum(1, _: Int, 3)

        /**
          *     转换函数
          *           通常不能将方法、嵌套函数直接赋值给变量
          *           但是通过偏应用函数，将def包装为函数值
          */
        def p (x: Int): Int = { 1 + x }
        val c = p _
        c(5)
    }

    /**
      * 闭包：P120
      *     1. 函数文本在运行时创建的函数值（对象），编写的时候代码尚未封闭
      *     2. 通过捕获自由变量的绑定，对函数文本执行的“关闭”行动
      *     3. 函数值是“关闭”这个行动的最终产物，得到的函数中，将包含指向捕获变量的参考
      *
      */
    def operator_closure() = {
        println("\noperator_closure:")

        /**
          * 这不是闭包，在编写的时候就已经封闭了
          */
        val t1 = (x: Int) => x + 1

        var more = 1
        val addMore = (x: Int) => x + more

        /**
          * 可以改变被捕获的，自由变量的值，这与java不同
          *     可以认为，捕获的是变量本身，而不是变量的值
          */
        val someNumbers = List(-11, -10, -5, 0, 5, 10)
        var sum = 0
        someNumbers.foreach(sum += _)
        println("sum: " + sum)

        /**
          * 可以生成函数的多个版本（例子中，生成的闭包是一个函数，捕获的是函数的形参，也是函数的局部变量），机制类似于go
          *
          *     1）定义了一个函数，函数体是返回一个闭包; 生成闭包时，使用了函数的 本地变量 more
          *     2）使用的实例，是在闭包创建时候活跃的；捕获当前值，作为more的绑定的闭包，被创建并返回
          *        这里有多个more，捕获的参数自动存在于堆中，因此可以表现出不同的绑定
          *
          *     实际上，scala编译器重新安排了它捕获的变量继续存在于堆中，而不是堆栈
          */
        def makeIncreaser(more: Int) = (x: Int) => x + more
        val inc1 = makeIncreaser(1)
        val inc99 = makeIncreaser(99)
    }

    /**
      * 重复参数
      *     实际上参数的类型是：数组
      */
    def operator_param(): Unit = {
        println("\noperator_param:")

        def echo(args: String*): Unit = {
            print(" echo: ")
            for (arg <- args) print(arg)
            println("")
        }
        print("multi param"); echo("1", "2", "3")

        /**
          * 实参直接传入数组，无法编译
          *
          *     使用_*：告知编译器，将数组的每个元素当做参数传入
          */
        print("array param"); echo(Array("1", "2", "3"): _*)
    }

    /**
      * 尾递归 (P123)
      *     函数体执行的最后一件是事情，就是调用自己的函数
      *     可以看到例子中，堆栈部分并不是处于层层调用 bang 中
      *
      *     设计建议：
      *         1）尽量用递归代替循环
      *         2）如果可以转换为尾递归，scala会进行优化，性能与循环完全相同
      *
      */
    def operator_recursive() = {

        if (false) {
            def bang(x: Int): Int =
                if (x == 0) throw new Exception("bang!")
                else bang(x - 1)
            bang(5)
        }
    }

    /**
      * 高阶函数：
      *     相当于将代码执行中的算法部分抽离出来，当做参数传入，减少代码重复
      */
    def operator_abstract(): Unit = {
        println("\nsyntax_abstract:")

        if (true) {
            val a = List(1, 2, 3, 4, 5)
            println("list exist(_ > 4): " + a.exists(_ > 4))
        }

        /**
          * 简化代码：
          *     过程中，使用闭包可以有效的减少代码重复, P128
          *     1. filesMatching：根据传入的高阶函数，执行不同的算法
          *        filesEnding：将一个的高阶函数，传入给filesMatching，得到一种语义
          *
          *     2. query 被绑定在传入的函数中，query只在 _.endsWith(query) 这个闭包中用到；绑定函数，封装了一些信息进去
          *        filesMatching(x => x.endsWith(query))，绑定了filesEnding中的局部变量 query
          */
        val files = List("1", "abc");
        def filesMatching(matcher: String => Boolean) =
            for (file <- files; if matcher(file)) yield file

        def filesEnding(query: String) =
            filesMatching(_.endsWith(query))

        println("file match: " + filesEnding("abc"))

        /**
          * Curry
          *     目的：让控制抽象，感觉像是语言的扩展
          *     相当于将一个“多层参数”的函数拆分成多个
          */
        if (true) {
            def sum1(x: Int, y: Int) = x + y
            def sum2(x: Int)(y: Int) = x + y

            /**
              *     1. a 的类型是一个函数 (Int) => Int
              *     2. 第二个参数使用占位符，使用了偏应用函数的方式, 使用_代替了剩余的所有参数
              *        这里sum2(2) 和 _ 之间可以不要空格，因为sum2(2)_本身就不是一个合法标识符，不像 println_
              */
            val a = sum2(2)_
            println("curry handle: " + a(3))
        }

        /**
          * 如果要新的內建控制结构：
          *     方式1：cury  P132
          *
          *         语言中的控制结构，通常是控制语句之后，使用 {} 的代码块，步骤如下：
          *         1）将函数分解成多个独立的参数
          *         2）使用cury，将函数变成多个参数的函数；因此对最后一个参数，可以使用 {}
          */
        if (true) {
            /**
              * 1）只有一个参数时：可以用{} 代替(), 看起来更像內建控制结构
              */
            println("absctract, use println(): " + "hello world!")
            println{"absctract, use println{}: " + "hello world!"}

            /**
              * 2）为了让有多个参数的函数，也能看起来像控制结构，可以使用cury后，调用时对最后一个参数使用 {}
              */
            def handle1(a: Int, work: Int => Int) = { work(a) }
            def handle2(a: Int)(work: Int => Int) = { work(a) }
            /**
              * 这个语句，实际是一个函数调用；最后一个参数放在{}中了
              */
            handle2(5) { x => x + 10 }
        }

        /**
          *    方式2：叫名参数 P134
          *         例子：实现一个类似assert的函数，在release版本下不启用；推迟了参数的 evaluation
          *
          *         1. 参数定义时，使用 =>type，将本来函数的参数（参数是一个函数），转换为叫名参数
          *         2. 这将使得传入的参数，不论是什么样（即使是一个表达式，而不是函数），都会被转换为一个函数值
          *         3. 因为是函数，只有调用时才会进行评估
          */
        if (true) {
            val assert = false
            /**
              * 普通参数：这里 predicate没有括号
              */
            def myAssert0(predicate: Boolean) = {
                if (assert && !predicate) {
                    println(assert)
                }
            }
            /**
              * 函数参数：这里 predicate必须有括号
              */
            def myAssert1(predicate: () => Boolean) = {
                if (assert && !predicate()) {
                    println(assert)
                }
            }
            /**
              * 叫名函数：类型始于=> 而不是 () =>
              */
            def myAssert2(predicate: => Boolean) = {
                /**
                  * 实际调用时不是一个普通变量，而是一个函数值，此时才开始计算值
                  *     但是写法上，不需要括号，像一个普通变量一样
                  */
                if (assert && !predicate) {
                    println(assert)
                }
            }
            /**
              * 1. 函数参数值（这里是表达式 5 > 3）先于函数调用被评估
              * 2. 原始使用方式，必须手动编写一个函数值传入
              * 3. 延迟评估，创建一个函数值，传递给myAssert2；函数值的apply方法将评估 5 > 3
              */
            myAssert0(5 > 3)
            myAssert1(() => 5 > 3)
            myAssert2(5 > 3)
        }
    }

    operator_func
    operator_closure
    operator_param
    operator_recursive
    operator_abstract
}
