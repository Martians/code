#!/bin/bash
set -x

# 可以不使用这些方法
# 	expr 数学计算，是原始方式，需要转义
# 	$[] 提供了一种数学表达方式

# 原始方式，适应性不好
expr 1 \* 5
#	可以当做是个函数？
v=$(expr 1 + 5)

a=1
b=2
c=$(expr $a + $b)

# 字符串相关
expr length abcde

# 模式匹配
expr abded : *ed

# 无效
# $(expr ( 1 + 5 ))

# 常用方式
var1=$[1 + 5]
var2=$[$var1 + 5]