# coding=utf-8

''' http://docs.fabfile.org/en/2.0/cli.html
    http://docs.pyinvoke.org/en/latest/invoke.html#inv
'''
from invoke import task

''' fab --list
    fab -H 192.168.10.112 hello

    临时命令：fab -H 192.168.10.112 -- uname -a
'''
@task
def hello(c):
    c.run("hostname")
