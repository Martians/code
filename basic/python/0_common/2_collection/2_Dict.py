# coding=utf-8

'''
    1. 也叫作关联数组、散列表，以散列表的方式实现；变长、异构的
    2. 任何不可变的对象，都可以作为 key
    3. 取消了has_key，使用 in
'''

###############################################################################################
''' 初始化
'''
# 所有的key有相同的value
d = dict.fromkeys(['a', 'b'], 'qq')
print(d)

''' 使用元组的方式添加
'''
d = dict([('name', 22), ('age', 40)])
print(d)

''' 使用zip来添加
'''
d = dict(zip(['1', '2', '3'], ['1', '2', '3']))
print(d)

''' 这里key默认必须是字符串
'''
d = dict(name='11', qq=43)
print(d)



###############################################################################################
''' 日常访问
'''
alien_0 = {'color': 'green', 'points': 5}
print(alien_0['color'])
print(alien_0)

alien_0['color'] = 'green1'
print(alien_0)

print('color' in alien_0)
print(alien_0)

''' 访问key
'''
# 不存在则返回默认值
value = alien_0.get('value', 100)
print('value exist or default: ', value)

try:
    print(alien_0['no_value'])
except KeyError:
    print("not exist key")

if 'newkey' in alien_0:
    print("exist")

''' 删除
'''
del alien_0["color"]
print(alien_0)

alien_0.pop('points')
print(alien_0)

user_0 = {
    'username': 'efermi',
    'first': 'enrico',
    'last': 'fermi',
}

''' 遍历key、value，返回的是视图对象
    .keys() 返回的是一个迭代器
'''
print(alien_0.keys())
print(list(alien_0.keys()))

print(alien_0.values())
print(alien_0.items())

''' 下边两个操作，效果一样
    1. 字典有自己的迭代器
    2. 循环结构自动迫使可迭代对象-keys，在每次迭代上产生一个结果
'''
for key in user_0:
    print(key)

for key in user_0.keys():
    print(key)

for key, value in user_0.items():
    print(key + "->" + value)

''' 按顺序输出dict的方式
'''
for key, value in sorted(user_0.items()):
    print(key + "->" + value)

# 遍历value，并确保不会重复
for value in set(user_0.values()):
    print(value)

###############################################################################################
''' dict之间
'''
import copy
another = copy.copy(user_0)
print(another)
print(user_0.copy())

alien_1 = {'color': 'green', 'points': 5}
alien_1.update(user_0)
print(alien_1)

''' 字典解析
'''
print({x: x* 2 for x in range(10)})

#################################################################################################
''' set
    1. 不可变、无序Collection
    2. 相比于dict，集合可以进行多种操作
    3. 只支持不可变、可散列的类型; list、dict不行，元组可以；元组是比价其完整值
'''

sets = {x**2 for x in [1, 2, 3, 4]}
print(sets)


''' 初始化
'''
# 该构造函数，将传入的内容，当做是序列
a = set('abcde')
b = set('befgh')
c = {1, 2, 3, 4}

''' 集合操作
'''
print(a - b)
print(a | b)
print(a & b)
print(a < b)

# 添加内容，这里将添加的内容当做一个元素
a.add('SPAM')
print(a)

a.update(b)
print(a)

a.remove('g')
print(a)

''' 嵌套类型
    只能存放元组
'''
a.add((1, 2, 3))
print(a)

''' 元组作用：过滤重复项
    set是可迭代的，因此可以传入list()
'''
ls = [1, 2, 3, 4, 5, 4, 3, 2, 1]
print(list(set(ls)))

