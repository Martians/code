# coding=utf-8

'''
    本文档用 python 3_fabric.py 的方式执行

    官方：http://docs.fabfile.org/en/2.0/concepts/configuration.html
    git: https://github.com/fabric/fabric/tree/master/fabric

    invoke：http://docs.pyinvoke.org/en/latest/concepts/configuration.html#configuration

    1. 由多个组件
        1）Invoke：cli解析、task组织、命令执行
        2）paramiko：ssh、sftp

## 功能
    1. cli命令，将函数、object当做是cli命令，一次性执行多个
    2. 执行之前调用其他命令，pre、post
    3. 解决sudo时需要输入密码的问题
    4. 并发在远程几个机器上执行操作
'''

from fabric import Connection, SerialGroup as Group, Config

config = {
    'host': '192.168.10.10',
    'port': 22,
    'user': 'long',
    'pass': '111111'
}

########################################################################################################################
''' 配置

    1. 文档：http://docs.fabfile.org/en/2.0/concepts/configuration.html
       http://docs.pyinvoke.org/en/latest/concepts/configuration.html#the-configuration-hierarchy
       文件：/etc/fabric.yml; ~/.fabric.py; ~/.tasks.yaml; ~/.fabric.json; 目前还不支持 env.hosts、env.roles

       注意：不能读取当前目录中的 fabric.yml，必须复制到其他位置

    2. 配置内容：主要是ssh相关配置，
        user、port
        connect_kwargs: 作为dict，最终传递给SSHClient.connect;
            1) 引用：http://docs.paramiko.org/en/latest/api/client.html#paramiko.client.SSHClient.connect
            2) password:  --prompt-for-login-password.
            3) username: 不能在这里设置 username，connection 的解析中，该信息不能设置在 connect_kwargs 中

        load_openssh_configs：True
        ssh_config_path：ssh client的配置，符合ssh标准，--ssh-config
            如果 SSHConfig 对象已经设置，那么将不会读取配置文件
            没有指定该参数，就会默认读取 ~/.ssh/config and/or /etc/ssh/ssh_config
'''

# 显示默认配置
if 1:
    print("\nglobal default:")
    print(Config.global_defaults())

    # 这个config会默认读取相关配置文件，检查配置是否成功
    conf = Config()
    print("\nconnections config:")
    print(conf.connect_kwargs)

    # 解析yaml，可以读取任何自定义配置
    print(conf.other)

########################################################################################################################
''' connection
    http://docs.fabfile.org/en/2.0/api/connection.html#fabric.connection.Connection
    默认不需要主动 close
'''
if 1:
    # 使用配置中，默认的user、password
    #   或者设置 Connection("root@ubuntu:22)
    Connection(host=config['host']).run("uname -a")

    # 覆盖connect_kwargs参数
    Connection(host=config['host'], connect_kwargs={'password': config['pass']}).run("uname -a")

########################################################################################################################
''' 命令执行
    run：http://docs.pyinvoke.org/en/latest/api/runners.html#invoke.runners.Runner.run
        hide = out|stdout、err|stderr、both|True
        warn：命令执行失败时，不抛出异常，只是显示 stderr; 其他异常还是会抛出
'''
if 0:
    c = Connection(host=config['host'], user=config['user'], port=config['port'])
    result = c.run("ls /etc1", hide="out", warn=True, echo=True)
    print(result.failed)

''' 执行结果
    http://docs.pyinvoke.org/en/latest/api/runners.html#invoke.runners.Result
'''
if 0:
    c = Connection(host=config['host'], user=config['user'], port=config['port'])
    result = c.run("ls /etc/hosts")
    print("Ran {0.command!r} on {0.connection.host}, got exit: {0.exited}, result: {0.ok} {0.failed}, stdout:\n{0.stdout}".format(result))

''' 环境变量，不起作用？
'''
if 0:
    c = Connection(host=config['host'], user=config['user'], port=config['port'])
    result = c.run("echo 11 $PYTHONPATH", env={'PYTHONPATH': '/some/virtual/env/maybe'})

########################################################################################################################
''' 批量执行，串行执行、并行执行(多线程)
    http://docs.fabfile.org/en/2.0/api/group.html#fabric.group.SerialGroup
    http://docs.fabfile.org/en/2.0/api/group.html#fabric.group.ThreadingGroup
'''
if 0:
    def show_info(c):
        res = c.run("uname -a", hide=True)
        print("host {0.connection.host}, info: {0.stdout}".format(res))

    for conn in Group('192.168.10.111', '192.168.10.112'):
        show_info(conn)


''' 执行异常
    http://docs.fabfile.org/en/2.0/api/exceptions.html
    似乎只有在执行group命令时会出现
'''

########################################################################################################################
''' 文件传输
    preserve_mode=True, 保留mode
'''

if 0:
    src = '/etc/hosts'
    dst = "/home/long/hosts"
    c = Connection(host=config['host'])

    c.run("rm " + dst + " -rf")

    result = c.put(src, remote=dst)
    print("Uploaded {0.local} to {0.remote}".format(result))

    c.run("cat " + dst)

########################################################################################################################
# from fabric.colors import *
# print(green('success'))


########################################################################################################################
''' sudo
    http://docs.fabfile.org/en/2.0/getting-started.html#the-sudo-helper
'''