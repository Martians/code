package com.data.a4_thread;

import java.io.IOException;

import static com.data.a0_util.format.Display.*;

/**
 * 485 P
 *
 * Java语言本身内置了同步支持，每个对象有自己的隐式监视器
 */
public class A1_Thread {

    static void mainThread() {
        /**
         * 获得当前主线程信息
         */
        Thread thread = Thread.currentThread();
        System.out.printf("name: %s, priority: %d, info: %s",
                thread.getName(), thread.getPriority(), thread);

        try {
            Thread.sleep(100);
            Thread.sleep(100, 100);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 子线程例子
     *
     * 除了继承Thread，也可以使用 new Thread(Runnable) 的方式
     *      这种方式不需要继承Thread，可以自由继承其他类
     */
    static class NewThread implements Runnable {
        Thread t;
        boolean suspendFlag = false;

        NewThread() {
            t = new Thread(this, "demo");
            out("child start");
            t.start();
        }

        /**
         * synchronized 类似于 C++中的 Critical？
         *
         * 对代码进行保护
         */
        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    /**
                     * 检查是否暂停
                     */
                    synchronized (this) {
                        while (suspendFlag) {
                            wait();
                        }
                    }
                    Thread.sleep(200);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            out("child %d complete", Thread.currentThread().getId());
        }

        synchronized void suspend(boolean set) {
            if (set) {
                suspendFlag = true;
            } else {
                suspendFlag = false;
                notify();
            }
        }
    }

    static void createThread() {

        NewThread n = new NewThread();
        try {
            Thread.sleep(500);
            n.t.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        out("main %d complete", Thread.currentThread().getId());
    }

    static void threadPool() {
        NewThread array[] = new NewThread[5];
        for (int i = 0; i < 5; i++) {
            array[i] = new NewThread();
        }

        try {
            for (int i = 0; i < 5; i++) {
                array[i].t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        out("main %d complete", Thread.currentThread().getId());
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 监视器， 是用作互斥锁的对象
     * 255 P
     *      已知源码：定义在方法的返回值
     *      没有源码：将对象的引用 放在synchromized代码快中，所有的成员方法调用都是互斥的
     */
    static void synchronize() {
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * thread通信
     *      暂停子线程、启动子线程
     *      虽然java中提供了相关语句 Suspend、resume等，但是不建议使用
     *      需要自己写代码，用标志的方式来实现
     *
     */
    static void waitNotify() {
        NewThread n = new NewThread();
        try {
            out("main sleep");

            n.suspend(true);
            Thread.sleep(500);
            Thread.sleep(500);
            n.suspend(false);

            n.t.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        out("main %d complete", Thread.currentThread().getId());
    }

    public static void main(String args[]) throws IOException {
        if (false) {
            mainThread();

            createThread();

            threadPool();

            synchronize();
        }

        waitNotify();
    }
}
