package com.data.a8_stream;

import com.data.a0_util.format.CollectionF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.data.a0_util.format.Display.*;

/**
 * 987
 */
public class A1_Stream {

    static void toStream() {
        /**
         * Collection 变成 Stream
         */
        ArrayList<Integer> a1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        a1.stream();

        /**
         * Array 变成 Stream
         */
        Integer [] a2 = {1, 2, 3, 4, 5};
        Stream<Integer> ss = Arrays.stream(a2);
    }

    static void toList() {

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String args[]) {
        toStream();

        toList();
    }
}
