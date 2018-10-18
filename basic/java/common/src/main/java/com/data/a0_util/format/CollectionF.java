package com.data.a0_util.format;

import java.util.*;

public class CollectionF {
//    interface Display<T> {
//        String handle(T t);
//    }
//    <T> CollectionF(T v) {
//        System.out.println(list(v));
//    }

    public static String list(Collection list, String output) {
        return output + "\n" + CollectionF.list(list);
    }

    /**
     * origin array
     */
    public static <T> String list(T[] list) {
        return list(Arrays.asList(list));
    }

    /**
     * collection: list、set
     */
    public static String list(Collection list) {
        StringBuffer buffer = new StringBuffer();
        int count[] = {0};

        list.stream().forEach(x -> buffer
                .append("\t")
                .append(++count[0])
                .append("> ")
                .append(x)
                .append("\n"));
        /** remove last \n */
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    /**
     * map
     */
    static final String empty = "                                                                                     ";
    public static <K, V> String list(Map<K, V> map) {
        /** get max key length */
        int length = 0;
        for (K k : map.keySet()) {
            length = Integer.max(length, k.toString().length());
        }
        length = Integer.min(length, empty.length());

        StringBuffer buffer = new StringBuffer();
        int count[] = {0};
        for (Map.Entry<K, V> entry : map.entrySet()) {
            buffer.append("\t")
                    .append(++count[0])
                    .append("> ")
                    .append(entry.getKey())
                    .append(empty, 0,length - entry.getKey().toString().length())
                    .append(" -> ")
                    .append(entry.getValue())
                    .append("\n");
        }
        /** remove last \n */
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    public static void main(String[] args) {
        List list = Arrays.asList("1", "2");
        System.out.println(list(list, "work"));
        System.out.println(list(list, "work"));
    }
}

