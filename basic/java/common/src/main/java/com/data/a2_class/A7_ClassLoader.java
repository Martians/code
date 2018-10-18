package com.data.a2_class;

/**
 * class loader:
 *  1. 资料
 *      自定义classloader：https://blog.csdn.net/briblue/article/details/54973413
 *      Oracle官方：https://docs.oracle.com/javase/7/docs/technotes/tools/findingclasses.html
 *
 *  2. 实现
 *      系统中默认的三个classloader，每个类加载器都有一个父加载器
 *      Bootstrap：jre/lib、rt.jar
 *              -Xbootclasspath  System.getProperty("sun.boot.class.path")；由C++编写的
 *              tools.jar 需要单独指定，并未包含在此中
 *
 *      Extention：lib\ext
 *              -D java.ext.dirs，System.out.println(System.getProperty("java.ext.dirs"))
 *                  C:\Program Files\Java\jre1.8.0_91\lib\ext;C:\Windows\Sun\Java\lib\ext
 *
 *      Appclass：CLASSPATH中查找
 *              SystemAppClass、User class
 *              -D java.class.path，System.out.println(System.getProperty("java.class.path"));
 *              运行时可以使用 -classpath 覆盖
 *
 *      Context ClassLoader 线程上下文类加载器，是一个逻辑概念，用于不同的线程；默认为 Appclass loader
 *
 *  3. 搜索
 *      双亲委托方式查找，它首先判断这个class是不是已经加载成功（已经加载在缓存中）
 *      如果没有的话它并不是自己进行查找，而是先通过父加载器，然后递归下去，直到Bootstrap ClassLoader
 *      如果到了Bootstrap classloader还没有找到，就进行加载；并一级一级返回，最后到达自身去查找这些对象
 *
 *      这样可以避免重复加载，当父亲已经加载了该类的时候，就没有必要子ClassLoader再加载一次
 *
 * classpath:
 *      https://blog.csdn.net/wk1134314305/article/details/77940147
 *
 *  1. 使用
 *      class：指定class文件，要传入 父目录
 *             class中带包名，需要指定的是包的根目录
 *
 *      jar：  要指定jar中的class，需要传入jar的完整路径
 *             通配符写法，适用于目录下所有的jar，不能用于指定class文件
 *
 *      通用：  path指定的目录目录不是递归搜索的
 *  2. 配置
 *      通常要配置 .; 加入到classpath
 *
 */
public class A7_ClassLoader {
}
