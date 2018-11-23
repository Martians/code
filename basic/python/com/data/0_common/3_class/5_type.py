#coding=utf-8

ls = [1, 2, 3, 4]
print(type(ls))

''' 语法上没错，但是python中不会这么用
'''
if type(ls) == list:
    print("yes1")

if type(ls) == type([]):
    print('yes2')

if isinstance(ls, list):
    print('yes3')