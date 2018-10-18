package com.data.a1_common;

import java.io.Serializable;

import static com.data.a0_util.format.Display.*;


public class A1_DataType implements Serializable {

    static void arrayType() {
        int array[] = {1, 2, 3, 4, 5};
    }

    /**
     * java 实现的很多标准数据结构，是针对对象进行操作的
     *
     * 需要将基本类型进行封装，自动解包、封包
     */
    static void wrapper() {
        Integer ab = new Integer(100);
        int i = ab.intValue();
    }

    /**
     * 自动装箱、拆箱
     * */
    static int returnValue() {

        Integer iob = 100;
        ++iob;

        iob = iob + 100;
        out(iob.toString());
        return iob;
    }

    /**
     * java中是会自动调用toString，将类型转换为字符串的
     */
    static void typeInfo() {
        out(Integer.BYTES + ", " + Integer.MAX_VALUE + "," + Integer.SIZE);
    }

    public static void main(String args[]) {
        if (false) {
            arrayType();

            returnValue();
        }

        typeInfo();

    }
}
