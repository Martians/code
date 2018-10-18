package a1_common

import "fmt"

/**
 * 66
	69
空间分配
没有指针运算
除非使用。。。

 */


/**
* 变量是可寻址的值
对变量取地址，或者复制指针，只是为变量创建了新的别名
*/
func Reference() {
	x := 1
	p := &x
	*p = 1

	/** 返回局部变量的指针是安全的，因为有指正仍然引用这个
		但如果调用多次函数，返回的地址是不同的 */
	q := func() *int{
		v := 1
		return &v
	}()

	fmt.Println(p, q)
}