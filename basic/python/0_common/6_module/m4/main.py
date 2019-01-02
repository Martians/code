
type = 1

""" 两种方式都可以
"""

if type == 1:
    from module import *
    test()

elif type == 2:
    import module
    module.test()
