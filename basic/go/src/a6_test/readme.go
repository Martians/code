package a6_test

/**
## 特性
	B 408：测试时，替换是用到的各个程序，mock
	外部测试包：两个包邮上下级关系，但测试时有交叉引用
		外部测试宝的白盒测试：有时候需要访问测试包内部代码，开个后门
		export_test.go：var IsSpace = isSpace

## 执行
 	cd src/a6_test

	1. 测试当前目录和所有子目录：
		go test ./...

	2. 只测试某一个包：1）这里必须写包的全名；2）或者进入到pack_1 文件夹，执行 go test
		go test a6_test/pack_1

	3. 并发
		go test -p 3 ./...

	4. 简短，需要在测试代码中，检测这个标志
		go test -short ./...

	5. 过滤
		go test  ./... -run=TestAdd
		go test  ./... -run TestAdd

	6. 性能
		go test -test.bench=".*" ./...
		go test -test.bench=".*" ./... -count=5
		go test -run=NONE -bench=ClientServerParallelTLS64  ## 禁止其他测试，只跑性能测试

	7. 更多
		覆盖率：go test -coverprofile=c.out && go tool cover -html=c.out

## 更多
	1. 覆盖率
		在测试代码中插入生成钩子来统计覆盖率数据
		go test -run=test1 -coverprofile=c.out
		-covermode=count：插入一个计数器而不是布尔标志量

	2. profile， B 421
		go test -cpuprofile=cpu.out
		go test -blockprofile=block.out
		go test -memprofile=mem.out
 */