#!/bin/bash
#set -x

# 自定义文件描述符的作用

:<<COMMENT
#######################################################

## 函数退出码 $?
# 1. 最后一个语句的执行结果
# 2. 通过return返回的退出码，不能退大于255
# 3. 函数输出到标准输出中，可以作为命令的返回值返回给调用者
#		把函数内部echo的输出，全部返回到一个外部变量中保存

#######################################################
# 函数获取传递给脚本的参数
#	sh 6_function.sh a b c d
func() {
	echo "func get param 1-$1, 2-$2, 3-$3"
}

value=$(func $@)
value=$(func $*)

echo "func output: " $value

#######################################################
# 变量作用域，默认是全局的; 除非使用local关键字
#	只有在函数中，可以定义local
func1() {
	value="abc"
}
func1
echo $value


#######################################################
# 数组
#	将数组传入函数
myarray=(1 2 3 4 5)
echo array data: ${myarray[*]}

func2() {
	echo func array: $@
}
func2 ${myarray[*]}

#######################################################
# 判断函数返回值
func1() {
	echo "func execute"
	return 1
}

# 方式1：因为 $? 只能获取一次就重置了, 后边输出的 $? 为 0
func1
if [[ $? != 0 ]]; then
	echo "func failed: $?"
fi

# 方式2：此时将返回值保留在其他地方，后续再输出
func1
res=$?
if [[ $res != 0 ]]; then
	echo "func failed: $res"
fi

# 方式3：边执行、边判断
#	此时func1的输出不会看到，因为``和$()一样，是将返回值交给了变量
if [[ `func1` != 0 ]]; then
	echo "execute and check: laster result $?"
fi


#######################################################
# 判断函数返回值
func1() {
	echo "func execute"
	return 1
}

# 方式1：直接执行func，会保留func中的输出
func1

# 方式2：获取func1的输出，此时func1不会直接有STDOUT输出了
result=$(func1)
echo "output func result outside: " $result

# 方式3：直接调用，并输出func1的输出
echo "just output: `func1`"



#######################################################
# 函数如果执行了退出，那么调用者也将退出
func1() {
	echo "func execute"
	exit 1
}

func1
echo "will never here"

#######################################################
# 函数指针，通过一个函数，执行其他函数
func1() {
	echo "func execute"
	return 0
}

succ() {
	#echo $@
	func=$1
	shift
	#echo "success handle: " $*
	
	# 这里使用 $@，无法将有空格的参数很好的传递
	$func "$*"

	result=$?
	if [ $result == 0 ]; then
		return 0
	else
		echo "exec [$func] failed, return $result"
		exit $result
	fi
}

succ "func1"
COMMENT

#######################################################
# 执行另一个脚本后，检查脚本的执行结果
# 新建1.sh 
# 	echo execute in 1.sh
# 	exit 1
succ() {
	# 执行传递的函数
	#echo $@
	func=$1
	shift
	#echo "success handle: " $*
	
	# 这里使用 $@，无法将有空格的参数很好的传递
	sh $func "$*"

	result=$?
	if [ $result == 0 ]; then
		return 0
	else
		echo "exec [$func] failed, return $result"
		exit $result
	fi
}

succ "1.sh"