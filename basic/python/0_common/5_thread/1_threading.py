#coding=utf-8

'''
    1. 全局解释锁GIL
        PYthon虚拟机（解释器主循环）只能有一个控制线程在执行，就像单核CPU一样
        每个执行的线程，必须先获得 GIL；除非线程执行IO

    2. 驼峰式函数都已经取消
        直接设置值：
        使用下划线：
'''

#######################################################################################
''' thread 已经被弃用
    1. 同步源于很少
    2. 对子进程何时退出没有控制
    3. Python3中已经被命名为 _thread
'''

#######################################################################################
''' threading
    1. 支持守护进城，即表明该线程是不重要的，它只是用来等待客户端的请求，而不做其他事情
        子线程都设置为守护线程，这样强制关闭主进程时，才能成功（不会等待子进程完成，整个线程立即退出）
    2.
'''

import threading
from time import sleep, ctime

def loop(index, second):
    print("start loop", index, 'at', ctime())
    sleep(second)
    print("loop", index, 'done')

def main():
    print('start ...')
    threads = []
    nloops = range(3)

    for i in nloops:
        t = threading.Thread(target=loop, args=(i, 5))
        threads.append(t)

    for i in nloops:
        threads[i].start()

    for i in nloops:
        threads[i].join()

    print("all done")

if __name__ == '__main__':
    main()
#######################################################################################
