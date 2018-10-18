package com.data.util;

import java.util.Arrays;
import java.util.List;

public class StringFormat {

    public static String list(List list) {
        StringBuffer total = new StringBuffer();
        int count[] = {0};

        list.stream().forEach(x -> total
                .append("\n\t")
                .append(++count[0])
                .append(". ")
                .append(x));
        return total.toString();
    }

    public static void main(String[] args) {
        List list = Arrays.asList("1", "2");
        System.out.println(list(list));
    }
}
