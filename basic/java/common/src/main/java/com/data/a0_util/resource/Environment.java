package com.data.a0_util.resource;

import com.data.a0_util.format.CollectionF;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import static com.data.a0_util.format.Display.out;


public class Environment {

    /**
     * 得到资源文件的完整路径
     *
     * Configure.class.getResource, 相对于class所在的路径
     *      1) Configure.class.getResource
     *      2) this.getClass().getResource
     *          path  以 / 开头，从CLASSPATH下获取，可能就是src目录？
     *          path不以 / 开头，从类所在的包下获取；这个类即为 ClassName.class.getResource 的这个class
     *
     * Configure.class.getClassLoader, 相对于CLASSPATH根目录
     *      1）Thread.currentThread().getContextClassLoader，推荐此方式
     *      2）ClassLoader.getSystemClassLoader
     *          path 不能以 / 开头
     */
    //http://www.cnblogs.com/alwayswyy/p/6421267.html
    public static URL getURL(String path) {
        try {
            // relative path is for .class file or jar
            URL url = Configure.class.getResource(path);
            if (url == null) {
                throw new FileNotFoundException(path);
            }
            return url;

        } catch (Exception e) {
            System.out.println("get resource " + path + ", but failed: " + e);
            return null;
        }
    }

    static void resourcePath() {
        //////////////////////////////////////////////////////////////////////////////////////////////////
        /** 获得资源文件完整路径 */
        String path = Configure.path;
        URL url = Environment.getURL(path);
        System.out.println(url.getPath() + " \n" + url.getFile());
    }

    public static void listPath() {
        //////////////////////////////////////////////////////////////////////////////////////////////////
        /**
         * 比较多种方式的区别
         */
        System.out.println("\nresource path: ");
        /** class.getResource */
        System.out.println(Environment.class.getResource("").getPath());
        System.out.println(Environment.class.getResource(""));
        System.out.println("\nclass path: ");
        System.out.println(Environment.class.getResource("/"));

        /** class.getClassLoader().getResource */
        System.out.println(Environment.class.getClassLoader().getResource(""));

        /** Thread.currentThread().getContextClassLoader */
        System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
        //System.out.println(Thread.currentThread().getContextClassLoader().getResource("/"));  /** invalid */

        /** ClassLoader.getSystemResource */
        System.out.println(ClassLoader.getSystemResource(""));
        //System.out.println(ClassLoader.getSystemResource("/")); /** invalid */

        //////////////////////////////////////////////////////////////////////////////////////////////////
    }

    /**
     * 查看环境变量
     */
    static void getEnv() {
        Map<String, String> map = System.getenv();
        out(CollectionF.list(map));
    }

    /**
     * 查看环境属性
     */
    static void getProperties() {
        Properties prop = System.getProperties();
        out(CollectionF.list(prop));
    }

    static void normalPath() {
        /** system style, maybe windows */
        System.out.println(System.getProperty("user.dir"));

        System.out.println(System.getProperty("java.class.path"));
    }

    public static void main(String[] args) {

        resourcePath();

        listPath();

        getEnv();

        getProperties();

        normalPath();
    }
}
