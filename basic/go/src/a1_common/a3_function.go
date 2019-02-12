package a1_common

import (
	"fmt"
)

/**
 * ## 特征
	没有默认参数值，不能通过参数名指定形参
	函数之间不可比较，只能判断是否是 nil

## 特性
	bare return：返回值有变量名，那么可以简写为 return
		不易过度使用，容易出错，有些变量没有被当前执行的代码覆盖到，可能还是初始值

	拥有名字的函数，必须在包级语法块中被声明（即：不能在函数内再声明函数）
	匿名函数可以访问完整的词法环境（lexical environment）

## 实现
	调用堆栈是可变的，按需增加大小，2K-1G

 */
func Square(n int) int { return n * n }

func Print() {
	f := Square
	fmt.Printf("function: %T", f)
}

/**
 * 	相邻的参数类型相同，可以省略前边的类型定义
	只有一个参数返回，可以不用括号
 */
func Add(a, b int) int {
	return 1
}

/**VA
 * 可变参数
	...Type是一个语法糖，本质上是一个数组切片，即 []type
	更方便调用方使用，不需要先将参数编程数组传入了

	可变参数可以继续传递给其他函数  VarArgsAnyType(args...)
 */
func VarArgs(args ...int) {
	fmt.Print("many: ")
	for _, arg := range args {
		fmt.Print(arg)
	}

	fmt.Println()
}

/**
 * 可以传入任意类型的可变参数
 */
func VarArgsAnyType(args ...interface{}) {
	fmt.Print("many: ")
	for _, arg := range args {
		fmt.Print(arg, ", ")
	}
	fmt.Println()
}

func MultiReturn() {
	/** 返回值可以命名，这样看起来更清晰 */
	s := func()(a, b int) {
		return 1, 2
	}

	v1, _  := s()
	v1, v2 := s()
	fmt.Println("multi return ", v1, v2)
}

/**
 * 匿名函数的写法
 */
func Anonymous() {
	f := func(a, b int) bool {
		return true
	}
	f(1, 2)

	/** 生成匿名函数后，不赋值，而是立即调用，只执行一次 */
	func(a, b int) bool {
		return true
	}(1, 2)
}

/**
	前提：支持匿名函数；函数在该语言中式第一类型
    包含自由变量的代码块，这些变量在包含代码块的环境中定义（不是在代码块中，或者全局环境中定义；即使是局部变量）
	例子：http://lib.csdn.net/article/go/33623

	B 184：函数值称为闭包，函数值不仅仅是一串代码，还记录了状态；存在着变量引用
		变量的声明周期，不由它的作用域决定

	B 185：匿名变量需要递归调用时，必须先声明一个变量
 */
func Closure() {
	fmt.Println("closure: ")
	var j int = 5

	/** 给a赋的值的类型是：一个匿名函数，该匿名函数执行后，返回的新的匿名函数

		这样是为了构建 i 是在局部范围生成的 */
	a := func()(func()) {
		var i int = 10
		return func() {
			i++
			fmt.Printf("i, j: %d, %d\n", i, j)
		}
	}()

	/** 将返回的匿名函数执行 */
	a()

	j *= 2
	a()
}

/**
 * B 191：循环变量的作用域，循环中生成的所有函数，共享相同的循环变量
		他们记录的是循环变量的内存地址，而不是某一个时刻的值，因此循环体中函数中记录的都是最优惠词迭代的值
		因此必须引入一个局部变量 dir := d，作为循环变量的副本
 */
func Trap() {
	// var rmdirs []func()
	// for _, d := range tempDirs() {
	// 	dir := d // NOTE: necessary!
	// 	os.MkdirAll(dir, 0755) // creates pare
	// 	rmdirs = append(rmdirs, func() {
	// 		os.RemoveAll(dir)
	// 	})
	// }
	// for _, rmdir := range rmdirs {
	// 	rmdir() // clean up
	// }
}


func Function() {
	fmt.Println("\n-------------------------- function:")

	Print()

	Add(1, 2)

	VarArgs(1, 2, 3, 4, 5)

	VarArgsAnyType(1, "bb", []int{1,2,3})

	MultiReturn()

	Anonymous()

	Closure()

	Trap()
}