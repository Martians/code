package com.data.a3_Collection;

import com.data.a0_util.format.CollectionF;

import java.util.*;

import static com.data.a0_util.format.Display.*;

public class A4_Set {

    static List<Integer> array = Arrays.asList(1, 2, 3, 4, -1, 7, 10, 6);

    static void hashSet() {
        HashSet<Integer> hs = new HashSet<>(array);
        hs.add(100);
        out(CollectionF.list(hs));
    }

    /**
     * 多维护了一个链表，记录插入顺序
     *
     * 后续可以按照插入顺序进行迭代
     */
    static void linkedHashSet() {
        LinkedHashSet<Integer> hs = new LinkedHashSet<>(array);
        out(CollectionF.list(hs));
    }

    /**
     * 按照顺序排列的
     */
    static void treeSet() {
        TreeSet<Integer> hs = new TreeSet<>(array);
        out(CollectionF.list(hs));

        /**
         * 传入 Comparator 接口, 该接口是一个函数式接口
         *
         * 这里用 lamda 实现了 int compare(T o1, T o2);
         */
        TreeSet<Integer> cp = new TreeSet<Integer>((a, b) -> {
            return b.compareTo(a);
        });
        cp.addAll(array);
        out(CollectionF.list(cp));
    }

    public static void main(String args[]) {
        if (false) {
            hashSet();

            linkedHashSet();
        }
        treeSet();
    }
}
