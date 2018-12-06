from invoke import task

@task
def release(c):
    c.run("python setup.py sdist register upload")