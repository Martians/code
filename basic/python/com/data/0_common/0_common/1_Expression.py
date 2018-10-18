#coding=utf-8


d = {"a": 1, 'c': 3, 'b': 2}

# if else 表达式
#   与scala不同，语句不是对象；因此先把一个值 放在前边
value = d['f'] if 'f' in d else 0


#########################################################################################################
''' 条件语句
'''
if not 'f' in d:
    print 'f not exist'

if 'f' not in d:
    print 'f not exist'