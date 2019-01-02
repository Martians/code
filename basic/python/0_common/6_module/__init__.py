
"""
## 含义
    1. 模块是命名空间：是变量名的软件包；模块是变量名的封装
    2. 模块是个对象：内部的所有顶级变量，相当于模块对象的属性
    3. 角色之间划分：顶层文件，即脚本，包含了主要控制流；而模块是工具库
    4. 模块文件的选择，有很多中形态，.py、zip、.so、P549

## 搜索路径（最终都是设置sys.path）
    1. 顶层脚本：小心会覆盖掉其他目录中，有相同名称的模块，因此取名不要相同
       即执行时的主目录

    2. PYTHONPATH：
            IDE上经常不生效

    3. pth文件：安装目录的顶层、或者site-package
            /home/long/.pyenv/versions/3.6.5
            /home/long/.pyenv/versions/3.6.5/lib/python3.6/site-packages/test.pth

    4. 动态添加：sys.path.append

    5. 方便使用
        1. IDE中：建议使用方案3

        2. 混合使用？
            lib库中可以直接调试
            顶级脚本中可以直接调试，调用lib库

        3. 得到各种path：
            http://www.cnblogs.com/xiyuan2016/p/9187695.html

## 使用
    1. import、from：是赋值语句，不是在编译期间的声明
        import ...
        import ... as
        from ... import ...
        from ... import *

        from：把变量名复制到另一个作用域；会让变量的位置更加隐秘和模糊，推荐使用import；等效性：P558
              不是变量的一个别名

        import：如果修改值，会修改原模块的变量

    2. reload可以重新载入模块，不需要重启程序就能更新模块中的变更，比如与数据库连接

    3. 包导入：就是把目录变成命名空间；可以简化搜索路径的设置

            1. 必须有 __init__.py 文件，这样可以区分出不是python的目录，不会因为同名目录（但无效的）出现在搜索路径前边，而造成覆盖
                搜索路径指定的那些根目录中，不需要该文件

            2. 包初始化
                __init__：不使用
                __init__：__all__
                __init__：手动 import，相当于使用了 __all__
                __init__：from import *
                        外部使用 import module
                        外部使用 from module import *

                __all__只针对调用时，使用 from import *时起效果

            3. 隐藏：P593
                    _开头的变量不会自动导出（手动可以）
                    使用了__all__之后，只有__all__中的才会导出
    4. 相对导入
            https://docs.python.org/3.6/tutorial/modules.html#packages

        1. 3.0中：默认是绝对导入，不指定前缀，就会直接取查找syspath；只有显示指定了，才会进行相对导入
        2. 该语法导致，只会搜索当前包，如果没找到，也不会去sys.path的绝对路径中查找; 只用于包内导入
        3. 限制：将此包作为顶级模块使用时，相对导入不起作用；只能是在包中使用时用到（python的处理：顶级模块，必然不处于一个包中）
"""

