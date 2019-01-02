# coding=utf-8


def describe_pet(animal_type, pet_name):
    print("\nI have a " + animal_type + ".")
    print("My " + animal_type + "'s name is " + pet_name.title() + ".")


''' 关键字实参，是键值对
'''
describe_pet(animal_type='hamster', pet_name='harry')


def build_person(first_name, last_name):
    person = {'first': first_name, 'last': last_name}
    return person


musician = build_person('jimi', 'hendrix')


def make_pizza(*toppings):
    print(toppings)

''' 可变参数，实际传入的是一个元组
'''
make_pizza('pepperoni')
make_pizza('mushrooms', 'green peppers', 'extra cheese')


''' 任意数量的关键字参数，传入的是一个字典
'''
def build_profile(first, last, **user_info):
    profile = {}
    profile['first_name'] = first
    profile['last_name'] = last
    for key, value in user_info.items():
        profile[key] = value
    return profile


user_profile = build_profile('albert', 'einstein',
                             location='princeton',
                             field='physics')
print(user_profile)

copy
deepcopy P162

==
is
