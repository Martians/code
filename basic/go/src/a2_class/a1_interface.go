package a2_class

import (
	"fmt"
	"io"
	"os"
)

/**
 * 一个类型可以自由地被另一个满足相同接口的类型替换，被称作可替换性(LSP里氏替换)

	B 237:接口赋值时，如果是 指针类型实现了该接口，那么赋值时，也必须使用 指针地址
var _ fmt.Stringer = &s /

	var _ io.Writer = (*bytes.Buffer)(nil)，用于在编译器断言，bytes.Buffer的确实现了io.Writer接口


接口是可以比较的：接口相等，即动态类型相同，并且动态值也根据这个动态类型的 == 操作相等
	B 247：如果接口有了动态类型，那它就不是nil的；最后如何修改 成功，这里不是特别明白！！！！

## 实现



 */

/**
	接口分类：
		1. 侵入式：知道需求方需要的所有接口，并提前声明实现这些接口是不合理的
		2. 非侵入式：只需要接口中所有函数的类型一致，就认为接口是相同的
 */

/** ---------------------------------- */
		type Integer int
		func (a Integer) Less(b Integer) bool {
			return a < b
		}
		func (a *Integer) Add(b Integer) {
			*a += b
		}
		/** ---------------------------------- */
		type LessAdder interface {
			Less(b Integer) bool
			Add(b Integer)
		}

/**
 * 两个接口不是完全匹配，但是go可以自动生成一些代码
 */
func InterfaceConvert() {

	var a Integer = 1
	/** 两个interface不完全匹配：
			要不都改成指针类型：Less(b *Integer)、Add(b *Integer)
			要不都改成引用类型：Less(b Integer)、Add(b Integer)

		因为不影响正确性，将自动生成代码进行匹配
		func (a *Integer) Less(b Integer) bool {
			return (*a).less(b)
		}

		这样，*Integer类型，就相当于即实现了 Less，也实现了Add方法
		这里，实现了LessAdder接口的类型，实际上是 *Integer

		但是反过来无法生成，因为该函数无效 integer.Add需要指针才能起效果，修改内部的值
		func (a Integer) Add(b Integer) {
			(&a).Add(b)
		}
	*/
	var b LessAdder = &a
	fmt.Println("InterfaceConvert: ", b)
}

/**
 	接口查询：
		1. 在COM领域很常见：从对对象一无所知，到逐步了解
		2. 接口是对一组类型的公共特性的抽象

	检查某个实例指向的对象，是否是辉县了某个接口
 */
func InterfaceQuery() {
	var a Integer = 1
	var b LessAdder = &a

	type Work interface {
		Add(b Integer)
	}

	// if c, ok := a.(LessAdder); ok {
	if c, ok := b.(LessAdder); ok {
		fmt.Println("b is LessAdder ", c)
	}

	if c, ok := b.(Work); ok {
		fmt.Println("a is Work ", c)
	}
}

/**
 * 将多个接口组合在，就相当于定义了一个大接口
 */
func InterfaceCombine() {

}

/**
 * 由两个部分组成，一个具体的类型和那个类型的值。它们被称为接口的动态类型和动态值
		总是被一个定义明确的值初始化，包括接口值

	B 244：编译器不知道接口的动态类型，一个接口上的调用必须使用动态分配
一个接口可以持有任意大的动态值（不应该是直接保存指针?）

 */
func Assign() {
	// 等价于 w := io.Writer(os.Stdout)
	w := os.Stdout
	w.Write([]byte("assign: hello"))
}

/** B 250
	自动sort接口：sort.Sort(byArtist(tracks))
	sort 包提供了特定版本的函数和类型
*/
func Sort() {

}

/**
 * B 272：在接口值上的操作, x.(T)被称为断言类型
	1. 具体类型的类型断言从它的操作对象中获得具体的值
	2.

	注意：这里的例子，都重用了变量名 w，实际上是生成了一个同名的新的本地变量，外层原来的w不会被改变
## 用处
	B 278：fmt.Fprintf，判断类型是否实现了erro接口、Stringer接口
 */
func TypeAssert() {
	/** 1. T 是具体类型，检查X与T是否相同
	       结果是x的动态值，类型是T */
	var w io.Writer
	w = os.Stdout
	if w, ok := w.(*os.File); ok {
		fmt.Println("type assert: ", w)
	}

	/** 2. T是接口类型
			结果仍然是具有相同动态类型和值部分的接口值，类型为T
			改变了可以获取的方法的集合（通常是变大了）; 不需要断言一个更小范围的接口（这仅仅相当于赋值） */

	w = os.Stdout
	if w, ok := w.(io.ReadWriter); ok {
		fmt.Println("type assert: ", w)
	}
}


func Interface() {
	fmt.Println("\n-------------------------- interface:")

	InterfaceConvert()

	Assign()

	Sort()

	TypeAssert()
}