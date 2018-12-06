from invoke import task

@task
def build(c):
    c.run("sphinx-build docs docs/_build")

@task
def clean(c):
    c.run("rm -rf docs/_build")