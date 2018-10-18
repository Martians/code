package com.data.a2_class;

import static com.data.a0_util.format.Display.*;

/**
 * 一个文件只能定义一个public class
 */
public class A1_Common {

    int value = 5;
    static int a = 3;
    static int b;

    /**
     * final 可用于定义内联函数
     */
    final int C_VALUE = 5;

    /**
     * 全局static语句，load class的时候会执行
     */
    static {
        out("static work here");
        b = a + 4;
    }

    //======================================================================================
    class Inner {
        void display() {
            /**
             * 嵌套类的的 instance，能访问外部类 instance 的成员变量
             */
            out("inner dispaly, outer value is " + value);
        }
    }

    void testInner() {
        /**
         * 这里创建的inner 实例，是在外部类实际已经存在的前提下执行的
         */
        Inner inner = new Inner();
        inner.display();
    }

    void testInner2() {
        class Inner2 {
        }
    }
    //======================================================================================

    public static void main(String args[]) {

        A1_Common a = new A1_Common();
        a.testInner();
    }
}
