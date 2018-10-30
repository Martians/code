package com.data.a1_common

object a2_syntax {

  def syntax_operator() = {

    // 前缀、中缀、后缀操作符
    if (true) {
      println("\nsyntax_operator:")
      // 中缀操作符，等价于
      val x1 = 1 + 2
      val y1 = (1).+(2) //值为1的int对象，调用了+方法，参数是Int对象2

      val s = "it is a good day"
      val s0 = s indexOf 'a'  //任何带参数方法都可以是中缀操作符

      // 后缀操作符
      val s1 = s.toLowerCase()
      val s2 = s.toLowerCase  //没有副作用，可以不使用()，惯例
      val s3 = s toLowerCase  //方法当后缀做操作符使用

      // 前缀操作符，+-!~，注意，这里没有*；转换为相应的函数调用
      val x2 = -0.2
      val y2 = (0.2).unary_-

      // 特例：以：结尾的操作符，由其右侧操作数调用，传入左侧操作数
      val a = List(1, 2, 3)
      val b = List(4, 5, 6)
      println("list cons a:::b:  " + (a ::: b))
      println("equal - b.:::(a): " +(b.:::(a)))
    }

    // 对象后跟()，转换方式
    if (true) {
      println("")
      val x = Array("1", "2", "3")

      // 1) 任何对某些在括号中的参数的对象的应用,将都被转换为对 apply 方法的调用
      val y1 = x(1)
      val y2 = x.apply(1)

      // 2) 当对带有括号并包括一到若干参数的变量赋值时，带有括号里参数和等号右边的对象的 update 方法的调用
      x(1) = "1"
      x.update(1, "2")
    }

    // 相等性比较；对于引用而言：java中比较的是参考相等性（是否是同一个对象）
    if (true) {
      println("\nequality:")
      // 引用相等性

      // 值相等性
      println("equality: " + (1 == 1.0))            // true，精度取决于第一个参数, 1.equal(1.0)
      println("equality: " + (1 == List(1, 2, 3)))  // false，任何对象都可以比较，都是从Unit继承的？
    }
  }

  def syntax_function(): Unit = {
    println("\nsyntax_function:")
    // 有名函数（使用def定义的）、无名函数
    val list = List(1, 2, 3, 4)

    // 函数定义
    if (true) {
      // 上课用def，本地函数：内部的函数
      val x = 100
      def process_data(): Unit = {
        val y = x             // 闭包
        println("inner function, get outer value: " + y)
      }

      // 函数值，即无名函数
      // => 含义是，将左边的值，转换为右边的值；将函数值赋值给变量
      val handle = (x: Int) => x + 10
    }

    // 简化函数的方法
    if (true) {
      // 目标类型化，即根据表达式的目标使用，影响表达式的类型化
      //    能够自动推断类型时，可以去掉参数两边的括号
      list.filter(x => x > 2)

      // 参数占位符，省略参数定义部分
      list.filter(_ > 2)
      //  没有足够信息推断时，可以指定
      val f = (_: Int) + (_: Int)
    }

    // 偏应用函数：函数未被应用所有的参数，只提供了部分参数
    if (true) {
      // 1) 偏应用函数，_作为整个参数列表的占位符
      list.foreach(print _)   ;println()  //函数和_之间，需要一个空格，防止编译器认为是定义的一个新函数名
      list.foreach(print)     ;println()  //如果代码正需要传入一个函数，可以简化，去掉_

      // 2) 偏应用函数，可以用辅助来转化def函数为函数值
      def handle(x: Int, y: Int) = x + 7 + y

      // 固定部分参数，生成新的函数值；
      //  不使用这种写法，转换def为函数值无法成功；一个是类，一个是实例
      //val c = handle
      //  a是编译器按照sum，自动产生的类的一个实例，该实例带有apply方法（因此可以用a.apply(xx,xx)）
      val a = handle _          //这里并不需要一个函数，所以不能省略_
      val b = handle(1, _: Int) //这类必须给出未应用的参数的类型，否则无法编译通过
      println("apply function: " + a(2, 3) + ", "+ b(5))

      def work(doit: (Int, Int) => Int): Int = { doit(1, 2) }
      println("transmit cury handle: " + work(a))       //这里传入的是函数值
      println("transmit def handle:  " + work(handle))  //这里传入的是函数，是一个类

      // 强制使用_的目的，还有是因为有时无法判断程序员意图
      println("list.tail:   " + list.tail)      //List(2, 3, 4), execute
      println("list.tail _: " + list.tail _)    //<function0>,   handle
    }

    // 闭包：通过“捕获”自由变量的绑定，对函数文本执行的“关闭”行动；把外部变量封闭包含进来，保留其合适的值？
    if (true) {
      val t1 = (x: Int) => x + 1      //这不是闭包，在编写的时候就已经封闭了

      //  1）函数值是关闭闭包的最终产物，得到的函数值将包含一个指向捕获的变量的参考，因此成为闭包
      var more = 1
      val t2 = (x: Int) => {
        more += 1
        x + 1 + more
      }
      println("closure: " + t2(5) + ", more " + more)   //闭包中的引用的是自由变量的引用, 可以改变自由变量的值

      //  2) 定义了一个函数，函数体是返回一个闭包；
      //    这里有多个more，捕获的参数自动存在于堆中，因此可以表现出不同的绑定
      def makeInc(more: Int) = (x: Int) => x + more
      val a = makeInc(10)   // a = (x: Int) => x + 10, 是一个偏应用函数
      val b = makeInc(100)  // b = (x: Int) => x + 10
      println("closure: bind diff more, " + a(100) + ", " + b(100))

      //  3）使用闭包，简化函数参数，将一个参数和另一个参数绑定起来
      //  原始函数; 没有闭包，就要写成 findMatch(matcher: String => Boolean, query: String)
      def findMatch(matcher: String => Boolean) = {
        val file = "a.last"
        if (matcher(file)) println("closure: " + "find match")
        else println("closure: " + "nothing")
      }
      //  绑定函数，封装了一些信息进去
      def findEnding(query: String) = {
        // 传入了 (name: String, query: String) => name.endsWith(query)
        //    这里绑定了变量query，相当于传入了一个函数  (name: String) => name.endsWith(query); query是绑定的自由变量
        findMatch(_.endsWith(query))    // 生成了新函数 name: String => Boolean
      }
      findEnding("a.last")

    }
  }

  def syntax_abstract(): Unit = {
    println("\nsyntax_abstract:")

    // 高阶函数
    if (true) {
      val a = List(1, 2, 3, 4, 5)
      println("list exist(_ > 4): " + a.exists(_ > 4))
    }

    // Curry，有点类似偏应用函数？
    if (true) {
      def sum1(x: Int, y: Int) = x + y
      def sum2(x: Int)(y: Int) = x + y
      // a的类型是一个函数 (Int) => Int
      val a = sum2(2)_    // 第二个参数使用占位符
      println("curry handle: " + a(3))
    }

    // 只有一个参数时，可以用{} 代替(), 看起来更像內建控制结构
    if (true) {
      println("absctract, use println(): " + "hello world!")
      println{"absctract, use println{}: " + "hello world!"}

      // 为了让有多个参数的函数，也能看起来像控制结构，可以使用cury，对最后一个参数使用 {}
      def handle1(a: Int, work: Int => Int) = { work(a) }
      def handle2(a: Int)(work: Int => Int) = { work(a) }
      // 调用时，可以使用；第二个参数用的是大括号
      handle2(5) { x => x + 10 }
    }

    // 叫名参数，看上去像使用了內建控制结构
    val assert = false
    def myAssert0(predicate: Boolean) = {
      if (assert && !predicate) { // 这里 predicate必须有括号
        println(assert)
      }
    }
    def myAssert1(predicate: () => Boolean) = {
      if (assert && !predicate()) { // 这里 predicate必须有括号
        println(assert)
      }
    }
    //    叫名函数，类型始于=> 而不是 () =>
    def myAssert2(predicate: => Boolean) = {
      if (assert && !predicate) {   // 这里 predicate没有括号
        println(assert)
      }
    }
    myAssert0(5 > 3)        // 函数参数（这里是表达式 5 > 3）先于函数调用被评估
    myAssert1(() => 5 > 3)  // 原始方式，必须传入
    myAssert2(5 > 3)        // 延迟评估，创建一个函数值，传递给myAssert2；函数值的apply方法将评估 5 > 3
  }

  def syntax_parameter(): Unit = {
    println("\nsyntax_parameter:")

    def echo(args: String*): Unit = {
      print(" echo: ")
      for (arg <- args) print(arg)
      println("")
    }
    print("multi param"); echo("1", "2", "3")
    print("array param"); echo(Array("1", "2", "3"): _*)    // : _* 相当于将数组的每个元素当做参数传入
  }

  def syntax_package(): Unit = {
    import java.util.{Formatter => _}          // 排除 Formatter
  }


  def main(args: Array[String]): Unit = {
    syntax_operator()

    syntax_function()

    syntax_abstract()

    syntax_parameter()

    syntax_package()
  }
}
