
""" 导入 a、b 模块
    1. 动态添加：sys.path.append('./module')

    2. 静态添加：
            /home/long/.pyenv/versions/3.6.5/lib/python3.6/site-packages/test.pth
            /mnt/hgfs/local/code/basic/python/0_common/6_module/module

    3. 加到PYTHONPATH
            效果非常差：编辑器可以识别出来；编译时会出错，找不到模块

    分析：
    1. IDE 编辑器
        无法识别 1）和 3），只能使用2）
        当前目录为工程的根目录

    2. IDE 编译器
        可以正常执行

"""
import sys
print(sys.path)
# sys.path.append('./module')

import a, b

a.test()
b.test()