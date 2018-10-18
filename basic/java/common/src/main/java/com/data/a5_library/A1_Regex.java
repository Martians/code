package com.data.a5_library;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.data.a0_util.format.Display.out;

/**
 * 998 P
 *
 *  http://www.cnblogs.com/gaopeng527/p/4523740.html
 *  https://www.cnblogs.com/xyou/p/7427779.html
 *
 *  http://www.runoob.com/java/java-regular-expressions.html
 */
public class A1_Regex {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    static void matchGroup() {
        String value = "int ,string(2)[10], int[8]";

        String match1 = "(\\w+)(\\(\\d+\\))*(\\[\\d+\\])*";
        Pattern pattern = Pattern.compile(match1);

        Matcher match = pattern.matcher(value);
        while (match.find()) {
            out("-- %s", match.group());

            /**
             *  group() 显示整个匹配
             *  group(0) 显示当前整个匹配
             *
             *  groupCount中不包括group(0)
             */
            for (int i = 1; i <= match.groupCount(); i++) {
                out("\t\t %d - %s, pos[%d-%d]", i, match.group(i), match.start(i), match.end(i));
            }
        }
    }

    public static void main(String args[]) {
            matchGroup();
    }
}
