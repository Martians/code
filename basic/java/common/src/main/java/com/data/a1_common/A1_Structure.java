package com.data.a1_common;

import static com.data.a0_util.format.Display.out;

public class A1_Structure {

    /**
     * 可以使用字符串，作为case的变量类型
     */
    static void caseType() {

        String str = "two";
        switch(str) {
            case "one": out("one");
                break;
            case "two": out("match");
                break;
            default:    out("nothing");
                break;
        }
    }

    /**
     * 可变参数类型，将参数当成某种类型的数组
     *
     * 此时，参数也可以传入数组
     */
    static void vaTest(int ... values) {
        for (Integer v : values) {
            out(v.toString());
        }
    }

    public static void main(String args[]) {
        if (true) {

            caseType();
            vaTest(5, 4, 3, 2, 1);

            int[] arrays = {1, 2, 3, 4, 3};
            vaTest(arrays);
        }


    }
}
