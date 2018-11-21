package com.data.a2_class

object a5_generic extends App {

    /**
      * 类型推断的局限性：P212
      *     1. scala的类型推断是局部的、基于流的
      *         如果之前流中已经有类型了，那么类型推断器将检查_代表的是否是已知类型
      *
      *     2. curry的函数，方法类型仅取决于第一段参数
      *
      *     list是协变的：S是T的子类型，那么 List[S] 也是 List[T] 的子类型； P194
      *
      * 类型参数化
      *     1. scala默认是非协变的，除非手动指定（Array不是协变的，list是协变的）
      */
    def operate_generic()= {

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

        val abcde = List('a', 'b', 'c', 'd', 'e')
        /**
          * 使用时的类型标注：
          *     1. 指明类型
          *     2. 类型标注
          *     3. 无法推断：msort(_ > _)(abcde)
          */
        msort((x: Char, y: Char) => x > y)(abcde)
        msort[Char](_ > _)(abcde)
    }

    /**
      * 变化型注解：
      *     1. 泛型类默认是非协变的; 假设结构体为Struct, U >: T
      *             +：协变（Struct[T]是Struct[U]的子类型）
      *             -：逆变（Struct[U]是Struct[T]的子类型），反过来了
      *
      *     2. 不可协变的例子
      *             1）数组不是协变的，因为数据可以改变其中的值 P256
      *             2）不允许使用+注解的类型参数，作为方法参数类型（即不能出现在形参中）P257
      *
      *     3. 里氏替换原则 LSP (P259)
      *             1）能在需要U的地方替换成T，那么T是U类型的假设就是安全的
      *             2）这里与U相应的操作比较，T要求的更少，但提供的更多
      *
      *             通常特质中同时需要逆变、协变；（例子：operate = A => B 是要传入的高阶函数定义，child = C => D 是实际传入的函数）
      *                 1）函数类型是逆变的（需要T，但是提供给其U，U对参数的要求更少；不需要是更具体的类型）即：要求子类型的地方，给的是父类型
      *                    参数C是A的父类：调用operate的位置传入的是A，而child只需要C，子类A当做父类C来使用，可以运行成功 P261
      *
      *                 2）其返回值是协变的（提供U，但是提供给其T，T提供的东西更多；子类型内容更丰富），即：父类型的地方，给的是子类型
      *                    返回值D是B的父类：需要operate返回值的地方只需要B，D是B的子类，子类D当做父类B来使用，可以运行成功 P261
      *
      *      4. 上下界定义
      *         下界：修饰的类，是当前类的父类，P258
      *         上界：修饰的类，是当前类的子类，T < : Ordered[T], 类型T必须是Ordered的子类型 P263
      */
    def operate_convariant() = {
    }

    operate_generic
    operate_convariant
}
