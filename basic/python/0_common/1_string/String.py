#coding=utf-8

''' 字符串本身就是一个序列
    1. 没有单个字符这种类型，只有一个字符的字符串
    2. python3中 u'abc' 和 'abc'是一样的？
'''


#####################################################################
''' 基本操作
    replace、
'''
message = "    One of Python's strengths is its diverse community.  "
print(message.strip())
print("this is a line")

print(message.title())

message = message.strip()
print(message.title())

# 使用 str来使得其他按类型 字符串化
print('happy ' + str(12) + "rd now\n")


car = "audi"
if car != "AUDI":
    print("not equal")

''' 复制字符串
'''
news = car[:]
print(news)

''' 修改字符串
    1. 分片后处理
    2. 转换为list，然后使用string的join
'''
print("=================================")
s = 'great is good'
s = s[:-4] + ", but " + s[2:]
print(s)

ls = list(s)
print(ls)
ls[5] = 'XPPP'
s = ''.join(ls)
print(s)

#####################################################################
''' 格式化表达式
'''
value = '%s, egges, and %s' % ('span', "spam!")
print(value)

''' 格式化方法
'''
value = '{0}, and {1}'.format('span', "spam!")
print(value)

#####################################################################
