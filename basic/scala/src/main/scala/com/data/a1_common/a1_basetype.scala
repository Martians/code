package com.data.a1_common

/**
  *
  */
object a1_basetype {

  def operate_primary() = {
    val a = 1
    val b = 2
    println(a.max(b))

    // 基本类型，定位为抽象、final的
    //val c = new Int

    // 所有类型的父类是Any: AnyVal、AnyRef(Object的别名)
    // 底层类型 Nothing、（引用类的子类）Null

    // RichInt

  }

  def operate_scope(): Unit = {
    // 在内部范围中，可以定义与内部范围相同的变量名字
    val a = 1;
    {
      val a = 2
      println(a)
    }
    println(a)
  }


  def operate_loop(): Unit = {
    println("\noperate_loop:")

    if (true) {
      // 这里的输出，是unit，而不是；因此 while ((line = readLine())) 不会起作用
      var line = ""
      println("(line = \"abc\") value always: " + (line = "abc")) //返回值是 ()
    }

    /**
      * 尽量不使用while，必然是产生副作用的地方
      */

    if (false) {
      // for: 添加过滤器
      //  1）if可以不带括号 2）带多个filter
      for (i <- 1 to 10
        if i % 2 > 0;
        if i > 5
      ) println(i)

      //  for 使用{}, 可以省略;
      for { i <- 1 to 10
           if i % 2 > 0
           if i > 5
      } println(i)

      //  for 嵌套
      //    x是变量绑定，记录中间信息，不需要带val x
      for { i <- 1 to 10
            if i % 2 > 0
            j <- 1 to i
            x = j + 10
            if j > 3
      } println(i + "-" + j + ", " + x)
    }
  }

  def operate_generator(): Unit = {
    println("\noperate_generator:")

    val x = 1 to 4
    val y = 1 until 4

    // 生成的是Array
    val loop =
      for { i <- 1 to 10
          j <- 1 to 2
    } yield {(i, j)}
    //loop.foreach()
    println("generate (i, j): " + loop)
  }

  // 不需要捕获检查异常
  def operate_exception(): Unit = {
    val n = 5
    val half = if (n % 2 == 0) n /2                   // 返回整形
              else throw new RuntimeException("111")  // 返回Nothing类型
  }

  def operate_match(): Unit = {

  }

  def main(args: Array[String]): Unit = {

    operate_primary()

    operate_loop()

    operate_generator()

    operate_exception()

    operate_match()

  }
}
