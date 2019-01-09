package a4_io

import (
	"io"
	"os"
)

/**
 * Todo：文件遍历读取《go bible》30、32
 ioutil.ReadFile(filename)
bufio.NewScanner(os.Stdin)

 */

func CopyFile(dst, src string) (w int64, err error) {
	srcFile, err := os.Open(src)
	if err != nil {
		return
	}
	defer srcFile.Close()
	dstFile, err := os.Create(dst)
	if err != nil {
		return
	}
	defer dstFile.Close()
	return io.Copy(dstFile, srcFile)
}

func FileIO() {
	CopyFile("File.go", "/tmp/c.go")
}