package a4_thread

import (
	"fmt"
	"runtime"
	"sync"
)

/**
 * 使用 go build，go run或者go test时，增加 -race 标志，可以自动检测竞态条件
 */

/**
 * 	以共享数据的方式，实现协程间数据共享
 */

func MutexSync() {
	count := 0

	handle := func (lock *sync.Mutex) {

		lock.Lock()
		count++
		fmt.Println(count)

		lock.Unlock()
	}

	lock := &sync.Mutex{}

	for i := 0; i < 10; i++ {
		go handle(lock)
	}

	/** 等待调度完成 */
	for {
		lock.Lock()
		c := count
		lock.Unlock()

		runtime.Gosched()
		if c >= 10 {
			break
		}
	}
	runtime.Gosched()
}

func UnLock() {
	var l sync.Mutex
	l.Lock()
	defer l.Unlock()
}

func MutexRW() {
	// RWMutex
}

/**
 * 每次将只有一个能够获得chanel，其他人因为channel已经满了，无法写入需要等待
 */
var (
	sema = make(chan struct{}, 1) // a binary semaphore guarding balance
	balance int
)
func ChannelSync(amount int) {
	sema <- struct{}{} // acquire token
	balance = balance + amount
	<-sema // release token
}

/**
 * B 351：对一个变量，多个channel有读有写，即使是读也要 lock
		数据写入后，可能在cpu的缓冲里边，需要flush到主存储
		像channel通信或者互斥量操作这样的原语会使处理器将其聚集的写入flush并commit
 */
func MemSync() {

}

/**
 * 可用于全局初始化操作
 */
func OnlyOnce() {
	var once sync.Once

	handle := func() {
		fmt.Println("hello, world")
	}

	work := func () {
		once.Do(handle)
	}
	go work()
	go work()
}

func Atomic() {
	// func CompareAndSwapUint64(val *uint64, old, new uint64) (swapped bool)
}

func Syncing() {
	fmt.Println("\n-------------------------- Syncing:")

	MutexSync()

	ChannelSync(1)

	MemSync()

	UnLock()

	MutexRW()

	OnlyOnce()

	Atomic()
}