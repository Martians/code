package a3_collection

import "fmt"

/**
 * 	map的顺序随机，每次已运行都会变化，放置程序依赖于特定的遍历顺序
	对nil的map执行 len、delete 等操作，也是安全的

	1. Key的要求
		要求key必须是支持 == 运算符的类型
		key不是一个变量，不能对map中的key取地址，因为map可能重新分配内存空间，导致之前的key地址无效

	2. map之间不能比较

	set 通常用key为 struct{}{} 的map来模拟， struct{}是类型，{} 表示生成实例
 */
func Map() {
	/** 创建空map，map 使用前必须先创建出来*/
	// 方式1
	ages := make(map[string]int)
	// 方式2
	ages = map[string]int{}
	fmt.Println(ages)

	/** 遍历 map */
	for name, age := range ages {
		fmt.Printf("%s\t%d\n", name, age)
	}

	/** 预先构建容量 100 */
	m1 := make(map[int] string, 100)

	/** 赋值时，即使对应的键还不存在，也会自动创建 */
	m1[1] = "11"
	m1[2] = "22"
	fmt.Println("map: ", m1)


	/** 方式1：获取 value 时返回两个值，第二个用于判断结果
			不能通过value来判断是否成功，因为存储的 value 本身可能就是 nil

		方式2：使用只一个返回值的函数形式，但是如果key不存在，就会panic */
	v, ok := m1[1]
	if ok {
		fmt.Println("value:", v, ok)
	} else {
		fmt.Println("mot find")
	}

	/** 创建并初始化 */
	m2 := map[int] string { 5 : "55", 6 : "66"}
	fmt.Println("m2：", m2, ", len: ", len(m2))

	delete(m2, 5)
	delete(m2, 9)

	/** 排序：提取name存储到slice中，sort.Strings(names) */

}