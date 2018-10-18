package packb

import (
	"fmt"
)

func Handle() {
	/** 直接使用，相同 package 中其他文件中的函数 */
	Handle1()
	fmt.Println("b.go is package b.")
}
