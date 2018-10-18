#!/bin/bash
#set -x

# for 遍历各种类型，包括目录下的文件
#	修改分隔符
#	遍历多个文件、目录
# 	C 语言风格遍历
# 	输入输出重定向

:<<COMMENT

#######################################
# for 多种获取数据源的方式

# 1）手工列出
for test in albaba alsta arkansas; do
	echo -n $test " "
done

# 2）包含单引号、空格的时候, albaba'、alsta'、alsta b
for test in albaba\' "alsta'" "alsta b" arkansas; do
	echo $test " "
done

# 3）通过变量，解析变量中的多个部分
list="Alabama Alaska Arizona Arkansas Colorado"
	# 	给变量追加内容，!!!! 只有这一种写法
list=$list" Connecticut"  
for test in $list; do
	echo $test
done

# 4）从命令中返回的内容，构成list
for test in $(ls /home); do
	echo $test
done

for test in `ls /home`; do
	echo $test
done

#######################################
# 更改字段分隔符
# 	内部字段分隔符（internal field separator），是环境变量
#	改完需要复原
IFS.OLD=$IFS
IFS=$'\n'
IFS=$IFS.OLD

# 读取文件内容
for state in $(cat ~/.bashrc)
do
	echo "Visit beautiful $state"
done

# for 命令自动遍历目录
#	强制shell进行文件扩展匹配
for file in /bin/* /home/* /notexist
do
	# 必须用双引号，$file中可能包含空格
	if [ -d "$file" ]; then
		echo $file is dir
	fi
done

#######################################
# C 语言风格
#	赋值语句可以有空格
#	括号两边不需要留空格
for ((a = 1; a < 10; a++));do
	echo $a
done

for ((a = 1, b=2; a < 10; a++, b--));do
	echo $a "-" $b
done

#######################################
# while
var=5
while echo -n loop " " && [ $var -gt 0 ]; do
	echo $var
	# 注意变量，用这种方式递减
	#	或者：(( var = $var - 1 ))
	var=$[$var - 1]
done

var=5
while echo -n loop $var " " 
		[ $var -gt 0 ]; do
	echo $var
	var=$[$var - 1]
done

#######################################
# until, 条件不成立时就一直执行
# ！！！！！！！！！！！！！！！！
var=5
until ((--var <= 0)); do
	echo $var
done

# 跳出两层循环
for (( a = 1; a < 4; a++ ))
do
	echo "Outer loop: $a"
	for (( b = 1; b < 100; b++ ))
	do
		if [ $b -gt 4 ]
		then
			break 2
		fi
		echo " Inner loop: $b"
	done
done

#######################################
# 循环整体数据，统一重定向
#	将循环的输出，统一交给 sort
for file in /home/*
do
	if [ -d "$file" ]
	then
		echo "$file is a directory"
	else
		echo "$file is a file"
	fi
done | sort
COMMENT

# 循环从文件中读取指定格式数据
#	指定了分隔符
tempfile=$(mktemp test.XXXXXX)
echo "use $tempfile"
echo "11, aa
22, bb
33, cc" > $tempfile

while IFS=, read -r id name
do
	echo "$id-$name"
done < $tempfile
