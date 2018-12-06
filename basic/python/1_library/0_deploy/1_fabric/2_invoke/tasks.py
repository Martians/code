# coding=utf-8

'''

## 注意
    1.

    invoke --list
    subprocess’ output

    启动时，默认使用的是将各方配置整合起来的config对象，传递给 @task标识的任务

    配置：http://docs.pyinvoke.org/en/latest/concepts/configuration.html#default-values

    fabric能否使用task.yaml作为配置文件

    环境变量：必须是之前已经定义过的，而不能新增？下划线隔开
        也防止了本身存在的歧义
    从python实现中，也可以访问到环境变量

这些命令，在fabric上是否也适用？
'''

from invoke import task, call, Collection, Responder, Config

########################################################################################################################
''' 单个函数
        1. 函数原型
                1. 每个函数都必须使用该参数，通常命名为 c、ctx、context
                   context中包含了从配置文件读取的所有内容(c.config)，执行时可以直接使用
                   其记录全局数据、从配置文件中获取的配置，cli标志、已启动task等
                
        2. 命令行参数：
                1. 自动将函数的参数，生成对应的短命令、长命令
                2. 对于默认值为 True 的 bool 类型，还生成 --no-[name]的选项
             
        3. 参数类型
                1. 有默认参数时，根据给出的value类型，决定了参数类型（int、string） 
                2. 没有默认参数，将使用string类型
        
        4. 下划线转换
                1. 函数命名中，出现的下划线_，会被自动替换为 -
                2. 函数参数中，也将进行上述转换
                
                http://docs.pyinvoke.org/en/latest/concepts/namespaces.html#dashes-vs-underscores
                可以修改此行为：tasks.auto_dash_names: false
'''
if 1:
    ''' 添加help信息
                1. 参数的help信息
                2. 整个函数的信息
            
        invoke --help build1         # 显示帮助，包括docstring、@task中对各个参数的注解
                或者：invoke build --help
        
        invoke build1 abc            # 没有默认参数的，也可以当做位置参数使用
        invoke build1 --name abc
        invoke build1 --name=abc
        invoke build1 -n     abc
        invoke build1 -nabc
        
        invoke build1 abc -v
        invoke build1 1 --no-value   # 默认参数为 True时，会自动生成 --no-[name] 的选项
    '''
    @task(help={'name': "name of the build"})
    def build1(c, name, value=True):
        ''' say hello to the name
        '''
        print("build with name {}, {}".format(name, value))
        c.run("ls /etc/hosts")

########################################################################################################################
''' 命令行参数
        http://docs.pyinvoke.org/en/latest/invoke.html

    invoke --list [namespace] 
    invoke --list --collection common    # 只使用 collection common，默认查找的是 tasks 这个 collection
    
    1. 参数解析
        命令顺序，实际core option也可以放在task之后，与task参数放在一起，只要不发生歧义即可
        
    2. 自动补全：
            http://docs.pyinvoke.org/en/latest/invoke.html#shell-tab-completion
            生成自动补全脚本：--print-completion-script
            
            invoke --complete -- build2       # 显示了所有命令
            invoke --complete -- build2 -
            invoke --complete -- build2 --n
            invoke --complete -- build2 --na
   
    3. run的参数
            --hide
            --warn-only
            
    4. 默认行为
            --no-dedupe：对一个函数只调用一次，即使在命令行相同的命令被触发（可能是pre/post等连锁触发）多次，也只执行一次
            --collection：修改默认的collection
        
    5. 运行参数：
            -d（--debug） -e（--echo）
            --prompt-for-sudo-password
'''
if 1:
    ''' invoke -d build2 abc
        invoke -e build2 abc
        
        invoke -r submod1 -c common -l      # 使用 submod1 目录下的 common 模块作为默认collection
    '''
    @task
    def build2(c, name):
        print("build with name "+ name)
        c.run("ls /etc/hosts")

########################################################################################################################
''' Task 修饰符
        http://docs.pyinvoke.org/en/latest/api/tasks.html#invoke.tasks.task
        对task属性的设置：1）通过@task 2）通过将task加入到collection时修改
        
        1. pre/post、autoprint
        2. name、aliases
        3. positional、optional、iterable
        4. default：collection的默认task
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

        # 将带参数的函数作为pre函数
        @task
        def params(c, which=None):
            print("the param {}".format(which))

        # build函数上中可以直接定义 @task(clean)
        #   这里传入参数到 params 函数的 which 参数
        @task(pre=[clean, call(params, which='all')], post=[ending])
        def build3(c, name):
            print("build with name "+ name)
            c.run("ls /etc/hosts")

    ''' 可选参数
        http://docs.pyinvoke.org/en/latest/concepts/invoking-tasks.html

        invoke compile          
        invoke compile --log            # 相当于参数为 true
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

    ''' Bool参数
            默认提供相应的 --no-color参数
            
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
''' 名字空间 Collection
        构建：http://docs.pyinvoke.org/en/latest/concepts/namespaces.html
        api：http://docs.pyinvoke.org/en/latest/api/collection.html
    
        讨论的是：如何直接在命令行中，显示哪些命令、如何使用其他模块中的命令
        
        测试时，需要打开 if 0：开关；测试其他内容时，要关闭这里的几个测试
        
    1. 名字空间的作用
            1）管理任务组织：将不同的任务放在不同的名字空间之下，建立名字空间的树状结果
                系统默认：自动创建一个root空间，并将所有的task放进去；可以改变此行为
                自动使用tasks的模块，因此使用的文件是 tasks.py
                
            2）管理配置内容，覆盖 context.config中的部分内容
                http://docs.pyinvoke.org/en/latest/concepts/configuration.html#configuration-overriding
                ns.configure({'sphinx': {'target': "built_docs"}})
                
    2. 使用方式
            1）可以import其他module，加入到当前的collection中
                    module 需要先加入到 collection 之后，才能直接被 invoke 访问到
                    默认将模块中的所有task，都加入到collection中去
                    如果模块中已经有了 ns、namespace，就复制这个内容中，而不是将所有task到导入

            2）也可以将module task 直接加入到顶级task中

    4. 名字空间可以设置不同的环境配置        

Todo：
    1. 如何快速构建一个 collection，将当前文档中个所有task都加入进去
            invoke -r submod1 -c common -l  # 将使用文件夹 submod1 下 common module的所有task；task都没有前缀 common
    
'''
if 0:
    ''' 
        invoke --list
    '''
    @task()
    def release(c):
        print("release  -- have another name")

    @task()
    def release1(c):
        print("release1 -- have another name")

    ''' 覆盖默认名字空间
            1. invoke默认会建立一个名字空间，名字为 tasks，自动将 tasks module 中的所有task加入到其中（因此默认使用 tasks.py）
            
            2. 如果modules中定义了一个空参数的 Collection()，将替换掉默认的root；变量名必须是 namespace or ns
                    这将导致：invoke --list 只会显示手动加入到这个 namespace 中的 task 
    '''
    ns = Collection()

    ''' 添加task到空间
            1. 加入的同时，可以替换其名字
                1）也可通过注解方式：@task(name='new-name')         修改名字
                           @task(aliases=('foo', 'bar'))  添加别名
                        
                2）添加方式：ns.add_task(release, name='deploy')
                           简写：ns.add_task(release, 'deploy')
                           
                3）添加别名：ns.add_task(release, aliases=('deploy', 'pypi'))
            
            2. 直接添加module的task
    '''
    ns.add_task(release, name="release_new name")
    # ns.add_task(release1, aliases=['deploy'])

    ''' 新建子空间
            1. 手动创建一个名字空间，建立树状结构
               在其中添加task，增加命令 child.release
            
    '''
    child = Collection('child')
    child.add_task(release, "child_space")  # 修改task的名字

    ns.add_collection(child, "child-release")

if 0:
    ''' 添加module：
            http://docs.pyinvoke.org/en/latest/concepts/namespaces.html#importing-modules-as-collections

            1. 模块名字：
                1）默认名字来自于模块对象的__name__ 属性，没有的话直接使用 模块的文件名
                2）加入时，可以指定一个新名字，也可以不指定
                3）包含多个文件的模块，可以增加一个 __init__.py 文件，将所有模块都组织到一起去（见 submod2）

            2. task设置
                1）collection默认task（最经常使用的），@task(default=True)；或者add_task时指定
                2）也可以将模块中的task，加入到 顶级名字空间的task中，这样就可以不使用module前缀了
        
            3. 构建时，混合的添加 module、task
                1）Collection(release.release, docs)
                2）Collection(docs, deploy=release.release)
                
            3. 修改默认collection：（不使用tasks）
                http://docs.pyinvoke.org/en/latest/concepts/loading.html
                
                1）修改默认：tasks.collection_name、--collection
                2）搜索路径：tasks.search_root、 --search-root
                
                invoke -r submod1 -c common -l 
                            
        invoke --list
    '''
    ns = Collection()

    import submod1.common as common
    ns.add_collection(common, "module_common")

    ''' 可以直接将其他 module 的 task，加入到顶级 task中，而不是在nested名字空间之下
    '''
    ns.add_task(common.build, "form_module_common_to_top")

    print("task name: ".format(ns.task_names))

########################################################################################################################
''' 配置文件
        使用：http://docs.pyinvoke.org/en/latest/concepts/configuration.html
        api：http://docs.pyinvoke.org/en/latest/api/config.html
    
    1. 多种配置文件路径，优先级
        1. 相对于fabric，可以将配置文件 invoke.yaml 放在当前目录中; fabric不支持此方式 
        2. config分成很多个level的
            
    2. 环境变量
        1. 只能用于覆盖配置中已有的的value；这是系统中专门做的限制
        
        2. 命名规则：      run.echo  => INVOKE_RUN_ECHO=1 
            增加INVOKE前缀；全部使用大写；将层级关系转换为_
    
    3. 名字空间下的配置
            接近root的名字空间配置，会覆盖上层名字空间中相同名称的配置
            http://docs.pyinvoke.org/en/latest/concepts/configuration.html#collection-based-configuration
        
    4. 修改配置的几种方式
        1. 通过几个配置文件
        2. 通过collection：configure
        3. 代码中修改：必须在某个@task的函数中修改？
        
'''
if 1:
    ''' invoke output output2       # output中的修改，在output2可以生效
    '''
    @task
    def output(c):
        ''' 可以跳转到定义处，查看所有默认配置
        '''
        print(Config.global_defaults())

        print("\n")
        print(c.tasks.collection_name)
        print(c.config.tasks.collection_name)
        print(c['tasks'])
        print(c.config.other)

        ''' 直接修改context中已有内容, 但是没有生效？
        '''
        c.tasks.auto_dash_names = False


    @task
    def output2(c):
        print("c.tasks.auto_dash_names: {}".format(c.tasks.auto_dash_names))

if 0:
    ''' 模块内配置：
        
        invoke build5
    '''
    @task
    def build5(c):
        print("ns.work: " + c['work'])
        print("ns.work: " + c.work)

        print("\ncollection config:")
        print(ns.configuration("build5"))
        print(ns.configuration())

    ns = Collection("inner", build5)
    ns.configure({'work': 'override value'})

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


    @task
    def drop_cache(c):
        command = "sh -c 'echo 1 > /proc/sys/vm/drop_caches'"
        # c.config.sudo.password = '111111'
        c.sudo(command)
        c.sudo(command, user='root', password='111111')

    ''' Sudo
        1. sudo配置：http://docs.pyinvoke.org/en/latest/concepts/configuration.html#configuration-files
            sudo.password、 sudo.user
            也可以运行时修改

        2. 可以添加  FailingResponder into the watchers
    '''

########################################################################################################################
''' Context
    
    1. 与config的关系
        1. context.config 是其类成员
        2. context继承了dataproxy，因此可以直接访问配置内容，而无需通过context.config 

    2. session功能
        http://docs.pyinvoke.org/en/latest/api/runners.html#invoke.runners.Runner.run
        
        1. 目录切换：执行命令时进入某个目录
        2. 前缀命令：执行命令前，先执行其他命令，&&
        3. 直接访问属性
        4. 执行过程，最终传递给runner，run的各种参数
'''

if 1:
    @task
    def dircheck(c):
        with c.cd('/var/www'):
            c.run('pwd')

    ''' 所有任务执行之前，要执行的命令
    '''
    @task
    def prefix(c):
        with c.prefix("ls /home -l"):
            c.run('ls /home/long -l')


    ''' 环境变量
    '''
    @task
    def info(c):
        c.run('echo $PYTHONPATH', env={'PYTHONPATH': '/some/virtual/env/maybe'})