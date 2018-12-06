# coding=utf-8

from invoke import task

''' 用于像invoke一行，将命令导出到命令行去
'''
@task
def hello(c):
    c.run("hostname")
