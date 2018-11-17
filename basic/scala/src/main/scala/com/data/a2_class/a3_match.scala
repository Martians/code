package com.data.a2_class

/**
  * 常用于无封装数据结构，对树形递归数据特别有用
  *     1. 样本类：
  *             能够快速定义类，自动生成一些代码，用起来很方便
  *             常用于模式匹配，可以进行分解、拆分
  *
  *     2. 模式匹配
  *             相当于抽丝剥茧，将已经‘打包’的内容，重新分解开来；case class、列表、元组、类型
  *             不同case 表达式可以返回不同类型的值，不需要完全一致
  *             变量模式和_，都可以当做default分支来使用
  *             匹配模板时，实际上已经进行了类型擦除，模板中的T部分用_代替
  *             经常用于对 Option 进行模式分离
  *
  *             根据参数的不同，返回多个不同的函数（限制：参数数any，返回值必须相同？）；即：函数可以有多个入口点？
  */
object a3_match extends App {

    /**
      * 定义一系列样例类：
      *     1. 类的空花括号可以省略
      *     2. 封闭类 sealed：表明除了该文件，不能在其他地方再定义类的任何新子类； (183)
      *             以免模式匹配时有遗漏；可以使用 @unchecked 注解 取消不完全匹配时的警告
      */
    sealed abstract class Expr
    case class Var(name: String) extends Expr
    case class UnOp(Operator: String, right: Expr) extends Expr
    case class BinOp(Operator: String, left: Expr, right: Expr) extends Expr

    def operate_caseclass() = {
        /**
          * 样例类自动生成代码：
          *     1）类的工厂方法；可以省略new关键字；可以方便嵌入表达式中（如：BinOp(Number(1, 2), Var(new))）
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
              *     2. val (P175：这里用了X进行匹配）；val的命名必须是大写字母（小写字母就是变量匹配了），或者是某个对象的字段
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
              *     1. 常用于类型的操作，替换掉：1）判断：s.isInstanceOf[String], 2）转换：s.asInstanceOf[String]
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
          * 定义val、var变量时，直接进行模式匹配
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

    operate_caseclass
    operate_match
    operate_normal
    operate_option
    operate_function
}
