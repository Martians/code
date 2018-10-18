package com.data.a1_common;

import static com.data.a0_util.format.Display.out;

public class A4_Enum {

    /**
     * 默认为公有、final的成员，他们是自类型化的
     *
     * 他们的类型，都是所定义的enum类型
     */
    enum Apple {
        Jonathan, GoldenDel, RedDel
    }

    static void appleEnum() {
        Apple ap = Apple.Jonathan;

        /**
         * 遍历enum
         */
        for (Apple a : Apple.values()) {
            out("" + a);
        }

        /**
         * 根据字符串，获取enum
         */
        ap = Apple.valueOf("GoldenDel");
        out("current " + ap + ", ordinal " + ap.ordinal());
    }

    /**
     * 每个enum，可以当做是 新定义enum类型的一个实例，final、public成员
     */
    enum Apple1 {
        Jonathan(8), GoldenDel(9), RedDel(10);

        private int price;
        int getPrice() { return price; }
        Apple1(int p) { price = p; }
    }

    public static void main(String args[]) {
        appleEnum();
    }
}
