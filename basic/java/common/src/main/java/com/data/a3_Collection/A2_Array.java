package com.data.a3_Collection;

import com.data.a0_util.format.CollectionF;
import static com.data.a0_util.format.Display.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * ArrayList 是动态数组
 *
 * 实现了 List 接口，并没有一个具体的类是 List 本身
 */
public class A2_Array {

    /**
     * Arrays.asList 返回的只是 list 接口；看源码，其返回的类型就是 ArrayList
     *      return new ArrayList<>(a);
     */
    static List local = Arrays.asList(1, 2, 3, 4);
    /**
     * http://www.itstrike.cn/Question/e74b36fa-c01f-4254-87ec-e549df2abebe.html
     */
    static void initialize() {
        /**
         * 初始化方法1：传入一个collection进行初始化
         *
         * Arrays.asList 返回的只是 list 接口；看源码，其返回的类型就是 ArrayList
         */
        ArrayList<Integer> a1 = new ArrayList<>(local);
        a1.add(5);
        out(a1.getClass() + ": " + a1);

        /**
         * 初始化方法2：使用一个匿名内部类来
         */
        ArrayList<Integer> a2 = new ArrayList() {{
            add(1);
            add(2);
            add(3);
        }};
        out(a2);

        /**
         * 初始化方法3：使用集合算法
         */
        ArrayList<Integer> a3 = new ArrayList();
        Collections.addAll(a3,12, 13, 14, 15);
        out(a3);

        /**
         *  数组的初始化
         */
        int a[] = {1, 2, 3, 4};
    }

    /**
     * ArrayList 是动态数组
     */
    static void arrayList() {
        /**
         * object array，没有指定具体类型
         */
        ArrayList array = new ArrayList();

        array.add(1);
        array.add("2");
        array.add("working");
        out(array);

        /**
         * type array，指定了类型为string
         */
        ArrayList<String> a1 = new ArrayList<>();
        a1.add("a");
        a1.add("b");
        /** 流的方式遍历 */
        a1.forEach(System.out::print);
    }

    /**
     * 与原始数组的转换
     */
    static void arrayConvert() {
        ArrayList<Integer> array = new ArrayList<>(local);

        Object[] a1 = array.toArray();
        out(CollectionF.list(a1));

        /**
         * 要预先分配类型数组，因为集合并不知道类型
         */
        Integer a2[] = new Integer[array.size()];
        a2 = array.toArray(a2);
        out(CollectionF.list(a2));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 算法相关
     */
    static void search() {
        ArrayList<Integer> array = new ArrayList<>(local);
        int index = Collections.binarySearch(array, 3, (a, b) -> a.compareTo(b));
        out("index " + index);

        /**
         * 如果list已经是排序的，不需要传入 Comparator
         */
        //out(Collections.binarySearch(array, 9, (a, b) -> a.compareTo(b)));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String args[]) {
        initialize();

        arrayList();

        arrayConvert();

        search();
    }
}
