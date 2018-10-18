package com.data.a2_class;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.data.a0_util.format.Display.*;

/**
 * lamda 是一个函数式接口所定义的抽象方法的实现，这是lamda的目标类型
 *
 * lamda 必须在其目标类型的上下文中才能使用；它被赋值给了一个函数式接口的引用；相当于实现了这个函数式接口
 *
 * lamda 会导致生成一个匿名类，这个类实现了函数式接口，而lamda就是接口中方法的实现；相当于将代码片段转换为对象了
 */
public class A6_Lamda {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    interface MyNumber {
        int getValue(int v);
    }

    static void lamdaTest() {
        /**
         * 直接将lamda，赋值给一个函数式接口
         * lamda实现了getValue函数，实际上因为只有一个函数，因此其名称无所谓
         *
         * 参数的类型是从上下文推断出来的
         */
        MyNumber myNum1 = (v) -> 100 * v;
        MyNumber myNum2 = (int v) -> 100 * v;
        /** 只有一个参数时，可以省略括号 */
        MyNumber myNum3 = v -> 100 * v;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * lamda不能是模板（因为它已经是具体实现了），函数接口可以是模板
     */
    interface SomeFunc<T> {
        T func(T t);
    }

    static void lamdaTemplate() {
        SomeFunc<String> reverse = (str) -> {
            return str;
        };
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 变量访问
     */
    static int value = 20;
    static void variable() {
        /**
         * 被lamda访问的局部变量，自动变为final，不能对引用重新赋值
         *
         * 对于一个final变量，如果是基本数据类型的变量，则其数值一旦在初始化之后便不能更改；
         *
         * 如果是引用类型的变量，则在对其初始化之后便不能再让其指向另一个对象；但是引用对象的值是可以变的
         */
        int num = 100;
        int array[]= {10};

        MyNumber myNum1 = (v) -> {
            /** 可以改变非局部变量的值 */
            value = 1000;

            /** 小技巧，没有改变array引用本身，而是改变了其指向对象的内容 */
            array[0]= 10000;

            return num * v;
        };

        /** 因为在lamda中访问，num已经变成了final了 */
        //num = 1000;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 静态方法引用，将class的静态函数，作为lamda的值传入
     */
    static class MyNumberOP1 {
        static int getIt(int v) {
            return v;
        }
    }

    static int ValueOP1(MyNumber func, int v) {
        return func.getValue(v);
    }

    static void function1() {
        ValueOP1(MyNumberOP1::getIt, 5);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 实例方法引用，将class实例的函数，作为lamda的值传入
     */
    static class MyNumberOP2 {
        int getIt(int v) {
            return v;
        }
    }

    static int ValueOP2(MyNumber func, int v) {
        return func.getValue(v);
    }

    static void function2() {
        MyNumberOP2 a = new MyNumberOP2();
        /**
         * 实例方法也可以传入
         */
        ValueOP2(a::getIt, 5);
        //ValueOP2(MyNumberOP2::getIt, 5);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 同上
     *
     * 类的非静态方法引用，见 404P
     *
     * 相当于绑定了this指针，函数接口中是两个参数，类中的接口是一个参数
     */

    static void function3() {
        List<Integer> array1 = Arrays.asList(1, 2, 3, 4);
        /**
         * System.out 是一个全局实例
         *
         * println就不是静态函数
         */
        array1.forEach(System.out::println);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 构造函数的引用
     *
     * 更常见的例子，是工厂模式
     */
    static class MyClass<T> {
        private T val;

        MyClass(T v) {
            val = v;
        }
    }

    interface MyFunc<T> {
        MyClass func(T v);
    }

    static void construct() {
        MyFunc cons = MyClass::new;

        /**
         * 这样也行？
         *
         * 相当于new是一个模板的构造函数
         */
        MyClass mc1  = cons.func("100");
        MyClass mc2  = cons.func(100);

        /**
         * 这里的new是一个绑定了具体类型的构造函数
         */
        MyFunc<Integer> myCons = MyClass<Integer>::new;
        MyClass<Integer> mc = myCons.func(100);
    }

    public static void main(String args[]) {
        lamdaTest();

        lamdaTemplate();

        variable();

        function1();

        function2();

        function3();

        construct();
    }
}
