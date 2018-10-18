package a3_collection

import (
	"bytes"
	"fmt"
	"strconv"
)

/**
 * rune 是 UTF-32或 UCS-4，等价于 int32 类型
 */
func RuneString() {
	/** B 103
		也可以用 utf8.DecodeRuneInString(s[i:]) 每次解析一个字符
		range循环可以自动隐式解码，比较简单
	*/
	s := "Hello, 世界"
	for i, r := range s {
		fmt.Printf("%d\t%q\t%d\n", i, r, r)
	}

	/** 程序内部采用rune序列可能更方便， 因为rune大小一致，支持数组索引和方便切割
		直接print rune 不行，需要转换为 string 才能看到有效值 */
	r := []rune(s)
	fmt.Println("rune: ", r, ", ---", string(r))

	r2 := []rune("Hello, 世界")
	fmt.Println("rune 2: ", r, ", ---", string(r2))
}

func ByteString() {
	s := "abc"

	/** string也字节数组的转换
		这里将分配一个新的字节数组，保存字符串的副本 */
	b := []byte(s)
	s2 := string(b)
	fmt.Println(s2)

	/** 动态构造string时，使用buf，然后再转换为string */
	var buf bytes.Buffer
	buf.WriteByte('[')
	buf.WriteByte(']')

	fmt.Println("ByteString：", buf, " - string: ", buf.String())
}

/**
 * 	1）不变性：意味着赋值任何长度的字符串，代价都是低廉的
		字符串及其切片，可以安全的共享相同的内存
	2）可以包含任意的数据，包括 0值
 */

/**
 	读取 byte类型 或者 unicode字符
 */
func StringChar() {
	str1, str2 := "hello", "world"
	print(str1, str2)

	fmt.Print("str byte: ")
	str := str1 + str2
	for i := 0; i < len(str); i++ {
		/** byte 类型 */
		fmt.Print(str[1], " ")
	}

	fmt.Print("\nstr unicode: ")
	for i, ch := range str {
		/** rune 类型 */
		fmt.Print(i, ch)
	}

	/** 原生字符串，无需转义 */
	const bb = `的是否撒
		地方
		1111`
	fmt.Println("origin: ", bb)
}

func StringConv() {

	/** 转换为字符串 */
	// 1）
	x := 123
	y := fmt.Sprintf("%d", x)

	// 2）
	fmt.Println(y, strconv.Itoa(x)) // "123 123"
	fmt.Println(y, strconv.FormatInt(int64(x), 2)) // "1111011"

	/** 转换为数字 */
	// 1）
	// fmt.Scanf

	// 2）
	x, err := strconv.Atoi("123") // x is an int
	z, err := strconv.ParseInt("123", 10, 64) // base 10, up to 64 bits
	fmt.Println(err, z)
}

func String() {
	RuneString()

	ByteString()

	StringChar()

	StringConv()
}