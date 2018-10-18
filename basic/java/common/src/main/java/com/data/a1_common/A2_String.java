package com.data.a1_common;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static com.data.a0_util.format.Display.*;

/**
 * String是不可变的
 */
public class A2_String {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void stringCommon() {
        /** 每个字符串字面值会自动创建String对象 */
        int a = "abc".length();

        /** 转换时，是默认调用数String.valueOf进行转换的
         *
         *  valueOf 再自动调用相应类型的 tostring方法
         */
        out("1" + 5 + 5);
        out(String.valueOf(100));
    }

    static void StringInner() {
        /**
         * java 的 string，使用的是 utf-8 编码
         */
        String str = "abcde";

        /**
         * 这里显示的内容，都不太正常
         *      因为char是16位的，byte是8位
         */
        byte[] bytes = str.getBytes();
        out("bytes: " + bytes + ", tostring: " + Arrays.toString(bytes));

        char chars[] = new char[str.length()];
        str.getChars(0, str.length(), chars, 0);
        out("chars: " + chars + ", tostring: " + Arrays.toString(chars));

        /**
         * 返回字符数组，显示正常
         *      1. 直接显示 str.toCharArray(), 输出到是其类型，而不是内容
         *      2. 需要使用 Arrays.toString
         */
        out("char array: " + Arrays.toString(str.toCharArray()));

        /**
         * 演示：Arrays.deepToString
         */
        String[][] arrayStr = new String[][]{{"JAVA","Node","Python"},{"C","C++","Objective-C"}};
        System.out.println(Arrays.deepToString(arrayStr));
    }

    static void stringEqual() {
        String s1 = "Hello";
        String s2 = new String("Hello");

        /**
         * equals 比较的是字符串的内容， == 比较的是对象指针
         *
         * true|false
         */
        out("" + s1.equals(s2) + "|" + (s1 == s2));
    }

    /**
     * 快速构造字符串
     */
    static void stringJoin() {
        String result = String.join(", ", "11", "22", "33");
        out(result);
    }

    static void stringFormat() {
        out(String.format("format: %d, %s", 100, "a string"));

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void stringBuffer() {
        StringBuffer sbf = new StringBuffer();
        sbf.append(1).append("aaa");
        out(sbf);

        out(sbf.reverse());
        out(sbf.insert(3, "tmp"));
    }

    /** 速度更快，非线程安全 */
    static void stringBuilder() {
        StringBuilder sbf = new StringBuilder();
        sbf.append(1).append("aaa");
        out(sbf);

        out(sbf.reverse());
        out(sbf.insert(3, "tmp"));
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 类型转换
     */
    static void stringConvert() {
        /**
         * 字符串转换为整形
         */
        int a = Integer.decode("11111");
        int d = Integer.valueOf("11111");

        int b = Integer.parseInt("11111");
        int c = Integer.parseInt("11111", 16);
        out(a + ", " + b + ", " + c + ", " + d);

        out(Integer.toBinaryString(555));
        out(Integer.toString(555, 2));
    }

    static void stringChar() throws UnsupportedEncodingException {
        String str = "Hello";
        byte[] sbyte = str.getBytes();
        out(sbyte + ", " + Arrays.toString(sbyte));

        sbyte = str.getBytes("utf-8");
        out(sbyte + ", " + Arrays.toString(sbyte));

        String news = new String(sbyte);
        out("new: " + news);

        byte[] abyte = {'h', 'e', 'l', 'l', 'o'};
        String a = new String(abyte);
        String b = new String(abyte, "utf-8");
        out("oirgin: " + abyte + ", a: " + a + ", b: " + b);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * String 继承了 CharSequence，可用于返回流
     */
    static void stringStream() {
        String str = "1234567";
        str.chars().forEach(System.out::println);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String args[]) throws UnsupportedEncodingException {
        if (false) {
            stringCommon();

            StringInner();

            stringEqual();

            stringJoin();

            stringFormat();

            stringBuffer();

            stringBuilder();

            stringConvert();

            stringChar();

            stringStream();
        }
        stringChar();
        StringInner();
    }
}
