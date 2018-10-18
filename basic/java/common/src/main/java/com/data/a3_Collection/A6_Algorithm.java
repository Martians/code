package com.data.a3_Collection;

import com.data.a0_util.format.CollectionF;

import static com.data.a0_util.format.Display.*;

import java.util.*;

/**
 * 554
 * 判断两个结合是否有交界
 *
 *
 */
public class A6_Algorithm {

    static void collections() {
        ArrayList<Integer> array = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));

        /**
         * 预先填充N个相同的item
         */
        List<Integer> list = Collections.nCopies(10, 100);
        out(list);

        /**
         * 将单个对象变为数组
         */
        List<Integer> l2 = Collections.singletonList(10);
        out(l2);

        Map<Integer, Integer> l3 = Collections.singletonMap(10, 100);
        out(l3);
    }

    static void arrays() {

    }

    public static void main(String args[]) {
        collections();

        arrays();
    }
}
