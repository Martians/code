#coding=utf-8

''' 字符串本身就是一个序列
'''

print("this is a line")
print "this is a line"


#####################################################################
''' 基本操作
'''
message = "    One of Python's strengths is its diverse community.  "
print message.strip()
print(message.title())

message = message.strip()
print(message.title())

# 使用 str来使得其他按类型 字符串化
print 'happy ' + str(12) + "rd now\n"


car = "audi"
if car != "AUDI":
    print "not equal"

''' 复制字符串
'''
news = car[:]
print news

#####################################################################
''' 格式化字符串
'''
value = '%s, egges, and %s' % ('span', "spam!")
print value

value = '{0}, and {1}'.format('span', "spam!")
print value

#####################################################################
''' 原始字符串
'''
raw = r'1122 \n\t'
print raw

''' multi line
    多行字符串
'''