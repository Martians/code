# coding=utf-8

'''
    本地命令执行测试

功能
    1. 执行 tasks.py 中定义的task；将命令行参数，当做函数参数传递
    2. 使用 @task修饰符，修改 task的各种属性、行为
    3. 嵌套名字空间，组织任务之间的层级关系
    4. 交互式操作，根据 prompt信息，自动进行相应的反馈
    5. 执行命令过程中，传递环境变量

## 注意
    1. 函数命名中，出现的下划线，会被自动替换为 -，才能在命令行中使用

    invoke --list
'''

from invoke import task, call, Collection, Responder

########################################################################################################################
''' task 调用
        自动将函数参数变为命令行参数
    invoke --help build         # 显示帮助

    invoke build abc            # 位置参数
    invoke build --name abc
    invoke build --name=abc
    invoke build -n     abc
    invoke build -nabc
'''

if 1:
    @task(help={'name': "name of the build"})
    def build(c, name):
        print("build with name "+ name)
        c.run("ls /etc/hosts")

########################################################################################################################
''' Task 修饰符
    http://docs.pyinvoke.org/en/latest/api/tasks.html#invoke.tasks.task
        name、aliases
        positional、optional、iterable
        default
        pre/post、autoprint
'''

if 1:
    ''' pre/post
        调用build时自动执行；他们也可以单独执行
        必须在build之前定义
    '''
    if 1:
        @task
        def clean(c):
            print("do clean work")

        @task
        def ending(c):
            print("the end")

        # 带参数的函数作为 pre函数
        @task
        def params(c, which=None):
            print("the param {}".format(which))

        # build函数上中可以直接定义 @task(clean)
        #   这里传入参数到 params 函数的 which 参数
        @task(pre=[clean, call(params, which='all')], post=[ending])
        def build(c, name):
            print("build with name "+ name)
            c.run("ls /etc/hosts")

    ''' 可选参数
        http://docs.pyinvoke.org/en/latest/concepts/invoking-tasks.html

        invoke compile
        invoke compile --log
        invoke compile --log work
    '''
    @task(optional=['log'])
    def compile(c, log=None):
        print("comiple, log name: {}".format(log))

    ''' 列表参数
        http://docs.pyinvoke.org/en/latest/concepts/invoking-tasks.html#iterable-flag-values

        invoke getlist -m aa bb cc        # 失败
        invoke getlist -m aa -m bb -m cc
        invoke getlist --my-list aa -m bb -m cc
    '''
    @task(iterable=['my_list'])
    def getlist(c, my_list):
        print("get list: {}".format(my_list))

    ''' Bool参数，默认提供相应的 --no-color参数
        invoke run-tests --no-color
    '''
    @task
    def run_tests(c, color=True):
        print("color: {}".format(color))

    ''' 任务别名，还是改名？
    '''
    @task(name="dir")
    def dir_(c):
        print("dir alia to dir_")

    ''' 默认任务
    '''
    @task(default=True)
    def taskd(c):
        print("default task")
########################################################################################################################
''' 命令行参数
    http://docs.pyinvoke.org/en/latest/invoke.html

    invoke --list

    1. 自动补全：http://docs.pyinvoke.org/en/latest/invoke.html#shell-tab-completion
        invoke --complete -- build
        invoke --complete -- build -
        invoke --complete -- build --n
    2. --hide
    3. --prompt-for-sudo-password
'''
if 1:
    @task
    def build(c, name):
        print("build with name "+ name)
        c.run("ls /etc/hosts")

    ''' 类型转换
        根据default value，可以推测出参数类型，比如整形
    '''

    ''' 命令重复执行
        包含在命令行中，也包含在pre-task中
        --no-dedupe 取消
    '''

########################################################################################################################
''' 名字空间 Collection，命令使用时要增加前缀
    http://docs.pyinvoke.org/en/latest/concepts/namespaces.html

    1. 作用：将不同的任务放在不同的名字空间之下，建立名字空间的树状结果
    2. 默认：自动创建一个root空间，并将所有的task放进去；可以改变此行为
    3. load：可以从其他module import任务；也可以将module task 直接加入到顶级task中

    4. 名字空间可以设置不同的环境配置
        http://docs.pyinvoke.org/en/latest/concepts/configuration.html#configuration-overriding
        ns.configure({'sphinx': {'target': "built_docs"}})
        这里有个通过配置文件，设置task默认参数的例子
'''
if 1:
    @task()
    def release(c):
        print("release  -- have another name")

    @task()
    def release1(c):
        print("release1 -- have another name")

    # 如果定义了一个 namespace/ns 名字空间，将替换掉默认的
    #   即：只显示手动加入进去的 task
    ns = Collection()

    # _自动替换为-来使用，invoke release-newname
    ns.add_task(release, name="release_newname")
    # 别名不起作用？
    ns.add_task(release1, aliases=('deploy', 'pypi'))

    # 手动创建一个名字空间，建立树状结构
    #   增加命令 child.release
    child = Collection('child')
    child.add_task(release)

    # 名字空间中加入其它名字空间, invoke newname.release
    ns.add_collection(child, "newname")

if 1:
    ''' 通过导入的 module 建立名字空间
        http://docs.pyinvoke.org/en/latest/concepts/loading.html

        invoke --list --collection common   # 只显示某个collection
    '''
    import common
    ns = Collection()
    # 等同于 ns.add_collection(Collection.from_module(common))
    ns.add_collection(common, "name_for_common")

    # 可以直接将其他 mudule的 task，加入到顶级 task中，而不是在nested名字空间之下
    ns.add_task(common.build, "form_module")

########################################################################################################################
''' Context
    http://docs.pyinvoke.org/en/latest/api/context.html#invoke.context.Context.cd

    1. 目录切换：执行命令时进入某个目录
    2. 前缀命令：执行命令前，先执行其他命令，&&
    3. 直接访问属性
    4. 执行过程，最终传递给runner，run的各种参数：
        http://docs.pyinvoke.org/en/latest/api/runners.html#invoke.runners.Runner.run
'''

if 1:
    @task
    def dircheck(c):
        with c.cd('/var/www'):
            c.run('pwd')

    @task
    def prefix(c):
        with c.prefix("ls /home -l"):
            c.run('ls /home/long -l')

########################################################################################################################
''' 配置文件
    http://docs.pyinvoke.org/en/latest/concepts/configuration.html

    1. 多种配置文件路径，优先级
    2. 环境变量，只用于覆盖配置中的value，对应于 INVOKE_RUN_ECHO=1 run.echo
    3. 名字空间下的配置：接近root的名字空间配置，会覆盖上层名字空间中相同名称的配置
        http://docs.pyinvoke.org/en/latest/concepts/configuration.html#collection-based-configuration
    4. 名字空间对象，可以直接从context里边访问属性
    5. 能够直接访问当前工程目录下的 invoke.yaml 中的配置
'''
if 1:
    @task
    def output(c):
        print(c.tasks.collection_name)
        print(c.config.tasks.collection_name)
        print(c['tasks'])
        print(c.config.other)

########################################################################################################################
''' sudo
    http://docs.pyinvoke.org/en/latest/api/context.html#invoke.context.Context.sudo

    1. config
        sudo.password
        sudo.prompt Default: [sudo] password:

'''
if 1:
    @task
    def prompt(c):
        name = '/tmp/abc'
        responder = Responder(
            pattern=r"rm: remove directory ' + name + '? ",
            response="y\n",
        )
        c.run("mkdir -p " + name)
        c.run("rm -r " + name, watchers=[responder])

    @task
    def connect(c):
        responder = Responder(
            pattern=r"root@192.168.10.111's password: ",
            response="111111\n",
        )
        c.run('ssh root@192.168.10.111', pty=True, watchers=[responder])

    ''' Sudo
        1. sudo配置：http://docs.pyinvoke.org/en/latest/concepts/configuration.html#configuration-files
            sudo.password、 sudo.user
            也可以运行时修改

        2. 可以添加  FailingResponder into the watchers
    '''
    @task
    def drop_cache(c):
        command = "sh -c 'echo 1 > /proc/sys/vm/drop_caches'"
        # c.config.sudo.password = '111111'
        c.sudo(command)
        c.sudo(command, user='root', password='111111')

########################################################################################################################
''' 内部实现
    1. context，每个函数都必须制定该参数，是主要的句柄
        其记录全局数据、从配置文件中获取的配置，cli标志、已启动task等
'''