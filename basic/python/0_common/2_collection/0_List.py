#coding=utf-8

""" 内部实现上，列表就是 C 数组，而不是链表
    1. 有哪些全局函数存在？
    2. list 也实现了一些队列相关的函数
"""

###########################################################################3
''' 构造列表
'''

''' 初始化列表
'''
bicycles = ['trek', 'cannondale', 'redline', 'specialized']

# list构造函数，解析的是seq
numbers = list(range(1, 10, 2))
print(numbers)

###############################################################################################
''' 日常操作
'''
bicycles = ['trek', 'cannondale', 'redline', 'specialized']
save = bicycles[:]      # 复制列表，利用切片
print(bicycles)

bicycles.append("honda")
print(bicycles)
print(bicycles[0] + ", last: " + bicycles[-1])

bicycles.insert(1, "new bike")
print(bicycles)

bicycles.extend([3, 4, 5])
print(bicycles)

print(bicycles.index(3))

''' 删除
'''
del bicycles[0]
print(bicycles)

del bicycles[3:]

print(bicycles.pop())
print(bicycles)

bicycles.pop(1)
print(bicycles)

try:
    bicycles.remove("redline")
    print(bicycles)
except Exception as err:
    print("remove but not exist, " + str(err))

print(len(bicycles))


''' 循环
'''
for a in bicycles:
    print("it is my: ")
    print(a)
print("last is " + str(a))

squares = []
squares.append("11 str")
squares.append(22)
print(squares)

''' 列表之间
'''
numbers = list(range(1, 10, 2))
squares.append(numbers)
print(squares)


L1 = [1, 2, 3]
L2 = [4, 5, 6]
print(L1 + L2)
print(L1 * 3)

###############################################################################################
''' 排序、翻转
'''
bicycles = save
bicycles.sort(reverse=True)

''' 临时排序，不会改变list，返回一个新的list
    key 设置了一个函数，返回在排序中使用的值
'''
print(sorted(bicycles, key=str.lower, reverse=True))

''' reversed 返回的是iterator
'''
s = reversed(bicycles)
print(list(s))

bicycles.reverse()
print(bicycles)

#bicycles.sort()

###############################################################################################
''' 复制、切片
'''

''' 切片
'''
players = ['charles', 'martina', 'michael', 'florence', 'eli', 'sss']
print(players[0:3])
print(players[:4])
print(players[2:])
print(players[-3:])

# 指定步进
#   分片也相当于使用slice对象进行索引
print(players[1::2])
print(players[slice(1, None, 2)])

# 负数步进，相当于反转
#   list.reverse是修改list
#   这里是返回一个新的list
print(players[::-1])

''' 利用切片，复制列表
'''
new_one = players[:]



###########################################################################3
''' 列表解析
        生成一个新的列表
'''
squares = [value ** 2 for value in range(1, 11)]
print(squares)

'''分片赋值
        相当于：删除左边的分片，插入右边到删除的位置
'''
L = [1, 2, 3, 4, 5]

L[2:4] = [7, 8, 9, 10]
print(L)