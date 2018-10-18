package a1_common

import (
	"fmt"
	"syscall"
)

/**
 * 	API：https://studygolang.com/pkgdoc
	blog：http://lib.csdn.net/article/go/37001
 */
func NormalPrint() {
	x := int64(0xdeadbeef)

	/** [1]表示编号，再次使用第一个操作数 */
	fmt.Printf("%d %[1]x %#[1]x %#[1]X\n", x)

	/** 返回error接口 */
	// fmt.Errorf

	/** 数字类型Errno */
	var err error = syscall.Errno(2)
	fmt.Println(err.Error())
	fmt.Println(err)
}

/**
	执行方式
		1. print首先检查类型是否实现了String方法
		2. 如果未实现，就利用反射进行遍历成员变量打印

	打印格式
	  	v  默认值显示，verb；变量的自然形式
				所有值都可以这么显示，它自动根据类型转换为 %s、%d等； fmt.print相当于对每个value调用 %v 来显示
		+v 更详细，显示struct的字段名
		#v 值的go语法表示，#副词；如果是数字将打印 0x 前缀
		T  值类型的表示，带包名；即变量的类型， reflect.Type
		c  字符rune，unicode码点
		q  带单引号或者双引号的字符串
 */
func StructPrint() {
	type Sample struct {
		abc int
		str	string
	}

	s := new(Sample)
	s.abc = 1
	s.str = "hello"

	fmt.Printf("v: %v,\n +v: %+v,\n #v: %#v,\n T: %T,\n %: %%\n", s, s, s, s, s.abc)
}

func ErrorPrint() {
	fmt.Errorf("print error")
}

func PrintHandle() {
	fmt.Println("\n-------------------------- print:")

	NormalPrint()

	StructPrint()

	ErrorPrint()
}