#coding=utf-8

'''
    1. 有哪些全局函数存在？
'''

###############################################################################################
''' 日常操作
'''
bicycles = ['trek', 'cannondale', 'redline', 'specialized']
save = bicycles[:]      # 复制列表，利用切片
print(bicycles)

bicycles.append("honda")
print(bicycles)
print bicycles[0] + ", last: " + bicycles[-1]

bicycles.insert(1, "new bike")
print bicycles

del bicycles[0]
print bicycles

print bicycles.pop()
print bicycles

bicycles.pop(1)
print bicycles

bicycles.remove("redline")
print bicycles

''' 排序、翻转
'''
bicycles = save
bicycles.sort(reverse=True)
# 临时排序
print sorted(bicycles)

bicycles.reverse()
print bicycles

print len(bicycles)

''' 循环
'''
for a in bicycles:
    print "it is my: "
    print(a)
print "last is " + a

###############################################################################################
''' 复制、切片
'''
# 必须要先创建对象？
squares = []
squares.append("11 str")
squares.append(22)
print squares

# 如何将另一个列表加入进来 ？？
numbers = list(range(1, 10, 2))
squares.append(numbers)
print squares
print min(squares)

''' 切片
'''
players = ['charles', 'martina', 'michael', 'florence', 'eli']
print(players[0:3])
print(players[:4])
print(players[2:])
print(players[-3:])

''' 利用切片，复制列表
'''
new_one = players[:]

###########################################################################3
''' 构造列表
'''
for value in range(0, 5):
    print value

''' 初始化列表
    这里的list是什么函数？？？？
'''
numbers = list(range(1, 10, 2))
print numbers

###########################################################################3
''' 列表解析
'''
squares = [value ** 2 for value in range(1, 11)]
print squares

# 3.0 中可用
# squares = (value ** 2 for value in range(1, 11))
# print squares