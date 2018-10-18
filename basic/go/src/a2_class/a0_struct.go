package a2_class

import "fmt"

/**
 * 	1. go 中没有隐藏的 this 指针，在定义成员函数时
		a. 是显示传递的，需要在定义函数时指出
		b. 可以不一定是指针；也不一定叫this

	2. 值和引用
		值语义的：
			基本类型，复合类型（array、struct、pointer）
			使用时会复制他们，传递时恢复制，这本身就是常规的需求

		看起来像是引用语义：
			slice：包含一个管理array的数据结构；但slice本身也是值语义的
			map、channel：引用语义，通常没有直接的复制需求
			interface：实际上包含两个指针

	3. 面向对象
		只保留了组合特性，简单但强大：可以随时创建出新类型
		没有构造函数，通常用一个全局的创建函数

	4. 可比较性
		所有成员可比较，那么结构体也是可比较的
 */

/**
 * 4 种构造struct的方法
 */
func Construct() {
	type Rect struct {
		x, y float64
		width, height float64
	}

	/** 这里四种创建方式，返回的都是引用（指针） */

	/** new：创建一个T类型的匿名变量，初始化为T的零值，然后返回变量地址
		与普通变量声明语句方式创建的变量，没有什么区别，只是一个语法糖
		new只是一个预定义函数，不是关键字；p := new(int) */
	rect1 := new(Rect)
	/** 创建一个空值 */
	rect2 := &Rect{}
	/** 两种初始化方式 */
	rect3 := &Rect{0, 0, 100, 200}
	rect4 := &Rect{width: 100, height: 200}

	fmt.Println(Type(rect1), Type(rect2), Type(rect3), Type(rect4))

	/** 下边两种方式等价 */
	type Point struct{ X, Y int }
	// 1）
	pp := new(Point)
	*pp = Point{1, 2}
	// 2）
	pp = &Point{1, 2}


	/** 点操作符，可以和变量、指针同样方式工作
		p.X 等价于 (*p).v  */
	v := Point{1, 2}
	p := &v
	fmt.Println("point operate: ", v.X, p.X)
}

/** --------------------------------------------------------------- */
		/**
		 * 注意，对Base::Foo的定义，必须放在函数之外定义，否则无法成功
		 */
		type Base struct {
			Work string
		}

		func (base* Base) Foo() {
			fmt.Println("Base: foo")
		}

		type Data struct {
			name string
			Base
		}

/**
 * 	匿名组合后，子类可以直接调用父类的函数，就像是“继承”一样
	1. 只需要在struct成员中，放一个组合类的类型（或者指针，但需要传入一个进来）即可
	2. 用简单的方式，实现了虚基类的用法

## 实现
	内嵌字段会指导编译器去生成额外的包装方法来委托已经声明好的方法

## 用途
	1. B 219：还可以生成临时结构体，将几个包级别变量整合在一个struct中，语义上更方便理解

	考虑问题
		组合时，是否使用指针

 */
func Combine() {
	data := &Data{}
	/** 直接使用了 “父类” 中的方法 */
	data.Foo()

	/** 方法值：f 是一个绑定了对象的方法，可以类似于匿名函数一样传递 */
	f1 := data.Foo
	f1()

	/** 方法表达式：类似于方法值，但没有绑定对象，使用时传入对象接收器作为第一个参数 */
	f2 := (*Data).Foo
	f2(data)
}

/**
 * 	方法的接收器，即struct的变量、指针

	B 212,123：
	1. 接收器是struct 指针
		当接收者变量本身比较大时，通常使用指针来声明方法。B 212: 类型本身是指针，不允许出现在接收器中，为了避免歧义
		可以直接所用变量调用该方法，编译器会隐式的转换为指针去调用
	2.  接收器是struct
		可以使用指针调用该方法，编译器会自动解引用

 */
func Method() {

}

func Struct() {
	fmt.Println("\n-------------------------- struct:")

	Construct()

	Combine()
}