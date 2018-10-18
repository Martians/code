#!/bin/bash
#set -x

# 输出重定向
# 	如果源，直接使用数字即可  1> file
# 	如果是目的，需要使用&     1> &2， 否则会写入到名字为2的文件中

# 自定义文件描述符的作用
# 	通过新的描述符，只改变部分输入输出（手动指定，某些输入输出到这个新的描述符）
#	保存已有描述符，将输入输出重定向，执行完操作后，再切换回来（通常是保存标准输入输出）

#《Linux命令行与shell脚本编程大全.第3版》330 Page 
#	定向错误输出的方法：1）重定向STDERR 2）重定向与STDERR关联的文件描述符（2）

# 语句解释
#	date | tee -a testfile 
#	cat >> $outfile << EOF

:<<COMMENT


#######################################
# 输入重定向，用指定文件的描述符，来替换STDIN文件描述符

# 方式1：输出重定向，默认的 > 实际上是 1>, 将描述符1 STDOUT重定向
#	分别重定向到不同位置
ls -al /home test test2 test3 badtest 2> file1 1> file2
more file1 file2

# 方式2：重定向到一起；&>是个特殊符号
#	错误消息会先输出
ls -al /home test test2 test3 badtest &> file1
more file1
rm file1 file2 -f

#######################################
# 临时重定向
#	一共重定向两次，因此可以选择
#		内部 1 > &2
#		外部 2 > file
#	用途：记录、收集脚本生发生的错误信息
#	./4_output.sh 2> errorFile  # 该语句在脚本外执行，将错误输出，输出到errorFile
echo "test error message " >&2  # 该语句在脚本内执行，将标准输出，重定向到错误输出；这样就与外部的再次重定向结合起来了
cat errorFile
rm errorFile -rf

#######################################
# 永久重定向
#	相当于直接在脚本内部，定义了重定向内容，不需要在脚本执行时指定
#	exec 启动一个shell
exec 1> testOutput
exec 2> testError
echo "this is some message"
echo "this is error message" > &2
# cat errorFile
# rm errorFile -rf

#######################################
# 脚本直接从文件中读取数据
exec 0< testfile
count=1
while read line
do
	echo "Line #$count: $line"
	count=$[ $count + 1 ]
done

#######################################
# 新建重定向描述符
# 重定向后再进行恢复

# 查看描述符分配状况
#	lsof -a -p $$ -d0,1,2,3,6,7

exec 3> test18file1
exec 6> test18file2
exec 7< testfile

#######################################
COMMENT

# 阻止输出 /dev/null
#	清除文件内容，cat /dev/null > testfile
#######################################

# 使用临时文件
#	tempfile=$(mktemp test19.XXXXXX)
#	mktemp -t test.XXXXXX # 强制在tmp目录生成文件
#	tempdir=$(mktemp -d dir.XXXXXX) # 创建临时目录

#######################################
# tee 管道的一个T型接头，将STDIN过来的数据
#		一部分发送到 STDOUT
#		一部分发送到指定的文件
#	who  | tee test18file2 	
#	date | tee -a testfile # 追加内容到文件

#######################################
# 328 P
# 	输出追加重定向，cat的输入追加到文件中
# 	输入追加重定向，cat的输入不再取自STDIN，而是从后续STDIN读取
#	EOF标记了追加到文件中的，数据的起止
#	cat >> $outfile << EOF
