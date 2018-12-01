# coding=utf-8

from fabric import Connection

'''
    fabric：http://docs.fabfile.org/en/latest/api/connection.html#fabric.connection.Connection.run 
    invoke：http://docs.pyinvoke.org/en/latest/api/runners.html#invoke.runners.Runner.run
    
    环境变量
'''

#######################################################################################################################
''' 环境变量
'''
config = {
    'host': '192.168.0.80',
    'port': 22,
    'user': 'long',
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
''' 执行选项
'''
if 1:
    ''' 命令执行
            hide = out|stdout、err|stderr、both|True
            warn：命令执行失败时，不抛出异常，只是显示 stderr; 其他异常还是会抛出
    '''
    if 1:
        result = connect().run("ls /etc1", hide="out", warn=True, echo=True)
        print(result.failed)

#######################################################################################################################
''' 执行结果
        http://docs.pyinvoke.org/en/latest/api/runners.html#invoke.runners.Result
'''
if 1:
    result = connect().run("ls /etc/hosts")
    print(''' command info:
            run {0.command!r} on {0.connection.host}, 
            got exit: {0.exited}, result: {0.ok} {0.failed}, 
            stdout: {0.stdout} 
            stderr: {0.stderr}'''.format(result))

#######################################################################################################################
''' 环境变量，不起作用？
'''
if 1:
    c = connect().run("echo 11 $PYTHONPATH", env={'PYTHONPATH': '/some/virtual/env/maybe'})

    ''' 执行异常
        http://docs.fabfile.org/en/2.0/api/exceptions.html
        似乎只有在执行group命令时会出现
    '''