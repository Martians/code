package packa

import "fmt"

func Handle() {
	fmt.Println("a.go is package a.")
}

/**
 * 方式1：包级初始化函数
 */
func init() { fmt.Println("a init 1") }

func init() { fmt.Println("a init 2") }


/**
 * 方式2：对需要复杂处理的初始化，可以包装为一个匿名函数
 */
var pc [256]byte = func() (pc [256]byte) {
	for i := range pc {
		pc[i] = pc[i/2] + byte(i&1)
	}
	return
}()