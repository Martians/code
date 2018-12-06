# coding=utf-8

from fabric import SerialGroup as Group, Connection, ThreadingGroup

''' 
    1. 小结
        1）多host执行时的多种方式，以及利弊
        2）
        
    1. 说明
        简介：http://docs.fabfile.org/en/latest/getting-started.html#bringing-it-all-together
        串行：http://docs.fabfile.org/en/latest/api/group.html#fabric.group.SerialGroup
        并行：http://docs.fabfile.org/en/latest/api/group.html#fabric.group.ThreadingGroup

    2. 功能
        1）将命令针对多个host串行执行
        2）多线程并行执行
        3）同时执行时，分别判断执行结果
'''

hosts = ['192.168.0.81', '192.168.0.82']
#######################################################################################################################
''' 脚本准备：
    这两个节点使用root登陆
'''
config = {
    'host': '192.168.0.80',
    'port': 22,
    'user': 'root',
    'pass': '111111'
}
command = "uname -a"

if 1:
    kwargs = {'user': config['user'], 'connect_kwargs': {'password': config['pass']}, 'connect_timeout': 1}

    def connect(host=config['host'], **args):
        return Connection(host, **kwargs, **args)

    def show_info(c):
        return c.run(command)

#######################################################################################################################
''' 方式1：原始方式，一个个connection获取
'''
if 1:
    for host in hosts:
        c = connect(host)
        show_info(c)

#######################################################################################################################
''' 方式2：只通过group获取connect
        优势：相对于方法3，更加灵活，可以执行多个条件判断判断
'''
if 1:
    # 简单执行一条命令
    for conn in Group(*hosts, **kwargs):
        show_info(conn)

    # 将执行操作流，放在一个函数中
    def more_work(c):
        if c.run('test -f /root/test', warn=True).failed:
            c.run('touch /root/test')
            c.run('ls /root/test')

    for conn in ThreadingGroup(*hosts, **kwargs):
        more_work(conn)

#######################################################################################################################
''' 方式3：统一执行
        1. 如果出错，对应connecion的result放置的将是一个exception
        2. 缺点：相对于方案2，不够灵活
'''
if 1:
    # 执行后，依次检查结果
    pool = Group(*hosts, **kwargs)
    results = pool.run('uname -a')
    for connection, item in results.items():
        print("{0.host}: {1.stdout}".format(connection, item))

#######################################################################################################################
''' 异常处理
        1. 为了确保即使某些host执行出错，其他host也可以继续执行下去：
            1）执行时使用 warn=True
            2）使用try cache，但这样就没有任何执行结果了
'''
if 1:
    if 1:
        print("prepare environment")
        connect(hosts[0]).run('touch /root/test1')
        connect(hosts[1]).run('rm /root/test1 -rf')

    print("\nsome failed, some success:")
    pool = Group(*hosts, **kwargs)
    results = pool.run('ls /root/test1', warn=True)
    for connection, item in results.items():
        print("{0.host}: {1.failed}, {1.stdout}, {1.stderr}".format(connection, item))

    ''' 统一检查结果：成功的部分、失败的部分
            实际上都显示是成功的？功能不起效果？
    '''
    print("result failed: {}".format(results.failed))
    print("result success: {}".format(results.succeeded))