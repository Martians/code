# coding=utf-8

from invoke import task, Context
from fabric import *

# class Sedding:


#######################################################################################################################
if 1:
    ''' 环境准备
    '''

    class Test:
        test_invoke = True
        new_file = True

        default_config = "config_file"

        if test_invoke:
            c = Context()
        else:
            config = {'host': '192.168.0.80', 'port': 22, 'user': 'long', 'pass': '111111'}
            c = Connection(host=config['host'], user=config['user'], connect_kwargs={'password': config['pass']})

        def segment(self, info):
            print("\n##########################################################################")
            print("======================== {info}:".format(info=info))

        def asserts(self, info, value):
            ''' 不使用new_file, assert都不会成功
            '''
            if not self.new_file: return
            assert (info == value)

        def dump_change(self, file=None, clear=False, dump=True):

            file = file if file else self.default_config

            if dump:
                ''' 输出单行显示的
                '''
                command = 'grep'
                for key, line, search in test_save:
                    if line == 0:
                        command += " -e '{search}'".format(search=search)
                command += ' ' + file
                print("[dump change]: {command}".format(command=command))
                c.run("{command}".format(command=command), warn=True, hide=True)

                ''' 输出多行显示的：
                    1. 每一个单独设置要显示的行数
                    2. append 方式添加的，显示原来的key，以表示其相对位置
                '''
                for key, line, search in test_save:
                    if line != 0:
                        print("----------- {key}, pos: {pos}".format(key=key, pos=line))
                        c.run("cat {file} | grep -{type} {count} '{search}' "
                              .format(file=file, search=search, count=abs(line),
                                      type='A' if line > 0 else 'B'))
            if clear:
                test_save.clear()
#######################################################################################################################

