package com.data.a2_class;
import static com.data.a0_util.format.Display.*;

/**
 * 接口中，默认所有的方法、变量都声明为 public
 *
 * 接口与类的主要区别是，接口不能有实例变量来维护状态信息
 *
 * 更多函数式接口，见 638 P
 */
public interface A3_Interface {
    /**
     * 只能声明final的变量，默认都会加上final
     *
     * 相当于 define 常量, C++中的const；这种方法不常用？
     */
    final int value = 10;
    int NO = 0;
    int YES = 1;


    void handle();

    /**
     * 默认接口方法，增加 default 前缀
     */
    default void callback(int p){
        out("default " + p);
    }

    default void working(int p){
        out("working not implemented.");
    }

    /**
     * 可以定义静态方法
     */
    static int getNumber() {
        return 0;
    }

    /**
     * 接口中可以定义静态类
     */
    static class AB {
        /**
         * 成员接口、嵌套接口
         */
        public interface NestedIF {
            void good();
        }
    }

    static class Client implements A3_Interface, AB.NestedIF {
        public void handle() {
            out("client handle");
        }

        /**
         * 实现接口的方法时，必须是public的
         */
        public void callback(int p) {
            out("client " + p);
        }

        public void good() {
            out("good");
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String args[]) {
        Client c = new Client();
        c.handle();
    }
}


