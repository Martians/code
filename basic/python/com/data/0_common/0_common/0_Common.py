#coding=utf-8

#!/user/bin/env python
#!/user/local/bin/python

''' 两种前缀写法，第一种适应性更强
    在PATH中查找python的路径
'''

# 获得 python 格言
# import this

###############################################################################
# 获得包中可用的变量名的列表
import sys
print(dir(sys))

''' 获得帮助
'''
str = '1122'
print(dir(str))

print(help(str.replace))

###############################################################################
''' Python 中所有都是对象
'''