package com.data.a3_Collection;

import com.data.a0_util.format.CollectionF;

import java.util.Arrays;
import java.util.LinkedList;

import static com.data.a0_util.format.Display.*;

/**
 * List 只是一个接口
 *
 *  这里展示的是其他实现了List接口的类
 */
public class A3_List {

    static void linkedList() {
        LinkedList<Integer> ll = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        ll.add(6);
        ll.addFirst(10);
        ll.addLast(10);
        ll.add(3, 8);

        out(CollectionF.list(ll));
    }

    static void priorityQueue() {

    }

    static void arrayyQueue() {

    }

    public static void main(String args[]) {
        linkedList();

        priorityQueue();
    }
}
