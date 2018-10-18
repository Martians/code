package main

/**
 *
## 说明
	1. 命令
		dep help ensure
		dep status
		dep prune

	2. 工作
		Gopkg.lock 记录当时使用的版本，通常不需要将版本更新到最新，使用一个有效的版本集合
		本地缓存：在 pkg/dep/sources 下，会放置依赖包完整的工程

	3. 资料
		https://studygolang.com/articles/10589
		https://www.jianshu.com/p/e3c9f9039542

			vendor协议：https://github.com/golang/proposal/blob/master/design/25719-go15vendor.md

## 执行
	0. 清理
		删除 Gopkg.lock、Gopkg.toml

	1. dep init -v -gopath
		vendor目录下，会放置必要的依赖，可能只有少量文件
		-gopath 会首先在本地路径中搜索


	2. 添加
		dep ensure -v -add github.com/bitly/go-simplejson
		toml、lock文件都会同步更新；此处可以指定库的版本

	3. 匹配
		dep ensure -v

	4. 更新
		dep ensure -v -update

	5. 查看
		dep status
 */
import (
	"fmt"
	"github.com/apodemakeles/ugo/time"
)

func main() {
	fmt.Println(utime.NowUnixTS())
}