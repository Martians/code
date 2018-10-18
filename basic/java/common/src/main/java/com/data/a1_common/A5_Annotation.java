package com.data.a1_common;
import static com.data.a0_util.format.Display.out;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@interface MyAnno {
    /**
     * 提供默认值
     */
    String str() default "working";
    int val() default 100;
}

/**
 * 标记注解，不包含任何成员
 */
@Retention(RetentionPolicy.RUNTIME)
@interface What {}

/**
 * 单成员注解, 成员名字必须是 value
 * 这样后续使用时，可以省略成员名
 */
@Retention(RetentionPolicy.RUNTIME)
@interface Single {
    String value();

    int xyz() default 0;
}


/**
 * 注解用于外部工具的解析，相当于在语言外部的扩展
 */
@What
@MyAnno
@Single("single param")
public class A5_Annotation {

    @MyAnno(str = "Annotation", val = 100)
    public static void myMeth(String value) {
        A5_Annotation ob = new A5_Annotation();
        try {
            /**
             * 获取class -> 获取method -> 获取method注解
             */
            Class<?> c = ob.getClass();

            Method m = c.getMethod("myMeth", String.class);
            MyAnno anno = m.getAnnotation(MyAnno.class);
            out("get anno str: %s, val: %s", anno.str(), anno.val());

            /**
             * 获取class的注解
             */
            out("list all: ");
            for (Annotation a : c.getAnnotations()) {
                out(a.toString());
            }

        } catch (Exception e) {
            out(e.toString());
        }
    }

    /**
     * 类型注解, 无法识别？
     */
//    public String @MaxLen(10) [] checkType() {
//
//    }


    public static void main(String args[]) {
        myMeth("11");
    }
}
