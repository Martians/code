package a1_common

import (
	"errors"
	"fmt"
	"io"
	"log"
	"os"
	"runtime"
	"time"
)

/**
 * 错误处理策略：输出错误并结束程序，应该只在main包中进行
 */

/**
 * 只要实现了 Error() string，即可当做error传递
	B 260
 */
func Error() {
	var err = errors.New("EOF error")

	if err != io.EOF {
		fmt.Println("error: ", err, ",", io.EOF)
	}

	// 有专门的函数，来判断是否是指定错误
	// func IsExist(err error) bool
	// func IsNotExist(err error) bool
	// func IsPermission(err error) bool
}

/**
 * panic不会影响 defer的执行；
		recover也需要放在 defer中进行恢复
 */
func Panic() {
	defer func() {
		if r := recover(); r != nil {
			log.Printf("Runtime error caught: %v", r)
		}
	}()

	panic(100)
}

func Stack() {
	var buf [4096]byte
	n := runtime.Stack(buf[:], false)
	fmt.Println("\n------ Print stack:")
	os.Stdout.Write(buf[:n])
}

/**
 * defer 相关
	1. B 198：甚至可以修改返回值（首先需要，对返回值命名，defer的函数中直接使用这个命名变量）
	2. 注意：循环中设置多个 defer，只有在整个函数结束后，才会执行defer，可能造成某些资源不够用了
 */
func bigSlowOperation() {
	defer trace("bigSlowOperation")() // don't forget the extra parentheses
	time.Sleep(time.Second) // simulate slow operation by sleeping
}

/**
 * 调试技巧：一个语句，同时打印输入输出
 */
func trace(msg string) func() {
	start := time.Now()
	log.Printf("enter %s", msg)
	return func() {
		log.Printf("exit %s (%s)", msg,time.Since(start))
	}
}

func ErrorHandle() {
	fmt.Println("\n-------------------------- Error:")

	bigSlowOperation()

	Error()

	Panic()

	Stack()
}
