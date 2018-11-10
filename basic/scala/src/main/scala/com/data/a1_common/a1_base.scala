package com.data.a1_common

/**
  * 每个文件都隐含包含了 java.lang、Predef的成员引用（包含println、assert）
  *
  * 应用程序编写：
  *     1. 编写main方法
  *     2. object混入Application特质接口，但不可访问命令行参数了； P45
  *         通常只用于简单程序时使用
  */
object a1_base {

    /**
      * val：是指变量本身不可改变，但是其指向的对象，内部内容是可以修改的
      *      仅表示，不可再次赋值
      */
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



  def main(args: Array[String]): Unit = {

    operate_primary()
  }
}
