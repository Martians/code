
"""
## 含义
    1. 模块是命名空间：是变量名的软件包
    2. 模块是个对象：内部的所有顶级变量，相当于模块对象的属性
    3. 角色之间划分：顶层文件，即脚本，包含了主要控制流；而模块是工具库

## 搜索路径（最终都是设置sys.path）
    1. 顶层脚本：小心会覆盖掉其他目录中，有相同名称的模块，因此取名不要相同

    2. PYTHONPATH：
            IDE上经常不生效

    3. pth文件：安装目录的顶层、或者site-package
            /home/long/.pyenv/versions/3.6.5
            /home/long/.pyenv/versions/3.6.5/lib/python3.6/site-packages/test.pth

    4. 动态添加：sys.path.append

    如果是IDE：
        建议使用方案3


    如何混合使用？
        lib库中可以直接调试
        定级脚本中可以直接调试


https://docs.python.org/3.6/tutorial/modules.html#packages
相对路径导入

共享变量名
全局变量

list(sys.modules.keys()) P545


http://www.cnblogs.com/xiyuan2016/p/9187695.html

搜索路径： P546

"""

