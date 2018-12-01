# coding=utf-8

from invoke import task

@task
def hello(c):
    c.run("hostname")
