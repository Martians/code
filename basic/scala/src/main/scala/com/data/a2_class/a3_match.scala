package com.data.a2_class

import scala.util.matching.Regex

/**
  * 常用于无封装数据结构，对树形递归数据特别有用
  *     1. 样本类：
  *             能够快速定义类，自动生成一些代码，用起来很方便
  *             常用于模式匹配，可以进行分解、拆分
  *
  *     2. 模式匹配
  *             相当于抽丝剥茧，将已经‘打包’的内容，重新分解开来；case class、列表、元组、类型、字符串（需要定义抽取器）
  *             不同case 表达式可以返回不同类型的值，不需要完全一致
  *             变量模式和_，都可以当做default分支来使用
  *             匹配泛型时，泛型类实际上已经进行了类型擦除，模板中的T部分用_代替（Array除外）
  *             经常用于对 Option 进行模式分离；Some、None，他们本身就是样本类
  *
  *             根据参数的不同，返回多个不同的函数（限制：参数是Any，返回值必须相同？）；即：函数可以有多个入口点？
  *
  *
  *     3. 抽取器（自定义的模式分解方式）：
  *             进一步进行数据的解构和分析
  *             如果定义好了，还可以解构字符串
  *             一定程度上解析正则表达式 P330
  */
object a3_match extends App {

    /**
      * 定义一系列样例类：
      *     1. 类的空花括号可以省略；样例类相当于是某种程度的 Tuple
      *     2. 封闭类 sealed：表明除了该文件中，不能在其他地方再定义类的任何新子类 p183
      *             以免模式匹配时有遗漏；可以使用 @unchecked 注解 取消不完全匹配时的警告
      */
    sealed abstract class Expr
    case class Var(name: String) extends Expr
    case class UnOp(Operator: String, right: Expr) extends Expr
    case class BinOp(Operator: String, left: Expr, right: Expr) extends Expr

    def operate_caseclass() = {
        /**
          * 样例类将自动生成一些代码：
          *     1）生成了类的工厂方法，因此可以省略new关键字；可以方便嵌入表达式中（如：BinOp(Number(1, 2), Var(new))）
          *     2）toString、equals等方法的自然实现
          *
          *     会为类自动添加一些语法、代码；使得类会变大一些（产生了一些附加方法，每个构造器参数都添加了隐含的字段）
          */
        val x = Var("aa")
    }

    /**
      * 模式匹配：
      *     1. 场景：相当于一次性定义多个函数；这些函数有不同的参数、返回值
      *     2. 始终以值作为结果，即表达式必然会返回一个值
      */
    def operate_match() = {
        println("\noperate_match:")

        val X = 1
        object Aclass {}

        def desc(x: Any) = x match {
            /**
              * 常量模式
              *     1. 字面量
              *     2. val (P175：这里用了X进行匹配）；val的命名必须是大写字母（小写字母就是变量匹配了，除非使用`x`），或者是某个对象的字段
              *     3. 单例object
              */
            case "abc" => println("get abc")
            case X => println("match define val " + x)
            case Aclass => println("match Aclass")

            /**
              * 构造器模式：
              *     1. 深度匹配，即可以检查任意深度
              */
            case BinOp("+", _, BinOp("-", _, e)) => println("match deep BinOp " + e)
            /**
              *     2. 创造变量绑定：绑定符合条件的部分，进行绑定：name @ pattern
              */
            case BinOp("+", _, t @ BinOp("*", _, e)) => println("match deep BinOp " + t)
            case BinOp(_, _, _) => println("match BinOp")

            /**
              * 类型匹配：
              *     1. 常用于类型的操作，替换掉：1）类型判断：s.isInstanceOf[String], 2）类型转换：s.asInstanceOf[String]
              *     2. 类型擦除：与java一样，在泛型中使用了类型擦除，因此匹配时不能带类型
              *             有一个例外是数组，数据的元素类型与数组值保存在一起
              */
            case s: String => println("match string, " + s); s.size
            case s: Map[_, _] => println("match map, " + s)
            case s: Array[Int] => println("match int array, " + s.toString)
            case s: Array[_]   => println("match any array, " + s.toString)

            /**
              * 变量模式：将变量绑定在匹配的对象上；注意：这里也相当于通配匹配了
              */
            case x => println("match variable " + x); 100
        }

        desc(X)
        desc(2)

        desc(BinOp("+", Var("1"), Var("2")))
        desc(BinOp("+", Var("1"), BinOp("-", Var("2"), Var("wait deep"))))
        desc(BinOp("+", Var("1"), BinOp("*", Var("2"), Var("variable"))))
        desc(Aclass)

        desc("it is a string")
        desc(Map(1->"a", 2->"b"))
        desc(Array(1, 2, 3))
        desc(Array("1", "2", "3"))

        /**
          * 样式重叠：
          *     递归方式使用模式匹配 P181
          */
    }

    /**
      * 匹配 List、Tuple
      * 模式守卫
      */
    def operate_normal() = {
        println("\noperate_normal:")

        def split(x: Any) = x match {
            /**
              * list：这里的 _*匹配剩余所有元素
              */
            case List(0, _*) => println("split list with head 0")
            case (a, b, c) => println("split tuple-", a, b, c)

            /**
              * 模式守卫：if 进行一次判断，进一步过滤
              */
            case (a, b: Int) if b > 2 => println("split with guard -", a, b)

            /**
              * 通配模式：因为之上有了变量匹配模式，因此不会运行到这里
              */
            case _ => println("split default, " + x); 5
        }
        split(0::1::Nil)
        split((1, 2, 3))
        split("sssss")
        split((1, 3))

        /**
          * 定义val、var变量时，直接进行模式匹配，不需要在match中
          *     1. 元组
          *     2. 样本类
          */
        var (a, b) = (1, 2)

        val BinOp(x, y, z) = BinOp("+", Var("1"), Var("2"))
        println("get binop, ", x, y, z)

        /**
          * for 中匹配
          */
        val list = List((1, 2), (3, 4))
        for ((x, y) <- list) {
            println("for list: (%d, %s)".format(x, y))
        }
    }

    /**
      * Option:
      *     1. Some、None两种类型
      *     2. 常见场景：判断操作的返回值（如：map.get返回null，此时无法知道是不存在，还是值本身就是null）
      */
    def operate_option() = {
        println("\noperate_option:")

        def show(x: Option[String]) = x match {
            case Some(s) => println("get " + s)
            case None => println("get none")
        }
        show(Option("1111"))
        show(None)
    }

    /**
      * 偏函数样本:
      *     样本序列就是函数字面量，并且更普遍，可以有多个函数入口点（相当于多个 if else?）
      */
    def operate_function() = {
        val work: Option[Int] => Int = {
            case Some(x) => x
            case None => 0
        }
        work(Option(1))
        work(None)
    }

    /**
      * 抽取器：进一步推广了模式匹配
      *     1. 表征独立：不像样本类，抽取器截短了数据表达与模式的联系，P327
      *        代表了数据表现与客户观察之间的简洁适配层，可以通过修改抽取器来修改类型的具体实现
      *
      *     2. 与抽取器相比，样本类用于封闭的应用，更简洁、速度更快
      *
      */
    def operate_extractor() = {
        println("\noperate_extractor:")

        type abc = (String, String) => String

        /**
          * 额外使用：P322
          *     Email 继承自scala的函数类型，即 Function2[String, String, String]，并且实现了apply
          *     可以传递给需要 Function2[String, String, String] 的位置
          */
        object Email extends abc {
            /**
              * 注入方法，可选的实现方法，与抽取器无关
              */
            def apply(user: String, domain: String) = user + "@" + domain

            /**
              * 抽取方法
              *     逆转了apply的构建进程
              */
            def unapply(str: String): Option[(String, String)] = {
                val parts = str split "@"
                if (parts.length == 2) Some(parts(0), parts(1)) else None
            }
        }

        /**
          * 这里，是调用的apply
          */
        val c = Email("1", "2")
        println("apply: " + c)

        "sss@bb" match {
            /**
              * 这里根据 object email的 unapply 方法，进行了抽取
              *     Email并不是一个class，调用 Email.unapply
              *     实际上，只是为了抽取的话，不需要定义apply方法，只需要unapply
              */
            case Email(user, domain) => println("extractor: " + user + " @ " + domain)
        }

        /**
          * 例子：
          *     1. 进一步，绑定 user部分是大写，并且是重复出现的字符串的 user，例子：P324
          *     2. 变参抽取器，可变参数 unapplySeq
          *        实际上，List、Array等，就是用这种方式抽取的
          */
    }

    def operate_regex() = {
        println("\noperate_regex:")

        /**
          * 1. 使用 """，可以减少对 \\ 的使用
          * 2. RichString中包含了r的方法，可以直接转换为正则表达式
          */
        val partten = """(-)?(\d+)(\.\d*)?"""
        val Decimal1 = new Regex(partten)
        val Decimal = partten.r

        val str = "for -1.0 to 99 by 3"
        for (a <- Decimal findAllIn str) {
            println(a)
        }

        /**
          * 正则表达式的抽取
          *     对已经事先 分组 的正则，可以直接抽取出来
          */
        val Decimal(a, b, c) = "-100.200"
        println(a, b, c)
    }

    operate_caseclass
    operate_match
    operate_normal
    operate_option
    operate_function
    operate_extractor
    operate_regex
}
