package a0_project

/**
## 工具
	1. go doc
		godoc -analysis=type 展示每个类型的方法和具体类型和接口之间的关系
		-analysis=type 和 -analysis=pointer，用于打开文档和代码中关于静态分析的结果

		http://wiki.jikexueyuan.com/project/go-command-tutorial/0.5.html
		go doc fmt Printf
		godoc  fmt Printf
		godoc -http :8000    # -index
		godoc -ex net/http FileServer

	2. goimports
		用于ide工具，vim、sublime等
		https://godoc.org/golang.org/x/tools/cmd/goimports
		go get golang.org/x/tools/cmd/goimports


	3. gofmt
		http://wiki.jikexueyuan.com/project/the-way-to-go/03.5.html
		https://blog.csdn.net/u012210379/article/details/50443654

		go fmt *.go
		gofmt -l -w *.go

	4. go list
		go list -json hash
		go list -f '{{join .Deps " "}}' strconv

		go list ...
		go list gopl.io/ch3/...		特定子目录下的所有包
		go list ...xml...			某个主题相关的所有包
 */