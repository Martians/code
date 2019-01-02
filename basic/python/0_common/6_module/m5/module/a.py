

""" 相对导入

    注意：不能作为顶层模块来执行，否则会失败
"""
from . import b

def test():
    print("a")

b.test()

import sys
print(sys.path)

abc=1000