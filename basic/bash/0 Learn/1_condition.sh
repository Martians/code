#!/bin/bash
#set -x

# if只能判断命令返回值，0 为成功
	# 使用test，可以判断表达式
	# []：相当于输入了test命令
	#     数值比较：-eq、-ne、-lt、-le、-gt、-ge
	#     字符比较：= \> \<  -n、-z
	#     文件比较：-d -e -f -r -w file1 -nt file2
	#	  多条件比较，分别使用[]，外部使用 &&
	# (())：数学运算高级用法，无需转义
	# [[]]：字符运算高级用法，无需转义：==，正则
# case
#	也能使用正则

:<<COMMENT

#######################################
# 仅仅判断命令退出码
# 	命令退出码是0的条件，才会执行
#	if 只能用于 命令退出条件的判断			
if pwd; then
	echo "if pwd " $(pwd) or `pwd`
elif ls -d /home ; then
		echo "home"
fi


# 判断命令执行结果，但是不显示命令本身的输出
if pwd > /dev/null; then
	echo "if pwd " $(pwd) or `pwd`
elif ls -d /home ; then
	echo "home"
fi

# 或者在命令中，指定不输出，只是判断返回值
#	grep -q
if echo 111 | grep -q "1"; then
	echo "exist"
fi

#######################################
# 使用test 检查条件; 
###################################################
# 	条件为true，test将其转换为 0 返回
value=1
if test $value; then
	echo "good"
fi

value=
if test $value; then
	echo "never here"
else		
	echo "not good"
fi

value=""
if test $value; then
	echo "never here"
else		
	echo "not good"
fi

#######################################
# [] 替代 test 进行比较, 但是[]内加空格
#	这里判断的是 true、false
value=1
if [ $value ]; then
	echo "good"
fi

####################################
# 数值比较
#		可以使用的比较符号

value=1
if [ $value -gt 0 ]; then
	echo "good"
fi

# 字符串比较
#	注意，这里需要进行 > 的转义，否则代表的是重定向
if [ 2 \> 1 ]; then
	echo "2 > 1, 字符串比较"
fi

# 字符串比较
#	注意，这里需要进行 > 的转义，否则代表的是重定向
if [ "2" \> "1" ]; then
	echo "2 > 1, 字符串比较"
fi

# 必须用此方式，才能进行数值比较
if [ 2 -gt 1 ]; then
	echo "2 -gt 1, 数值的比较"
fi

####################################
# 字符串
if [ aaa = aaa ]; then
	echo "good"
fi

# 非空字符串
if [ -n "aaa" ]; then
	echo "not empty"
fi

# 空字符串
#	方式1
if [ -z  ]; then
	echo "is empty"
fi

#	方式2
if [ !$sss ]; then
	echo "empty"
fi

####################################
# 文件比较
# 	非空、可读、可写、是否是文件、文件新旧比较
if [ -d /home ]; then
	echo "dir exist and is dir"
fi

if [ -f /bin/sh ]; then
	echo "file exist and is file"
fi

if [ -e /bin/sh ]; then
	echo "file or dir exist"
fi


####################################
# 多条件比较
var=100
if [ $var -gt 10 ] && [ -e /home ]; then
	echo "match condition"
fi

####################################
# 高级条件比较，更通用

# 数学运算
#	适合所有的数学表达式，不需要进行转义
#	--a, 取反，与、或
if (( 1 * 9 < 2 *10 )); then
	echo "1 < 2, 数学比较"
fi

((var= 100 * 20))
echo $var

# 失败的写法
# var=((100 * 20))

# 字符串比较
#	适合所有的字符串，不需要进行转义
#	内部使用test来进行比较
#	支持模式匹配，正则表达式
if [[ "abc" == a* ]]; then
	echo "string match"
fi

if [[ "abc" = a* ]]; then
	echo "string match"
fi

####################################
# @@@@ 字符串包含 
#	方式1：运算符
if [[ "abcde" =~ "bc" ]]; then
	echo "include"
fi

#	方式2：grep
if echo "abcde" | grep -q "bc"; then
	echo "include"
fi

#	方式3：正则
if [[ "abcde" = *"bc"* ]]; then
	echo "include"
fi
COMMENT

####################################
# case
var=abc
case $var in
ab* | bcd) 
	echo "abc | bcd";;
efg) 
	echo "efg";;
*)   
	echo "default";;
esac

