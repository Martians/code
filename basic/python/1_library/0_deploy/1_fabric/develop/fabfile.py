# coding=utf-8

''' http://docs.fabfile.org/en/2.0/cli.html
    http://docs.pyinvoke.org/en/latest/invoke.html#inv
'''
from invoke import task
from hosts import *

@task
def hello(c):
    c.run("hostname")

@task
def work(c, name):
    get_host(0).run("hostname; echo " + name)