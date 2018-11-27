# coding=utf-8


d = {"a": 1, 'c': 3, 'b': 2}

# if else 表达式
#   与scala不同，语句不是对象；因此先把一个值 放在前边
value = d['f'] if 'f' in d else 0


#########################################################################################################
''' 条件语句
'''
if not 'f' in d:
    print('f not exist')

if 'f' not in d:
    print('f not exist')

#########################################################################################################
# 实体测试
#    == 测试是否有相同的值
#    is 测试内存地址，严格意义的相等
#    x、z都指向对象1（对象1被缓存和复用了）
x = 1
y = x
z = 1
print(str(x is y) + ", " + str(x is z))

import sys
print("ref count:" + str(sys.getrefcount(1)))

#########################################################################################################
# 连续表达式
x = 1
# 1 < 2 and 2 < 3
print(1 < 2 < 3)
# 这里并不是比较 1==x，然后在于3比较；而是同上， 1 == x and x < 3
print(1 == x < 3)

