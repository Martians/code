package com.data.datasource.m3_dynamic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.util.Assert;

/**
 * 线程绑定变量
 */
@Slf4j
@Profile("multi-3")
public class DataSourceContextHolder {

    private static ThreadLocal<String> CONTEXT
            = new ThreadLocal<>();
    //public static List<String> dataSourceIds = new ArrayList<String>();

    public static void set(String current) {
        Assert.notNull(current, "current cannot be null");
        CONTEXT.set(current);
    }

    public static String get() {
        return CONTEXT.get();
    }
//    public static boolean contain(String source) {
//        return CONTEXT.get();
//    }

    public static void clear() {
        CONTEXT.remove();
    }
}