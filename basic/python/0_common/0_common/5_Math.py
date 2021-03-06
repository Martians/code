# coding=utf-8

import math

print(2 ** 200)

########################################################################################################
'''
    1. 传统除法：整数除法为整数，浮点数除法保持小数
    2. 真除法：保留小数部分
    3. floor除法：
         也叫截断除法，总是省略小数部分 //，注意：结果的数据类型依赖于操作数的类型
    4. math.floor, math.trunc：对负数而言稍有不同

    2.0中/表示传统除法，3.0中/表示真除法
'''
print(5 / 2, 5 // 2)
# 即使是浮点数，5 // 2.0的结果是浮点数，但是也只有整数部分
print(5 / 2.0, 5 // 2.0)


# round 四舍五入，应该仍然返回一个浮点数？
print(5 / 3, round(5 / 3))

# 平方根
print(144 ** .5, pow(144, .5), math.sqrt(144))

###########################################################################################################
import random
print(random.random())
print(random.randint(1, 100))
print(random.randrange)
