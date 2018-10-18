package com.data.a4_thread;

import java.io.IOException;

public class A2_Concurrent {

    /**
     * 设置初始值，计数 > 0可用
     */
    static void semaphore() {
    }

    /**
     * 两个线程交换数据
     *      exchange(object)
     */
    static void exchanger() {
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 锁存器：等待条件成立，大家一起去做一件事情
     *      所有人等待，直到计数器编程0
     *
     *      1) 设置初始值，等待的个数
     *      2）await()
     *      3）countDown()，减少等待个数
     *      4）等待者都唤醒
     */
    static void countDownLatch() {
    }

    /**
     * 线程等待器，执行屏障：
     *      必须有多个线程，在预定的执行点等待，直到所有线程都达到执行点位置；提供了相当流线化的解决方案
     *      记录了线程信息
     *
     *      1）设置线程数
     *      2）所有线程执行 wait，表明到达了执行点，会减少等待个数
     *      3）最后一个线程达到执行点，执行传入的一个新线程；其他线程唤醒
     */
    static void cyclicBarrier() {
    }

    /**
     * 938 P
     * 线程阶段协调器：类似于 CyclicBarrier，但是支持多个阶段
     *      协调多个线程，一起完成任务的几个阶段
     *
     *      1）参与者注册 register, 然后就知道当前阶段有几个线程要被等待了
     *      2）参与者到达 arrive、arriveAndAwaitAdvance；所有人到达，并且主线程已经准备好时，进入下一个阶段
     *      3）arriveAndDeregister，线程在到达时注销自身，就不会进入下一个阶段了
     *
     *      为了更精确控制，可以继承 Phaser，实现 OnAdvance，只运行指定数量的阶段
     */
    static void phaser() {

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 执行器：
     *      启动并控制线程的执行，由系统实现的线程池，使用时传入 Runnable 接口的类；减轻了创建独立线程带来的负担
     *
     * 接口：
     *      Executor
     *          -- ExecutorService: 添加了管理和控制线程的方法,shutdown
     *              实现：ThreadPoolExecutor：线程池
     *                    ForkJoinPool
     *              -- ScheduleExecutorService：支持线程调度
     *                  实现：ScheduledThreadPoolExecutor，允许调度线程池
     *
     * 获得：ExecutorService
     *          1）调用 Executors 的工厂方法
     *              newCachedThreadPool
     *              newFixedThreadPool
     *              newScheduleThreadPool
     *          2）将Runnable类添加到ExecutorService中
     *          3）ExecutorService对象 shutdown
     */
    static void execute() {
    }

    /**
     * 异步执行
     *      在执行器的基础上，直接将任务提交给执行器线程池，不需要关注具体线程信息
     *
     * 接口：
     *      Callable：请求的接口
     *          抽象接口，需要实现该方法：call方法；该方法 如果不能计算结果，必须跑出异常
     *
     *      Future：返回结果接口
     *          有默认实现
     *          get、get(wait, unit)
     *
     *  不能直接传入一个回调？
     */

    /////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static void main(String args[]) throws IOException {

    }
}
