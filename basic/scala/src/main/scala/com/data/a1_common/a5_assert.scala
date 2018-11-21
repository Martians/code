package com.data.a1_common

object a1_assert extends App {

    /**
      * 常规场景：判断一个值的合法性后，返回给外部
      */
    def operate_assert() = {

        /**
          * 方式1：常规用法
          */
        val r1 = {
            val x = 1
            assert(x == 1)
            /**
              * 第二个参数是叫名参数，延迟执行  message: => Any
              *     可以是任意类型，相当于print
              */
            assert(x == 1, () => "not good")
            assert(x == 1, "not good")
            x
        }

        /**
          * 方式2：对表达式的返回值，调用ensuring，执行一个判断
          *     1. ensuring 存在隐式转换
          *     2. ensuring 有一个论断函数，会将调用者当做参数传入这个函数进行判断
          */
        val r2 = if (0 > 1) {
            val x = 1
            x
        } ensuring(_ >= 1)

    }

    operate_assert
}
