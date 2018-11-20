package com.data.a2_class


/**
  * 小结：
  *     1. 访问权限
  *     2. 伴生对象
  *     3. 主构造器
  *     4. 隐式转换
  *     5. 参数化字段实现重载
  *     6. 无括号函数，以后可以重载为字段
  *
  * 设计
  *     1. 不可变对象的优势：P62
  *         优势：更容易理解；自由传递：线程安全、Hash表安全
  *         劣势：如果对象很大复制就很慢；而可变对象的更改在原址发生
  *
  * 实现
  *     1. 一个Scala源文件中可以有多个类；实际上编译出来是多个class文件
  *     2. 每个public的var成员，都隐含实现了 setter（x的 函数名为x_）、getter（x的函数名为x），P235
  *         实际上var成员是private的，setter、getter是public的
  *
  * 语法
  *     1. 类定义可以有参数，称为类参数，相当于定义了类成员
  *
  */
object a0_class extends App {

    def class_simple() = {
        println("\nclass_simple:")

        /**
          * 访问权限: P177
          *   1. 默认public属性
          *   2. private 需要显示给出
          *   3. protected：仅子类可以访问，同一包内的的其他类也无法访问
          *   4. 可以指定保护范围 protected[scope]
          */
        class CheckSum {

            val path = "/home/long"
            private var sum = 0
            protected var access: Int = 0

            /**
              * 这里将成员初始化为缺省值，根据其类型决定具体值
              *     如果不定义值的话，会认为该变量时抽象的，而不是未初始化
              */
            var work: String = _

            /**
              * 函数的定义，必须有一个 =
              *     没有=的函数定义，返回值必然是Unit
              *     这样就类似于“过程”的定义，为了副作用而执行的方法
              */
            def add(b: Byte) {
                sum += b
            }

            /**
              * 方法仅计算单个表达式（一条语句即可完成），那么可以去掉花括号
              */
            def checksum(): Int = ~(sum & 0xFF) + 1
        }

        /**
          * P 43
          * 伴生对象（companion object），相当于了singleton
          *     1）每个单例对象，都由一个静态变量指向的虚构类的一个实例，当前名称为：CheckSum$
          *        是CheckSum的伴生对象，可以访问其私有成员
          *        不能带参数构造，不能使用new
          *
          *     2）对没有任何伴生类的伴生对象，称为是独立对象；可以当做程序入口点、实现工具类等
          */
        object CheckSum {
            import scala.collection.mutable.Map
            private val cache = Map[String, Int]()

            def caculate(s: String) = {
                if (cache.contains(s)) {
                    cache(s)
                } else {
                    val acc = new CheckSum
                    for (i <- s) {
                        acc.add(i.toByte)
                    }
                    val cs = acc.checksum()
                    cache += (s -> cs)
                    cs
                }
            }
        }
    }

    def class_normal() = {
        println("\nclass_normal:")

        /**
          * 设计：定义为不可变结构
          *
          * 定义主构造器，这是类的唯一入口点
          *     1. 只有主构造器才能调用超类的构造器
          *     2. 辅助构造器都需要先调用主构造器
          *
          * 说明：
          *     1. 主构造器定义的成员变量，仅能内部访问; 这里笔记写的是错误的？
          *         不能从外部引用访问(that: Rational, that.d、that.n 访问失败)???? 似乎可以访问
          *     2. 可以使用参数化字段方式来实现
          *     3. 类定义中的代码，都将是主构造器的执行代码，包括定义成员变量等
          */
        class Rational(n: Int, d: Int) {
            /**
              * 先决条件，检查主构造器之后的状态
              */
            require(d != 0)

            private val g = gcd(n.abs, d.abs)

            val numer: Int = n / g
            val denom: Int = d / g

            /**
              * 辅助构造器
              */
            def this(n: Int) = this(n, 1)

            println("rational: " + n + "/" + d)

            /**
              * 操作符，实际上就只是定义方法
              */
            def +(that: Rational): Rational = {
                new Rational(numer * that.denom + that.numer * denom, denom * that.denom)
            }

            def -(that: Rational): Rational = {
                new Rational(numer * that.denom - that.numer * denom, denom * that.denom)
            }

            def *(that: Rational): Rational = {
                new Rational(numer * that.numer, denom * that.denom)
            }

            def /(that: Rational): Rational = {
                new Rational(numer * that.denom, denom * that.numer)
            }

            ////////////////////////////////////////////////////////////////
            /**
              * 不重载的话，输出的是 Rational@a0b0f5 这类名字
              */
            override def toString = n + "/" + d

            def gcd(a: Int, b: Int): Int = {
                if (b == 0) a else gcd(b, a % b)
            }
        }

        val s1 = new Rational(1, 5)
        val s2 = new Rational(2, 8)
        println("rational s1 + s2: " + (s1 + s2))

        {
            /**
              * 隐式转换
              *     1. 带 implicit 关键字，函数名没有要求
              *     2. 只能定义在作用域范围之内，否则无法起作用
              *     3. 这个将被编译器隐式应用，对程序来说并非显而易见；让代码变得难以阅读和理解
              */
            implicit def intToRational(x: Int) = new Rational(x)

            println("rational s1 + 3: " + (s1 + 3))
            println("rational 3 + s1: " + (3 + s1))
        }
    }

    /**
      * 1. 使用无参数方法，支持统一访问原则，client不受通过字段，还是方法来实现属性的影响
      *     1）仅定义不带参数、没有副作用的方法为无参方法
      *     2）另外，在调用无参数的任何函数式时，如果不是仅仅访问对象内容，而是进行了某些改变，推荐调用方法时，写上()
      *
      * 2. 重载非抽象成员，必须使用override，以免覆盖后自己不知道
      * 3. final确保不被重载
      */
    def class_inherit() {

        /**
          * 使用 abstract 关键字
          *     使用在类前边，方法中不需要使用这个关键字；只要是未实现的，就是抽象方法
          */
        abstract class Element {
            def contents: Array[String]

            /**
              * 无参数方法
              *     继承类中可以改成 val height = contents.length，那么client代码也不需要改变，P140
              *         1）java中有四个命令空间，scala中仅有两个（值、类型），因此方法、成员如果重名，会被检测出来
              */
            def height: Int = contents.length
            def width: Int = if (contents.length == 0) 0 else contents(0).length
        }

        /**
          * 重载方式
          *     1）字段和方法在相同的命名空间：名字不能重复；字段、无参数的方法，可以互相重载
          *     2）这里是实现抽象成员，override可选；
          *        如果在父类中不是抽象的，那么必须带 override；这时为了防止意外覆盖了基类的函数却不知道
          *
          *        父类中contents是函数，子类这里是字段
          */
        class ArrayElement1(conts: Array[String]) extends Element {
            val contents: Array[String] = conts //
        }

        /**
          * 参数化字段方式重载 P141
          *     1）这是同一时间，用相同的名称定义参数和字段（即类成员）的简写方式
          *     2）可以添加protected、override等前缀
          */
        class ArrayElement2(val name: String,
                            val contents: Array[String],
                            override val height: Int) extends Element {}
        val a = new ArrayElement2("1", Array("1", "2"), 1)
        println(a.contents.toString)
    }

    /**
      * 所有类的父类是 Any
      *     1. AnyVal：所有內建值的父类
      *         1）所有的內建值类都是 抽象、final的，不能 new Int
      *         2）值类型之间可以隐式转换：有 Int 到 RichInt 的隐式转换，需要时会自动转换
      *
      *     2. AnyRef：所有引用类的基类
      *         1）AnyRef是 java.lang.Object 的别名
      *         2）实际上所有类扩展了 ScalaObject 特质
      *
      *     3. Nothing：所有引用类的子类
      *         1）Null是null类型的引用
      *         2）Nothing用处是标明不正常的终止，如：throw...
      *            因此throw nothing类型，可以兼容函数返回值，放在函数中任何位置 P159
      */
    def class_hierarchy(): Unit = {

    }

    def class_state() = {
        /**
          * 每个public的var成员，都隐含实现了 setter（x的 函数名为x_）、getter（x的函数名为x），P235
          */
        class Value {
            var data: Int = _

            /**
              * 这里将 fdata、fdata_定义成了data的getter、setter
              */
            def fdata= data * 10

            /**
              * fdata_= 是函数名
              */
            def fdata_= (v: Int)= {
                require(v > 10)
                data = v / 10
            }
        }

        val t = new Value
        t.data = 100
        println("class state: ", t.data, t.fdata)

        t.fdata = 200
        println("class state: ", t.data, t.fdata)
    }

    /**
      * 1. 将类的主构造函数设置为 private，只能通过辅助构造函数创建，P251
      * 2. 只暴露特质，实现类混入特质，但设置为private；通过object类的一个工厂方法
      */
    def class_hiding() = {

    }

    /**
      * 类层级，超类的设计
      *     1.
      */
    def class_abstract() = {

        class Food
        trait RationTait {
            /**
              * 抽象类型成员，P273
              *     1. 必要性：如果不用type，基类中使用了这个固定参数类型的函数；那么子类要重载时就必须完全一致的参数，而不能使用这个参数的子类等
              *     2. 通过在子类中定义自己的type，可以有不同的函数原型（参数类型不同）
              *     3. 后续可以用 object.Food 的方式，引用这个类型（其中object是指向一个具体对象的变量），是一个路径依赖类型
              *
              */
            type t <: Food

            /**
              * 抽象val：
              *     1. 值不可改变，不能用def重写（def 可能返回不同的值）
              *     2. 子类对于val的实现，是在超类完成初始化之后才执行的；容易出现错误 P269
              *
              *         1）使用预初始化：在with之前，设置初始化字段的值 P270
              *         2）懒加载：在抽象类中定义为 lazy val a; 这样考虑定义的 文本顺序了
              */
            val a: Int

            var b: Int

            /**
              * 可以用val重写def
              */
            def c: String
        }

        /**
          * 例子2：P280，货币
          */
    }

    def class_inner() = {
        /**
          * 类型Inner
          *     1. 完整路径是 Outer#Inner，但不能用 new Outer#Inner 的方式初始化
          *     2. 实例化内部类的方式
          *         1）内部类持有外部类的引用，因此不能再没有外部实例的情况下，实例化内部类；必须在外部类的方法中，实例化内部类
          *         2）使用路径依赖类型
          *
          */
        class Outer {
            class Inner
        }
        val o1 = new Outer
        val o2 = new Outer

        /**
          * 路径依赖类型方式初始化，它们两个是不同的类型，但都继承于 Outer#Inner
          */
        val i1 = new o1.Inner
        val i2 = new o2.Inner
    }

    def class_enum() = {
        object Color extends Enumeration {
            val red, green, blue = Value
            val a = Value("a")
            val b = Value("b")
        }

        /**
          * 路径依赖类型
          *     Color.Value
          */
        val n = Color.blue
        val c: Color.Value = n

        println("enum: " + Color.a.id)
        println("enum: " + Color(1))

    }

    class_simple
    class_normal
    class_inherit
    class_hierarchy
    class_state
    class_hiding
    class_abstract
    class_inner
    class_enum
}
