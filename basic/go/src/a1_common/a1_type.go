
package a1_common
// package main
import (
	"fmt"
	"os"
)

/**
 	1. 布尔类型
		true、false，不能从整形转换（类似于java）

	2. 不支持枚举变量
		使用 const、itoa模拟

	3. 语言库
	   	string：不可变类型
		map：hash map, 不能使用 cap(map)
   		slice：相当于 vector

		使用通用函数 len、cap、append、delete、copy 等对集合进行操作
 */

/**
 *  语言支持零值初始化机制，因此不存在未初始化的变量，如果不显示初始化，自动设置为对应类型的零值

	多种变量赋值方式
		var：通常用于, 需要制定变量类型；或者变量稍后会被重新赋值，而不需要给出初始值的地方
 */
func Variable() {
	/** 一次定义多个变量 */
	var (
		v1 int
		v2 string
	)

	/** 不指定类型，自动推导 */
	var v3 = 10

	/** 省略 var、类型，使用特殊的赋值符号，可以自动推导类型 */
	v4 := 10

	/** 多重赋值；这里直接交换变量，减少代码
		实际上属于 元组赋值，同时更新两个变量 */
	v3, v4 = v4, v3
	fmt.Println(v1, v2, v3, v4)

	/**
	 * 1. 简短赋值语句, := 是一个声明；而 = 是一个变量赋值操作
			简短赋值中出现的变量，可以是之前已经声明过的，此时将只是起到赋值作用
	 */
	f, err := os.Open("1")

	// 简短赋值语句，要求至少要声明一个新变量
	f, err2 := os.Create("1")

	// 如果两个返回值，都使用前边已经定义过的变量，可以只使用赋值语句即可，不能使用 :=
	f, err = os.Create("1")
	fmt.Println(f, err, err2)

	/**
 	 * 2. 简短赋值语句，注意：
			如果 cwd 为全局变量，函数内部用 cwd :=
			那么将生成一个新的变量 cwd，全局变量不会被赋值
 	 */
}


/**
 * 常量，必须在编译期间确定值
 */
func Constant() {
	/**
	 * 字面常量，是无类型的
			比如：-12 可以赋值给任何合法的类型， int、uint、int32等
	 */

	/**
	 * 定义常量，可以指定类型；可以不指定(那就是无类型的)
	 */
	const zero = 0.0

	/** 超出整数类型能表达的范围，但是它们依然是合法的常量 */
	const (
		_ = 1 << (10 * iota)
		KiB // 1024
		MiB // 1048576
		GiB // 1073741824
		TiB // 1099511627776 (exceeds 1 << 32)
		PiB // 1125899906842624
		EiB // 1152921504606846976
		ZiB // 1180591620717411303424 (exceeds 1 << 64)
		YiB // 1208925819614629174706176
	)
	fmt.Println("\nconst exceed: ", YiB/ZiB)
}

/**
 * 在每个空间范围内自增长
		只要手动定义一个变量，就会重置
 */
func Iota() {
	/**
	 *  省略了b、c的定义，默认与a的定义一致
     */
	const (
		a = iota
		b
		c
	)
	fmt.Println(a,b,c)
}

/**
 * 语言不支持enum，用此方式模拟
 */
func Enum() {
	const (
		Sunday = iota
		Monday
		Tuesday
		Wednesday
		Thursday
		Friday
		Saturday
		numberOfDays // 这个常量没有导出（小写开头）
	)
}

/** int8、 byte、 int16
	int、 uint：长度平台相关
	uintptr：同指针
 */
func IntType() {
	// uintptr

	/** int、int32是不同类型，不同的整形之间不能比较、运算，必须进行强制类型转换 */
	var(
		v1 int = 1
		v2 int32 = 1
	)
	v2 = int32(v1)
	println(v2)

	/** uintptr 用于指针运行时使用；底层编程时用到 */
}


/**
 *  数组是固定长度的，是僵化的类型，一般很少使用
	数组式值类型的，当做参数传递时，将执行复制

	语法：长度写在前边，长度|类型|初始值
 */
func Array() {
	array := [32]byte {1,2,3}

	/** 数组长度，根据初始值个数来计算 */
	q := [...]int{1, 2, 3}
	fmt.Printf("%T\n", q) // "[3]int"

	/** 定义100个元素，最后一个元素为-1 */
	r := [...]int{99: -1}
	fmt.Printf("%T\n", r) // "[3]int"

	/** 数组的值可以改变 */
	array[1] = 9
	fmt.Println("\narray: ", array)

	/** 数组遍历 */
	fmt.Print("array loop: ")
	for _, v := range array {
		if v != 0 {
			fmt.Print(v, " ")
		}
	}

	/** 不同长度的数组不可以比较 */
	a := [2]int{1, 2}
	b := [...]int{1, 2}
	fmt.Println(a == b)
}

func Types() {
	 fmt.Println("\n-------------------------- Types:")

	 Variable()

	 Constant()

	 Iota()

	 Enum()

	 IntType()

	 Array()
 }