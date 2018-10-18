#!/bin/bash
#set -x

# 命令行参数的多个变量
#	$#：参数个数
#	$*：一次性返回所有
#	$@：空格分隔的参数，用于遍历所有参数

# 解析脚本选项和参数，解析option
#	getopt： 简单方式
#	getopts：--表示额外参数

# 读取文件输入，read每次读取一行

:<<COMMENT

#######################################
# 输入参数判断
#	获得脚本当前位置
name=$(basename $0)
echo $name

#######################################
# 这里必须加引号，否则判断错误？
#	$1 始终是已经定义的变量，因此test 会成功？
if [ -n "$1" ]; then
	echo param $1
fi

if [ $# -ne 2 ]; then
	echo "param count $#, not equal 2"
fi

# 获取最后一个参数
#	${$#}, {}中不能使用$，换成！
# 测试了不起作用！！！
echo "last param:  ${!#}"

# 一次性显示所有
echo "whole param: $*"

# 遍历所有参数
for var in $@; do
	echo "loop $var"
done

# 向前移动参数，shift
echo
count=1
while [ -n "$1" ]
do
	echo "Parameter #$count = $1"
	count=$[ $count + 1 ]
	shift
done

#######################################
# 方式1：简单脚本选项
echo
while [ -n "$1" ]
do
	case "$1" in
		-a) echo "Found the -a option" ;;
		-b) echo "Found the -b option" ;;
		-c) echo "Found the -c option" ;;
		*)  echo "no such option" ;;
	esac
	shift
done

# 方式2：处理脚本参数
echo
while [ -n "$1" ]
do
	case "$1" in
		-a) echo "Found the -a option, value $2"; 
			shift;;
		-b) echo "Found the -b option, value $2"; 
			shift;;
		-c) echo "Found the -c option" ;;
		*)  echo "no such option" ;;
	esac
	shift
done

# 方式3：解析更复杂场景
#	./3_input.sh -a -b test1 -cd test2 test3 test4

# getopt会将命令行选项，设置为调整后的选项
#	ab:cd为其模式
option=$(getopt -q ab:cd "$@")
echo "resolved to: $option"

# @@ 将当前命令行的参数，重新设置
set -- $option
# 仍然用之前的方式解析
echo
while [ -n "$1" ]; do
	case "$1" in
	-a) echo "Found the -a option" ;;
	-b) param="$2"
	echo "Found the -b option, with parameter value $param"
	shift ;;
	-c) echo "Found the -c option" ;;
	--) shift
	break ;;
	*) echo "$1 is not an option";;
	esac
	shift
done
# 解析剩余的额外参数
count=1
for param in "$@"; do
	echo "Parameter #$count: $param"
	count=$[ $count + 1 ]
done

#############################################################
#############################################################
# 方式4：最佳方式
#	./3_input.sh -ab test1 -c
#	./3_input.sh -abtest1   # 甚至不加空格的方式
#	./3_input.sh -a -b test1 -d test2 test3 test4
# 注意：多次使用时，需要设置 local OPTIND
#	https://stackoverflow.com/questions/5048326/getopts-wont-call-twice-in-a-row

###### 加上这三句，会更可靠
##			特别在多层函数调用，都要进行参数解析时
local OPTIND
var=$*
set -- $var 

echo
while getopts :ab:c opt
do
	case "$opt" in
	a) echo "Found the -a option" ;;
	b) echo "Found the -b option, with value $OPTARG";;
	c) echo "Found the -c option" ;;
	*) echo "Unknown option: $opt";;
esac
done

shift $[ $OPTIND - 1 ]
echo
count=1
for param in "$@"
do
	echo "Parameter $count: $param"
	count=$[ $count + 1 ]
done


#############################################################
#############################################################


#######################################
# read 保存变量，并输出提示
read -p "input you name: " first last
echo "get $first, $last"

# 读取的数据，放入到统一变量中
#	默认名字 REPLY
read -p "enter: "
echo 
echo "get: $REPLY"

COMMENT
#######################################
# 遍历读取文件内容
# 	管道，或者 <
test_file=$(mktemp test.XXXXXX)
echo "11, aa
22, bb
33, cc" > $test_file
cat $test_file | while read line; do
	echo "get line: $line"
done

echo "========"
while read line; do
	echo "get line: $line"
done < $test_file
