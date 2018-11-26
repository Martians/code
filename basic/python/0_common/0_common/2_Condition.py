#coding=utf-8

banned_users = ['andrew', 'carolina', 'david']
user = 'marie'

''' 检查是否在集合中出现
'''
if user not in banned_users:
    print "not find"

''' 检查集合是否为空
'''
requested_toppings = []
if not requested_toppings:
    print "empty"