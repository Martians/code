package main

/**
	go 就是为开源软件设计的，因此将所有软件放在统一的目录下：src、pkg、bin
	go env 查看当前设置

	包名是从 GOPATH/src 下开始的

## 特性
	匿名导入：B 377，通常用于在main入口选择性地导入附加的包，import _ "image/png"
	internal包：可以被同一个父目录的包所导入

## 执行
	go run calc/calc.go
	go run a0_project/a0_normal/calc

*/

import (
	"a0_project/a0_normal/packa"
	"a0_project/a0_normal/packb"
)

func main() {
	packa.Handle()

	packb.Handle()
}
