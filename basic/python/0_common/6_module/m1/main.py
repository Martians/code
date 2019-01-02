
""" 导入 a、b 模块
    IDE中执行时：当前路径为6_module，可以识别出
    命令行中执行：需要添加搜索路径，或者sys.path.append

"""
import sys
# print(sys.path)
sys.path.append('./module')

import a, b

a.test()
b.test()

print("==============================")
print(a.__dict__)
print(a.__dict__.keys())
print(a.__dict__['abc'])


""" 模块特有内容
"""
print("==============================")
print(__file__)
print(__name__)
""" __name__充当模式标志
        当前文件作为可导入的库
        当前文件作为顶层脚本
"""
print(a.__name__)


""" 已经导入的所有模块 P545
"""
print("==============================")
print(sys.modules.keys())