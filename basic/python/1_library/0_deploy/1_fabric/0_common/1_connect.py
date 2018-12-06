# coding=utf-8
import getpass

from fabric import Connection, Config
from invoke import Responder

'''
    ## 小结
        1. 配置 connect 参数（用户、密码等）的方式
        2. sudo 的执行方式
     
    ## ssh认证
        1. http://docs.fabfile.org/en/latest/concepts/authentication.html
        
            Private key files: 
                connect_kwargs：key_filename 
                shell：--identity
                ssh_config 中指定 IdentityFile 
            
            passphrases
                connect_kwargs.passphrase
                shell：--prompt-for-passphrase
            
            Private key objects:
                connect_kwargs.pkey
            
            Passwords
                connect_kwargs.password 
                shell: --prompt-for-login-password
            
            SSH agents
            GSSAPI
            
            http://docs.fabfile.org/en/latest/api/connection.html
    
        2. fabric ssh配置、访问ssh_config
            http://docs.fabfile.org/en/latest/concepts/configuration.html#loading-and-using-ssh-config-files

'''


#######################################################################################################################
''' 脚本准备
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
''' 
    登陆配置：
     
        1. 配置文件（/etc/fabric.yaml、~/fabric.yaml）
            user、port
            connect_kwargs：将用于填充Connection中的connect_kwargs，提供默认参数
            inline_ssh_env、load_ssh_configs
            
            默认配置：http://docs.fabfile.org/en/latest/concepts/configuration.html#new-default-values-defined-by-fabric
            
        2. ssh file
            http://docs.fabfile.org/en/latest/concepts/configuration.html#loading-and-using-ssh-config-files
        
        3. 函数参数
            1. 普通参数：host、user、connect_timeout
            
            2. connect_kwargs:      见paramiko，http://docs.paramiko.org/en/latest/api/client.html#paramiko.client.SSHClient.connect
                inline_ssh_env
                password
                
            3. config 变量：
                默认传入的是匿名config对象，空对象？ 
                1）http://docs.fabfile.org/en/latest/api/config.html#fabric.config.Config
                2）invoke：http://docs.pyinvoke.org/en/latest/api/config.html#invoke.config.Config
                
        4. shell执行
            1. -H, --host
            2. 
            
    ## 使用
        1. 默认不需要主动 close
        2. 其他可配置选项
                配置超时间，能否在配置文件中设置默认值？
'''

#######################################################################################################################
''' 连接测试
    1. 环境准备：
        1）fabric.yaml中，默认的user是root
        2）connect_kwargs：配置了passwd
        3）复制 fabric.yaml 到 ~/
'''
if 1:
    ''' 1. 使用ssh连接字符串
    '''
    connstr = "{}@{}:{}".format(config['user'], config['host'], config['port'])
    Connection(connstr).run(command)
    Connection(host=connstr).run(command)

    ''' 2. 使用分离的参数配置
    '''
    Connection(host=config['host'], user=config['user']).run(command)

    ''' 3. 用connect_kwargs，覆盖更多选项
    '''
    Connection(host=config['host'], user=config['user'],
               connect_kwargs={'password': config['pass']}).run(command)
    connect().run(command)

    ''' 
        主动关闭, 通常不需要
    '''
    # with Connection('host') as c:
    #     c.run('command')
    #     c.put('file')

#######################################################################################################################
'''
    父类功能：继承：invoke.Context，展示更多功能
    http://docs.pyinvoke.org/en/latest/api/context.html#invoke.context.Context  
'''

#######################################################################################################################
''' sudo
    http://docs.fabfile.org/en/latest/getting-started.html#superuser-privileges-via-auto-response
    
    1. 准备工作：
        新建用户并加入到sudo组：useradd -G sudo test; sudo passwd test
'''
if 1:
    # 更新用户为 test
    kwargs = {'user': 'test', 'connect_kwargs': {'password': config['pass']}, 'connect_timeout': 1}

    # ------------------------------------------------------------------
    ''' 方式1：直接执行，会提示需要输入密码
    '''
    c = connect().run("sudo netstat", pty=True)

    # ------------------------------------------------------------------
    ''' 方式2：捕获提示信息，自动进行回应
            实际的提示是：[sudo] password for test: 
    '''
    sudopass = Responder(pattern=r'\[sudo\] password.*:', response=config['pass'] + '\n')
    connect().run('sudo whoami', pty=True, watchers=[sudopass])

    # ------------------------------------------------------------------
    ''' 方式3：使用connection.sudo命令
    '''
    print("\nuse connection.sudo：")
    mode = 0
    if mode:
        ''' 通过用户手动输入配置，传入到config中
                自行设置提示语
        '''
        sudo_pass = getpass.getpass("What's your sudo password?")
        config = Config(overrides={'sudo': {'password': sudo_pass}})
        connect(config=config).sudo('whoami', hide='stderr')

    else:
        ''' 通过在配置文件中配置：sudo: password: '111111'
        '''
        connect().sudo('whoami')