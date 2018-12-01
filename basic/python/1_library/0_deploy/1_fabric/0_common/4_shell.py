
# coding=utf-8

from fabric import Connection

'''
    ## 小结
        1. 
        2. ad-hoc 查询
    
    ## 文档    
        http://docs.fabfile.org/en/latest/cli.html
        http://docs.pyinvoke.org/en/latest/invoke.html#inv

    ## 说明
        1. 命令执行文件，必须命名为 fabfile.py；会递归向上查找 fabfile文件
           http://docs.fabfile.org/en/latest/cli.html#seeking-loading-tasks

Todo：
    @task前缀：http://docs.fabfile.org/en/latest/api/tasks.html#

'''

#######################################################################################################################
''' 
    命令要求：
        1. 标注了 @task
        2. 第一个参数为connection
        
    常用命令
        fab --list
        
        fab -H 192.168.0.80 hello
        fab -H 192.168.0.80,192.168.0.81 hello
        
        fab -H 192.168.0.80 -- uname -a
'''