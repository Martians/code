package com.data.a2_class;

import static com.data.a0_util.format.Display.*;


public class A2_Derive {

    /**
     * 访问保护，见 197
     */
    static class Base {
        int value;

        Base(int a) {
            value = a;
        }
    }

    static class Child extends Base {
        int value;

        Child(int a, int b) {
            super(a);
            value = b;
            out("super " + super.value + ", Child: " + value);
        }
    }

    public static void main(String args[]) {
        Child a = new Child(1, 2);
    }
}
