package com.data.a0_util.format;

import static java.lang.System.out;

public class Display {

    public static void out(String format, Object ... args) {
        out.printf(format + "\n", args);
    }

    public static <T> void out(T str) {
        out.println(str);
    }
}
