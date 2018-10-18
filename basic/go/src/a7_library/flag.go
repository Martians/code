package a7_library

import (
	"flag"
	"fmt"
	"time"
)
var infile *string = flag.String("i", "infile", "File contains values for sorting")
var outfile *string = flag.String("o", "outfile", "File to receive sorted values")
var algorithm *string = flag.String("a", "qsort", "Sort algorithm")


/** B 240, 时间周期标记值非常的有用，所以这个特性被构建到了flag包中
自定义flag标志 */
var period = flag.Duration("period", 1*time.Second, "sleep period")
/**
 * 注意，这里定义的都是指针
 */
func main() {
	flag.Parse()
	if infile != nil {
		fmt.Println("infile =", *infile, "outfile =", *outfile, "algorithm =",
			*algorithm)
	}
}