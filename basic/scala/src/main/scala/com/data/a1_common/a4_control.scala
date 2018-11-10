package com.data.a1_common

import java.io.FileNotFoundException

object a4_control extends App {
    /**
      * 几乎所有的控制结构，都会产生某个值
      * 引入变量，等效于计算他的表达式，因此可以省略这个变量的定义 P74
      *
      */
    def operate_normal()= {

        val c1 = if (1 > 0) 5 else 4

        /**
          * while、do-while是循环
          *     1. 不是表达式；他们自身不能产生有意义的结果；他们通常和var结对出现
          *     2. 通常为了去掉他们和var，需要使用递归，这样更易于理解 (P84)
          *        实际上，如果是尾递归，那么编译器会生成类似于while的代码出来
          *  代码例子：P87 打印乘法表
          */
    }

    /**
      * 没有break（增加一个标志代替）、continue（用if括起来代替）；P84
      */
    def operate_loop(): Unit = {
        println("\noperate_loop:")


        var line = ""
        /**
          * 这里的输出，是unit，而不是；因此 while ((line = readLine())) 不会起作用
          */
        println("(line = \"abc\") value always: " + (line = "abc")) //返回值是 ()

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

        /**
          * for乘坐生成器语法
          *     1. to：包括end， until：不包括end
          *     2. 多个过滤器；
          *             如果使用for(), 多个过滤器之间必须有; 使用 for{} 大括号时不需要 (P78)
          *     3. yield：
          *         生成的结果Array
          */
        val loop =
            for { i <- 1 to 10 if i % 2 == 0
                  j <- 1 to 5
                  if j > 3
                  if i > 5; if i > 2
            } yield {(i, j)}
        println("generate (i, j): " + loop)


        /**
          * for的结果直接输出， P78
          *     k 滤间变量绑定，不需要加 val、var
          */
        for { i <- 1 to 2
              k = i + 1
              j <- 1 to 2
        } print("i-" + i + ",k-" + k + " |")
    }

    /**
      * 异常处理：
      *     throw不会产生任何职，返回Nothing类型，但仍然可以将其当做表达式
      *     不需要捕获检查异常
      */
    def operate_exception(): Unit = {
        println("\n\noperate_exception:")

        val n = 4
        val half = if (n % 2 == 0) n /2
                   else throw new RuntimeException("111")

        /**
          * try-cache 也可以产生值
          *     这里有个错误，因为有了finally，因此返回的是finally中的结果 Uint
          *
          */
        val url = try {
            } catch {
                case ex: FileNotFoundException => {
                    println("not find")
                    "not file"
                }
            } finally {
                println("do finally")
            }
        println(url)
    }

    /**
      * 可以匹配任何常量
      *     结尾不需要break
      */
    def operate_matching()= {
        println("\n\noperate_matching:")

        val data = "long"

        /**
          * 这里不同的分支，返回的是不同的值类型！
          */
        val result = data match {
            case "long" => println("match long"); 5
            case "short" => println("match short"); "bb"
            case _ => println("default")
        }
        println(result)
    }

    operate_normal
    operate_loop
    operate_generator
    operate_exception
    operate_matching
}
