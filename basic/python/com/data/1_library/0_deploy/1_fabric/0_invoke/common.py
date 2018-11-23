
''' 该文件用于作为第三方文件存在，由 tasks.py 导入形成新的名字空间
'''
from invoke import task, call, Collection

@task(help={'name': "name of the build"})
def build(c, name):
    print("build with name "+ name)
    c.run("ls /etc/hosts")

@task
def build2(c, name):
    print("build with name "+ name)
    c.run("ls /etc/hosts")