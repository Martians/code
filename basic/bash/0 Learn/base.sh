#!/bin/bash
#set -x

:<<COMMENT


# 命令退出码是0的条件，才会执行
#	if 只能用于 命令退出条件的判断			
if pwd; then
	echo "if pwd " $(pwd) or `pwd`
elif ls -d /home ; then
		echo "home"
fi


# test监测 变量存在
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
COMMENT

# 替代 test 进行比较, 但是[]内家空格
value=1

if [ $value ]; then
	echo "good"
fi
