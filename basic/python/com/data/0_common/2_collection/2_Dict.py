#coding=utf-8

alien_0 = {'color': 'green', 'points': 5}
print(alien_0['color'])
print alien_0

alien_0['color'] = 'green1'
print alien_0

# 不存在则返回默认值
value = alien_0.get('value', 100)
print 'value exist or default: ', value


''' 删除，同list
'''
del alien_0["color"]
print alien_0

user_0 = {
    'username': 'efermi',
    'first': 'enrico',
    'last': 'fermi',
}

''' 遍历key、value
'''
for key, value in user_0.items():
    print(key + "->" + value)

for key, value in sorted(user_0.items()):
    print(key + "->" + value)

# 遍历value，并确保不会重复
for value in set(user_0.values()):
    print(value)


#################################################################################################
''' set
'''

sets = {x**2 for x in [1, 2, 3, 4]}
print sets