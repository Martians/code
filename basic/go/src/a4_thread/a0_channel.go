package a4_thread

import (
	"fmt"
	"sync"
	"time"
)

/**
 *
	Blog：https://blog.golang.org/pipelines

## 特性
	channel 只能传递一种值，是一种类型安全的管道
	是可以比较的，如果是相同的channel
	只有一个操作符 <-  可以使用： <- chann、chann <-

## 使用
	close 之后拒绝写入 channel，关闭一个channels还会触发一个广播机制
		关闭操作只用于断言不再向channel发送新，只有发送完之后才会调用
		只接受的channel调用close是错误
 */

/**
* select 的每个语句，必须是IO操作
*/
func Select() {
	count := 0
	ch := make(chan int, 1)
	for {
		/** 这里是随机写入 0、1？ */
		select {
		case ch <- 0:
		case ch <- 1:
		}
		i := <-ch

		if count = count + i; count > 10 {
			fmt.Println("select loop exit, last ", i)
			break
		} else {
			// fmt.Println("select loop", i)
		}
	}
}


/**
 * 超时后，向另一个channel中写入一个数据，这样就可以唤醒
 */
func Timeout() {

	timeout := make(chan bool, 1)
	ch := make(chan int)

	/** 方式1：设置超时等待函数 */
	go func() {
		// 等待1秒钟，注意写法
		time.Sleep(1e8)
		timeout <- true
	}()

	select {
		case <-ch:
		case <-timeout:
			fmt.Println("channel timeout 1")
	}

	/** 方式2：直接使用time的特定channel */
	ch = make(chan int)
	go func() {
		time.Sleep(time.Microsecond * 1000)
		ch <- 1
	}()

	select {
	case <-time.After(time.Microsecond * 5):
		fmt.Println("channel timeout 2")
	case <-ch:
		fmt.Println("recv data!")
	}
}

func TimeTick() {
	/** 方式1：适用于，整个生命周期都要用到的定时器 */
	tick := time.Tick(1 * time.Microsecond)
	for countdown := 10; countdown > 0; countdown-- {
		<-tick
	}

	/** 方式2： */
	ticker := time.NewTicker(1 * time.Microsecond)
	<-ticker.C
	ticker.Stop()
}

/**
 * 将值依次传递下去，在多个channel中处理
 */
func Stream() {

	// type PipeData struct {
	// 	value int
	// 	handler func(int) int
	// 	next chan int
	// }
	//
	// handle := func(queue chan *PipeData) {
	// 	for data := range queue {
	// 		data.next <- data.handler(data.value)
	// 	}
	// }
}

/**
 * 转换为单向 channel
 */
func Convert() {
	// ch4 := make(chan int)
	// ch5 := <-chan int(ch4)
	// ch6 := chan<- int(ch4)
}

func CheckClose() {
	//
}

/**
 * 同步Channels：不带缓存的channel将导致两边做一次同步操作
		接收者收到数据发生在唤醒发送者goroutine之前（happens before）

	也可用于当做锁来使用
 */
func SyncChanner() {

}

func WaitGroup() {
	sizes := make(chan int64)
	var wg sync.WaitGroup
	for i := 1; i< 5; i++ {
		wg.Add(1)

		// worker
		go func() {
			defer wg.Done()
			sizes <- 1
		}()
	}

	// closer
	go func() {
		wg.Wait()
		close(sizes)
	}()

	var total int64
	for size := range sizes {
		total += size
	}
	fmt.Println("wait group: ", total)
}

func Channel() {
	Select()

	Timeout()

	TimeTick()

	Stream()

	Convert()

	CheckClose()

	WaitGroup()
}