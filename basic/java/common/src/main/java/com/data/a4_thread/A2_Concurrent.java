package com.data.a4_thread;

import java.io.IOException;

/**
 * https://blog.csdn.net/axi295309066/article/details/65665090
 */
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
     * 1. 最初接口：
     *      Callable：请求的接口
     *          抽象接口，需要实现该方法：call方法；该方法 如果不能计算结果，必须跑出异常
     *
     *      Future：返回结果接口
     *          有默认实现
     *          get、get(wait, unit)
     *
     * 2. CompletableFuture
     *      资料
     *          《Java8实战》P224
     *          http://www.importnew.com/28319.html
     *          https://www.jianshu.com/p/4897ccdcb278
     *
     *      1）基础元素
     *              执行方设置结果：用户相关耗时工作完成后，使用futurePrice.complete将结果设置进去
     *                             completeExceptionally，返回异常
     *              请求方等待结果：Client端使用使用futurePrice.get即可得到结果
     *
     *      2）线程池
     *              1）默认使用的是ForkJoinPool，将会生成 Runtime.getRuntime().availableProcessors() 个线程
     *              2）可以在请求执行时传入自己指定的线程池 executor，控制性更强
     *
     *      3）异步流水线
     *              场景：位于流式处理过程中，连续多个map，每个map执行一个操作
     *
     *                  1. 第一个map中，将参数交给 future 任务去执行；这时流中存放的是 Future<T>
     *                  2. 后续的map中，对每个future继续执行操作
     *                          1）thenApply：转换future中T的类型
     *                                  即将上一个结果类型的future，转换为本次处理后类型的future，继续交给流进行下一步处理
     *
     *                          2）thenCompose：P239，同时需要两个future中的数据时
     *                                  第一个future中操作执行完毕后，将结果作为参数传递给第二个future的操作函数；最终返回的是第二个future的对象类型的future
     *                                  猜测：这与 thenApply 的区别是，thenApply需要等stream中所有元素都准备好之后，再执行下一个map？thenCompose可以针对每个元素立即启动？
     *
     *                          3）thenCombine：两个操作不是串联的（串联即：一个操作依赖于另一个操作的结果），而是都需要
     *                                  类似于thenCompose，但是多一个参数：两个future都返回后，处理两个future的结果，并生成新的结果
     *
     *                          4）handle：处理异常
     *
     *                          5）thenAccept：将流中的值消耗掉，返回 future<void>
     *
     *                  3. 等待结果
     *                          CompletableFuture.allOf(futures).join();
     *
     *      ## 说明
     *      1. future的多个 then... 操作中，仍然可以使用async版本
     *              这将会将处理再次丢入到线程池中，一般针对耗时操作，避免阻塞了后台处理线程
     *              如果是小开销工作，就不需要这样处理，反而造成线程切换
     *
     *      2. java stream中的实现
     *          发现了元素是future，就会等待其执行完成？
     *          这种方式有把任何操作封装为异步的能力？（并不需要对操作执行futurePrice.complete，直接取耗时函数的返回值？）
     *          java将同步操作交给到后台的线程去执行，执行完成后通知前端
     *
     *      3. 严重依赖于闭包，封装到 lamda 中的很多变量都是闭包；否则写起来将非常麻烦
     */

    /////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static void main(String args[]) throws IOException {

    }
}
