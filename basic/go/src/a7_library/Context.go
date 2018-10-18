package a7_library

/**
## 使用
 	官网：https://blog.golang.org/context
	翻译：http://chuansong.me/n/697130652137
	例子：https://studygolang.com/articles/10155?fr=sidebar

	源码分析：https://www.jianshu.com/p/b7202f2bb477

## 实现
	返回的cancel函数，是一个闭包，其中封装了变量参数等，非常方便

 */

import (
	"context"
	"fmt"
	"time"
)

func WithCancel() {
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	// 执行者需要检查 parent是否已经终止
	go func() {
		for {
			select {
			case <- ctx.Done():
				fmt.Println("parent done, child exit")
				return
			default:
				fmt.Println("child work")
				time.Sleep(1 * time.Second)
			}
		}
	}()

	time.Sleep(3 * time.Second)
	fmt.Println("main done")

	/** 等待执行 cancel */
}

func WithTimeout() {
	ctx, cancel := context.WithTimeout(context.Background(), 3 * time.Second)
	defer cancel()

	// 执行者需要检查 parent是否已经终止
	go func() {
		for {
			select {
			case <- ctx.Done():
				fmt.Println("parent done, child exit")
				return
			/**
				这里注释的三行代码，可以打开，可以关闭
				1. 打开：因为有default，因此select将被频繁唤醒
				2. 关闭：只有从ctx.Done中读到数据时，才会关闭
			*/
			// default:
			// 	fmt.Println("child work")
			// 	time.Sleep(1 * time.Second)
			}

		}
	}()

	/** parent可以等待一个时间，使得chiild先打印信息 */
	time.Sleep(4 * time.Second)
	select {
		case <-ctx.Done():
			fmt.Println("main wait timeout")
	}
}

func ContextHandle() {
	/** 最好是每次只测试一个函数，输出更明确 */
	WithCancel()

	WithTimeout()

	/** 确保child那边能够输出信息 */
	time.Sleep(1 * time.Second)
}
