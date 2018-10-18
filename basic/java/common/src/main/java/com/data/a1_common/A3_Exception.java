package com.data.a1_common;

import static com.data.a0_util.format.Display.out;

public class A3_Exception {

    /**
     * 程序抛出没有处理的异常，必须对外指明这种行为，这是接口规范
     */
    static void throwOne() throws Exception {
        out("inside throwOne");
        throw new IllegalAccessException("demo");
    }

    public static void main(String args[]) {
        try {
            throwOne();

        } catch (IllegalAccessException | NullPointerException e) {
            out("Caught " + e);
            e.printStackTrace();
            out("end");

        } catch (Exception e) {
            out("Caught " + e);
            e.printStackTrace();
            out("end");
        /**
         * 无论如何都会处理，不管是否有异常。用于释放资源
         */
        } finally {
            out("finally");
        }
    }
}
