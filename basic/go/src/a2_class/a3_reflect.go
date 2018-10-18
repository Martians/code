package a2_class

import (
	"fmt"
	"reflect"
)

func Type(a interface{}) string {
	return reflect.TypeOf(a).String()
}

/**
 * 类型分支：没有实现复杂的类型工厂，不能根据字符串构建类型出来
	B 280

 */
func TypeArgs(args ...interface{}) {
	for _, arg := range args {

		/** 该语法，只能用在switch
			否则报错：use of .(type) outside type switch */
		switch arg.(type) {
		case int:
			fmt.Println(arg, "is an int value.")
		case string:
			fmt.Println(arg, "is a string value.")
		case int64:
			fmt.Println(arg, "is an int64 value.")
		default:
			fmt.Println(arg, "is an unknown type.")
		}
	}

	/** 更常见的形式，重用变量名是很常见的 */
	// switch x := x.(type) { /* ... */ }
}

/**
 * 获得 Type、Value 两个类型
 */
func ReflectTypeValue() {
	var c int32 = 100
	types := reflect.TypeOf(c)
	value := reflect.ValueOf(c)

	fmt.Printf("Type: %s, type: %s\n", types, value.Type());

	/** 检查类型是否相同，kind实际返回的是一个 类似于enum中的index */
	fmt.Println("check type: ", types.Kind() == reflect.Int32)

	/** 重新获取对应的value */
	fmt.Println("value is: ", value.Int())
}

func ReflectSetValue() {
	var c int = 100

	/** 值复制，无法直接修改 */
	v1 := reflect.ValueOf(c)
	if v1.CanSet() {
		v1.SetInt(100)
	} else {
		fmt.Println("can't set type ", v1.Type())
	}

	/** vs是指针类型; 需要通过elem获得其value后更改，否则更改的是指针本身 */
	vs := reflect.ValueOf(&c)
	v2 := vs.Elem()
	if v2.CanSet() {
		v2.SetInt(200)
		fmt.Printf("we can set type %s, get by interface: %d, origin： %d",
			vs.Type(), v2.Interface(), c)

	} else {
		fmt.Println("can't set type ", v2.Type())
	}
	fmt.Println()
	// fmt.Printf("Type: %s, type: %s\n", types, v1.Type());
}

func ReflectStruct() {
	type T struct {
		A int
		B string
	}
	t := T{203, "mh203"}
	s := reflect.ValueOf(&t).Elem()

	fmt.Print("struct filed: ")
	for i := 0; i < s.NumField(); i++ {
		fmt.Print(s.Field(i).Type(), " ")
	}

	/** 这里获取了 struct中 filed的 name */
	fmt.Print("\nstruct filed: ")

	// 注意，这里是 获取的值类型，不是指针类型
	p := reflect.TypeOf(t)
	for i := 0; i < s.NumField(); i++ {
		f := s.Field(i)

		/** 通过 f.interface 获取的是当前 value */
		fmt.Printf("| field name: %s, type %s, interface %v",
			p.Field(i).Name, f.Type(), f.Interface())
	}
}

func Reflect() {
	fmt.Println("\n-------------------------- reflect:")

	TypeArgs(1, "bb", []int {1, 2, 3})

	ReflectTypeValue()

	ReflectSetValue()

	ReflectStruct()
}