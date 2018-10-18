package a7_library

import (
	"encoding/json"
	"fmt"
	"log"
)

type Movie struct {
	Title string
	Year int `json:"released"`
	Color bool `json:"color,omitempty"`		/** 为空或零值时不生成该JSON对象 */
	Actors []string
}

// 编组（marshaling）
// 只有导出的结构体成员才会被编码
// 结构体成员Tag是和在编译阶段关联到该成员的元信息字符串
// 值中含有双引号字符，因此成员Tag一般用原生字符串面值的形式书写
func JsonHandle() {
	var movies = []Movie{
		{Title: "Casablanca", Year: 1942, Color: false,
			Actors: []string{"Humphrey Bogart", "Ingrid Bergman"}},
		{Title: "Bullitt", Year: 1968, Color: true,
			Actors: []string{"Steve McQueen", "Jacqueline Bisset"}},
	}

	/** 编码：每一行输出的前缀和每一个层级的缩进 */
	// data, err := json.Marshal(movies)
	data, err := json.MarshalIndent(movies, "", " ")
	if err != nil {
		log.Fatalf("JSON marshaling failed: %s", err)
	}
	fmt.Printf("%s\n", data)

	/** 解码：新生成一个struct，只解析title部分 */
	var titles []struct{ Title string }
	if err := json.Unmarshal(data, &titles); err != nil {
		log.Fatalf("JSON unmarshaling failed: %s", err)
	}
	fmt.Println(titles) // "[{Casablanca} {Cool Hand Luke} {Bullitt}]"

	/** 流式解码：json.NewDecoder */
}
