package com.data.datasource.m4_reconstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.util.Assert;

@Profile("multi-4")
public class DataSourceContextHolder {

    private static ThreadLocal<String> CONTEXT
            = new ThreadLocal<>();

    public static void set(String current) {
        Assert.notNull(current, "current cannot be null");
        CONTEXT.set(current);
    }

    public static String get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}