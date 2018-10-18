package com.data.a2_class;

import com.data.a0_util.format.CollectionF;


import static com.data.a0_util.format.Display.out;

public class A5_Runtime {
    static int value = 100;

    /**
     * Class 类不能显式声明
     *      通过 this.getclass(), ClassName.class获得
     */
    static void classType() {

        try {
            /**
             * 通过类的字符串名字，获得其Class类型
             */
            Class type1 = Class.forName("com.data.a2_class.A5_Runtime");
            out(type1.getName());

            /**
             * 必须用 ?，java中的模板实际上是没有类型信息的
             *
             * type1只是一个类型，这里的接收变量类型，必须是数组？
             *
             * 数组、泛型中，都没有保存类型？
             */
            Class<?>[] list = type1.getClasses();
            out(list.length);
            for (Class<?> c : list) {
                out("next " + c);
            }

            Object c = type1.newInstance();
            out("new object: " + c);

        } catch (Exception e) {
            out(e);
        }
    }


    public static void main(String args[]) {
        if (false) {
        }
        classType();
    }
}
