# coding=utf-8

from fabric import Connection, Config

''' 

/etc/fabric.yml, ~/.fabric.json
    1. 配置文件
           fabric：http://docs.fabfile.org/en/latest/concepts/configuration.html#default-values
           invoke：http://docs.pyinvoke.org/en/latest/concepts/configuration.html#configuration
           
           文件：/etc/fabric.yml; ~/.fabric.py; ~/.tasks.yaml; ~/.fabric.json; 
           注意：不能读取当前目录中的 fabric.yml，必须复制到其他位置
           
           不支持针
                对单个host的配置：之前版本的功能 env.hosts、env.roles
                fabric希望，用户自己通过配置ssh config 文件的方式去使用额外功能
            
    2. 默认配置
            http://docs.fabfile.org/en/latest/concepts/configuration.html#new-default-values-defined-by-fabric
            
Todo：
    能否从工程当前位置，直接读取配置文件 fabric.yaml
    
'''

#######################################################################################################################
''' 
    1. 环境准备：配置文件复制到 /etc、~/
'''
if 1:
    print("\nglobal default:")
    print(Config.global_defaults())

    import json
    # json.dumps(Config.global_defaults())

    ''' 读取配置文件
            这个config会默认读取相关配置文件，可用于检查配置是否成功
    '''
    conf = Config()
    print("\nconnections config:")
    print(conf.connect_kwargs, conf.user)

    # 解析yaml，可以读取任何自定义配置
    print(conf.other)
