package a1_common

import "fmt"

func Condition() {
	/**
	 * if 之后可以放至多一个，用于进行赋值，但必须用短变量声明方式（:= 否则要使用var关键字了）
			可以利用语法上的便利，给多个变量赋值
	 */
	x := 10
	if c, d := 10, 100; x > 0 {
		fmt.Println(c, d)
	}

	/**
	 * 词法块，Bible 79
	 * 连续多个 if else，前边 if 词法域中创建的变量，后边的if 因为是嵌套在前一个if内部的，因此是可以直接访问变量的
 	 */
}

/**
 * P39, 说这里会编译失败？
 */
func Example(x int) int {
	if x == 0 {
		return 5
	} else {
		return x
	}
}

/**
 * 	case 一行可以写多个值
	不需要break
 */
func Switch() {
	i := 2
	switch i {
		case 0:
			fmt.Println("0")
		case 2:
			/** 除非专门这样指定，才会紧接着执行下一个case */
			fmt.Println("2, fallthrough")
			fallthrough
		case 4, 5, 6:
			fmt.Println("4, 5, 6")
		default:
			fmt.Println("Default")
	}

	/** 不带操作对象时，默认用true代替；可以当做 if else使用 */
	switch {
		case i > 1 && i <10: fmt.Println("1-10")
		case i > 100: fmt.Println(">100")
	}

	/** 直接使用 字符串 作为分支选择条件 */
	str := "abc"
	switch str {
		case "abc": fmt.Println("switch match abc")
	}
}

func Loop() {
	/**
	    没有while，使用for代替
		不支持：if i++ > 10
	*/
	i := 0
	for {
		i++
		if i > 10 {
			break
		}
	}

	/** 多重赋值：但是不允许；间隔的赋值语句
			必须使用平行赋值方式，初始化多个变量
			同样，最后一部分不能写成 i++, j++，必须是短赋值 */
	for i, j := 1, 5; i < j; i, j = i + 1, j - 1 {

	}

	/** 允许break到制定标签，快速退出循环
		当前版本不支持这个? */
	// for j := 0; j < 5; j++ {
	// 	for i := 0; i < 10; i++ {
	// 		if i > 5 {
	// 			break JLoop
	// 		}
	// 		fmt.Println(i)
	// 	}
	// }
	// JLoop:
	// 	fmt.Println("jump loop")
}

func Control() {
	fmt.Println("\n-------------------------- control flow:")

	Example(1)

	Switch()

	Loop()
}