package com.data.a3_collection

/**
  * list是immutable类型，对应于mutable的array、ListBuffer
  *
  *     1. list被定义为只能像stack一样使用
  *         head：第一个元素, tail: 除第一个元素之外的list， head + tail = list
  *         init：除最后一个元素之外的list，last：最后一个元素；计算他们需要遍历整个列表
  *
  *     2. Nil是空列表,  List() == Nil 为真
  *
  *     3. list是协变的：S是T的子类型，那么 List[S] 也是 List[T] 的子类型； P194
  *         List[Nothing] 是任何 list[T] 的子类型，因此 val xs: List[String] = list()
  *         Nil 就是extend List[Nothing]
  *
  *         这里的list就是叠加而成的，计算length很慢
  *
  *     4. 访问模式：
  *         适合头部访问
  *         如果要进行尾部访问：先进行翻转，需要时再翻转回来
  *
  *  实现：
  *     1. List是抽象类，new List不合法；
  *     2. ::、object 是 List的子类
  *         case object Nil extends List[Nothing]
  *         case class ::[T](head: T, tail: List[T]) extends List[T] P303
  *     3. List内部实现时，部分地方实际采用了 ListBuffer，更加高效
  *
  */
object a2_list {

    def operate_list() {
        println("\noperate_list:")

        val a = List(1, 2, 3)
        val b = List(4, 5, 6)

        /**
          * 初始化：结尾必须有个Nil; 只有Nil有::方法
          */
        val c = 1::2::3::Nil
        val h = List.range(1, 5)

        // 1) con item
        /**
          * :: 是右操作数的方法
          *
          * immutable类型也可以追加，是因为将生成一个新的list
          */
        val c1 = 5 :: a
        val c2 = a.::(5)
        println("con item: " + c1 + ", " + c2)

        val d = a ::: b
        println("con list: " + d)
        println("mkString: " + d.mkString(" + "))
        println("reverse:  " + a.reverse)

        /**
          * 常用函数
          *     1. take、drop
          *     2. splitAt: (xs take n, xs drop n)
          *     3. apply: (xs drop n).head，随机访问 list 效率很低
          */
        val abcde = "abcde"
        println(abcde take 2)
        println(abcde drop 2)
        println(abcde splitAt 2)
        println(abcde(2))

        /**
          * 转换
          */
        println(a.toArray)

        /**
          * 高阶函数
          *     1. 映射：map、flatmap
          *     2. 过滤：find、partition（类似于filter，但是将filter掉的也收集起来到新的list）
          *             takeWhile、dropWhile、span 找到符合条件的最常前缀
          *     3. 论断：forall、exists
          *     4. 折叠：/: :\ 格式是：(z /: list) (op)；也可以用 foldLeft、foldRight
          *             可以用来实现 reverse
          *     5. sort
          */
        val r = ("1" /: List("a", "b", "c"))(_ + _)
        println("zip: " + r)

        /**
          * 伴生对象方法：
          *     1. apply
          *     2. range
          *     3. flatten（将包含了list的list打散）
          */
    }

    def operate_match() = {
        println("\noperate_advance:")

        val l3 = List(1, 2, 3)
        val l4 = List(1, 2, 3, 4)

        /**
          * 列表的模式匹配,
          *     1. P196（还不太懂）
          *         1） 形式1：个数必须完全对应
          *         2）形式2：最后一个包含了剩下的元素
          *
          */
        val List(a, b, c) = l3

        val a1::b1::c1 = l4
        val a2::b2::c2::d2 = l3

        println("last " + c1)
        println("last " + d2)
    }

    /**
      * 实现list的各个函数
      */
    def operate_advance() = {
        /**
          * 排序：
          *     模式匹配：是拆分列表的常用方法，拆分出 head、tail
          */
        def isort(xs: List[Int]): List[Int] = xs match {
            case List() => List()
            case x::xs1 => insert(x, isort(xs1))
        }
        def insert(x: Int, xs: List[Int]): List[Int] = xs match {
            case List() => x::Nil
            case y::ys => if (x <= y) x::xs else y::insert(x, ys)
        }

        val list = isort(List(5, 4, 3, 2, 1, 7))
        println("sort: " + list)

        /**
          * 实现 :::
          *     列表是从后向前构造的，因此ys保持完整而拆分xs
          */
        def append(xs: List[Int], ys: List[Int]): List[Int] = xs match {
            case List() => ys
            case x::xs => x::append(xs, ys)
        }

        /**
          * 归并排序：
          *     1. 用了 currying
          */
        def msort[T](less: (T, T) => Boolean)
                    (xs: List[T]): List[T] =
        {
            def merge(xs: List[T], ys: List[T]): List[T] = (xs, ys) match {
                case (Nil, _) => ys
                case (_, Nil) => xs
                case (x :: xs1, y :: ys1) => {
                    if (less(x, y)) x :: merge(xs1, ys)
                    else y::merge(xs, ys1)
                }
            }
            val n = xs.length / 2
            if (n == 0) xs
            else {
                val (ys, zs) = xs.splitAt(n)
                merge(msort[T](less)(ys), msort[T](less)(zs))
            }
        }
        println("msort: " + msort[Int](_ < _)(List(1, 3, 7, 8, 2, 10)))
    }

    /**
      * ListBuffer: P217
      *     1. 是一个可变对象，+= 可以直接向尾部添加对象（LIst是提供对头部的快速访问）
      *     2. 代码中的递归不是尾递归时，为了避免栈溢出，用 for + ListBuffer 替代 List 递归
      */
    def operate_listbuffer() = {

    }

    def main(args: Array[String]): Unit = {
        operate_list
        operate_match
        operate_advance

        operate_listbuffer
    }
}
