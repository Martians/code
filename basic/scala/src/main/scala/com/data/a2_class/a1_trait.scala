package com.data.a2_class

/**
  * 扩展、混入特质（min in）
  *     1. 类似于接口，但是可以保存成员状态；但不能有主构造函数
  *     2. 解决多继承问题，可以设置默认实现（实际上 java8 中的接口可以有默认实现了）
  *     3. 特质是可堆叠的，而不是互相覆盖？几个重载了相同函数的特质，可以一起混入，串联起作用，但要注意顺序
  *     4. 特质可以继承自类、混入其他特质
  *
  * 语法
  *     只混入特质，使用extends；同时继承超类并混入特质，使用 extends ... with ...
  *
  * 设计
  *     1. 最终编译器会将特质编译成接口，接口运行稍慢于虚方法（如果发现是瓶颈可以改成超类）
  *     2. 要在多个类中使用的话，就定义为特质
  *
  * 常见特质：
  *     1. Ordered[type]，混入之后，就可以使用 > < 等操作符了
  *
  */
object a1_trait extends App {

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
        /**
          * 可堆叠的改变：在父类的基础上进行变动 P167
          *     1）特质可以继承抽象的超类; 同时在特质的默认实现中，可以引用超类中尚未实现的函数（必须加上 abstract override）
          *     2）意味着特质被混入到，某个具有期待方法的具体定义的类中
          *     3）super对trait来说是动态绑定的
          */
        trait Increase extends IntQueue {
            /**
              * 注意，super.put 在定义特质时尚未实现
              */
            abstract override def put(x: Int) {super.put(x + 1)}
        }
        trait Double extends IntQueue {
            abstract override def put(x: Int) {super.put(x * 2)}
        }

        /**
          * 灵活性：
          *     混入的顺序：右边的特质先起作用
          *     混入的多个不同的特质，都重载了相同的函数，他们将进行一连串连续操作
          */
        val queue = new BaseIntQueue with Increase with Double
        queue.put(2)
        println("trait effect: " + queue.get())

    }

    class_trait
}
