package a8_combine


/*
#cgo CFLAGS: -I "/D/Program Files/TDM-GCC-64/x86_64-w64-mingw32/include"
#cgo LDFLAGS: -lm
#cgo CFLAGS: -DPNG_DEBUG=1
#include <math.h>
#include <stdio.h>
#include <stdlib.h>

int hello() {
	return 1;
}
*/
import "C"
import "fmt"

/**
 * 	只在linux下起效，windows下路径指定失败（或者是没有指定 lib ?）
	注释和import之间，不能有空格

	http://wiki.jikexueyuan.com/project/go-command-tutorial/0.13.html
	1. #cgo 定义更多的选项
		#cgo CFLAGS: -DPNG_DEBUG=1      定义宏
		#cgo linux CFLAGS: -DLINUX=1    linux下的宏
		#cgo LDFLAGS: -lpng             链接

	2. go tool cgo
		会生成 _obj 目录，里边是中间文件
 */
func CCode() {
	fmt.Printf("code random: %v, hello: %v\n", int(C.random()), Hello())
	C.hello()

	n, err := C.sqrt(C.double(10))
	fmt.Printf("sqrt :%v, err: %v\n", n, err)
}

func Hello() int {
	return int(C.hello())
}