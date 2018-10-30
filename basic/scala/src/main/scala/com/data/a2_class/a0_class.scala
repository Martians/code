package com.data.a2_class


/**
  * 一个Scala源文件中可以有多个类
  *
  * 类定义可以有参数，称为类参数，相当于定义了类成员
  *
  * override val xc 为重写了父类的字段。
  */
// 虽然这里很多类都写在一起，但是编译后会生成多个文件
object jClass {

  //http://hongjiang.info/scala-type-and-class/
  //http://blog.csdn.net/a2011480169/article/details/52960101
  //http://blog.csdn.net/sinat_25306771/article/details/52004355
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

  def class_simple() = {
    println("\nclass_simple:")

    class CheckSum {
      // 默认public属性
      val path = "/home/long"
      // private 需要显示给出
      private var sum = 0
      // 仅子类可以访问，同一包内的的其他类也无法访问
      protected var access: Int = 0

      // 函数的定义，必须有一个 =
      //    没有=的函数定义，返回值必然是Unit；这样就类似于“过程”的定义，为了副作用而执行的方法
      def add(b: Byte) {
        sum += b
      }

      def checksum(): Int = ~(sum & 0xFF) + 1
    }

    // 单例对象，此处也是CheckSum的伴生对象，可以访问其私有成员
    //    每个单例对象，都由一个静态变量指向的虚构来的一个实例来实现，名称为：CheckSum$
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

    // 定义为不可变结构
    //    定义主构造器，这是类的唯一入口点
    class Rational(n: Int, d: Int) {
      //  先决条件
      require(d != 0)

      private val g = gcd(n.abs, d.abs)
      //  主构造器定义的成员变量，仅能内部访问；不能从外部引用访问(that: Rational, that.d、that.n 访问失败)
      //      可以使用参数化字段方式来实现
      val numer: Int = n / g
      val denom: Int = d / g

      //  定义从构造器
      def this(n: Int) = this(n, 1)

      //  ，所有期间语句，都将是主构造器的执行代码，包括定义成员变量等
      //println("rational: " + n + "/" + d)

      ////////////////////////////////////////////////////////////////
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
      //  不重载的话，输出的是 Rational@a0b0f5 这类名字
      override def toString = n + "/" + d
      def gcd(a: Int, b: Int): Int = {
        if (b == 0) a else gcd(b, a % b)
      }
    }

    val s1 = new Rational(1, 5)
    val s2 = new Rational(2, 8)
    println("rational s1 + s2: " + (s1 + s2))

    {
      // 定义隐式转换，只能在当前作用域范围之内，否则无法起作用
      //   这个将被编译器隐式应用，对程序来说并非显而易见
      implicit def intToRational(x: Int) = new Rational(x)
      println("rational s1 + 3: " + (s1 + 3))
      println("rational 3 + s1: " + (3 + s1))
    }
  }

  // 孤立对象
  object Single {
  }

  // 相当于main，如何执行？？？？
  object SingleApp extends App {
  }

  def class_hierarchy() {

    abstract class Element {
      def contents : Array[String]
      // 空括号方法, 在不改变内部状态时，尽量使用此方式
      def height: Int = contents.length
      def width:  Int = if (contents.length == 0) 0 else contents(0).length
    }

    // 较繁琐的定义方式
    if (false) {
      class ArrayElement(conts: Array[String]) extends Element {
        //  字段和方法在相同的命名空间：名字不能重复；字段、无参数的方法，可以互相重载
        val contents: Array[String] = conts   // 父类中是函数，这里是字段
        //  这里是实现抽象成员，override可选
      }
    }
    // 较简化的定义方式
    class ArrayElement(
        val contents: Array[String],   // 参数化字段，可在同一时间，同时定义函数参数、字段（类成员）
        override val height: Int) extends Element {}

    val a = new ArrayElement(Array("1", "2"), 1)
    println(a.contents.toString)
  }

  def class_trait(): Unit = {

    import scala.collection.mutable.ArrayBuffer
    abstract class IntQueue {
      def get(): Int
      def put(x: Int)
    }
    class BaseIntQueue extends IntQueue {
      private val buf = new ArrayBuffer[Int]
      def get(): Int = buf.remove(0)
      def put(x: Int) = { buf += x }
    }

    // 可堆叠特质，在父类的基础上进行变动
    //    这里的super是动态绑定的
    trait Increase extends IntQueue {
      abstract override def put(x: Int) {super.put(x + 1)}
    }
    trait Double extends IntQueue {
      abstract override def put(x: Int) {super.put(x * 2)}
    }
    // 右边的特质先起作用
    val queue = new BaseIntQueue with Increase with Double
    queue.put(2)
    println("trait effect: " + queue.get())
  }

  def main(args: Array[String]): Unit = {
      class_hierarchy()

      class_trait()

      if (false) {
      class_runtime()

      class_simple()

      class_normal()
    }
  }

}
