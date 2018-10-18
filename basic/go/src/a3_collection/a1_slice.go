package a3_collection

import (
	"fmt"
	"reflect"
)

/**
 *  切片是一个管理结构，内部包含了一个原生数组，记录数组的个数、存储空间
		slice是一个轻量级的数据结构，主要是指针、长度、容量

	可以从数组、其他slice中、make构建
	复制一个slice只是对底层的数组创建了一个新的slice别名

	slice之间不能比较（可以使用 bytes.Equal 来比较字节型的slice）
		slice是间接引用的，甚至可以包含自身，没有简单有效的比较方法
		底层元素可能修改，扩容后导致地址发生变化

 */
func Slice() {
	/** nil slice，生成对应类型slice的nil值 */
	s1 := []int(nil)
	var s2 []int

	// len(s3) == 0, s3 != nil
	s3 := []int{}
	fmt.Println("nil slice: ", s1 == nil, s2 == nil, s3 == nil)

	fmt.Println()
	/** a1 是数组，a2 是切片 */
	var a1 [10]int = [10]int{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
	var a2 []int = []int{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
	fmt.Println("type a1", reflect.TypeOf(a1), ", type a2", reflect.TypeOf(a2))

	/** 这里将产生一个匿名的数组变量 */
	my1 := make([]int, 5)
	my2 := []int{1,3}
	fmt.Println("slice: ", my1, ", ", my2)

	/** 需要对append的结果，直接赋值给slice
		bible 130，新的修改可能导致空间重新分配，产生了不同的slice，因此需要赋值给原来的slice变量 */
	my2 = append(my2, 1, 2, 3)
	fmt.Println("my2 new: ", my2)

	/** 传入 slice，需要...打散 */
	my2 = append(my2, my1...)
	fmt.Println("my2 new: ", my2)

	my3 := my2[5:]
	fmt.Println("my3: ", my3, ", cap: ", cap(my3))

	/** 按照较小的 len 进行复制，而不是cap */
	copy(my3, my2)
	fmt.Println("my3 copy: ", my3)
}