# coding=utf-8

'''
    查看配置项是否存在、修改配置文件中的项目

额外说明：
    1. 使用 sed 时:
        http://www.cnblogs.com/edwardlost/archive/2010/09/17/1829145.html
        sed 所有情况下输出都是0，除非语法错误; 如果使用此方法，必须检查output

    2. 显示
        1）invoke 命令显示控制：run的参数 hide=True，因此默认输出是全部关闭的
        2）具体执行命令的参数： grep -q、sed -n

Todo:
    1. 函数中部分查询内容是固定的，灵活性欠佳；提供从外部传入完整 patten 的参数
'''

from invoke import task, Context
from fabric import *

# 要修改的、默认配置文件
default_config = "config_file"

# 是否真的修改配置文件，测试模式下不修改
test_just = None
test_save = None

########################################################################################################################
def filter_multi(value):
    '''
        如果是搜索多行，首先将\n替换为其他字符
        方法1：
            1. 多行情况下，grep得到整个文本数据，所以这里设置 show = 0
            2. a为标签；一次读取两行，替换其中的\n；只要发现\n就继续跳转到标签

        方法2：
            1. 代码：
                base = 'N; ' * value.count('\n') - 1
                command = "sed -n '{base} /{key}{sep}{value}/p' {file}".format(base=base, file=file, key=key, sep=sep, value=value.replace('\n', '\\n'))
            2. 但该方法比较脆弱，容易出错
                成功：sed -n 'N; N;  /data_file_directories: \n    - \/mnt\/disk1\n    - \/mnt\/disk2/p' tempfile
                失败：sed -n 'N; N; N; /data_file_directories: \n    - \/mnt\/disk1\n    - \/mnt\/disk2\n    - \/mnt\/disk3/p' tempfile
    '''
    convert = 'NNN'
    value = value.replace('\n', convert)
    filter = "| sed ':a; N; s/\\n/{convert}/g; ta;'".format(convert=convert)
    return value, filter

def grep_line(file, key=None):
    ''' 找到 key所在的行号
    '''
    if key:
        command = "sed -n '/{key}/=' {file}".format(file=file, key=key)
    else:
        # 找到最后一行的行号
        command = "sed -n '$=' {file}".format(file=file)

    result = c.run(command, warn=True, hide=True)
    print("grep_line: {command}, line: {index}".format(command=command, index=result.stdout))
    if len(result.stdout):
        if result.stdout.count('\n'):
            return int(result.stdout.split("\n")[0]), result
        else:
            return int(result.stdout), result
    else:
        return -1, None

def grep_data(c, key, file=None, value=None, sep=' ', show=0, mute=False, cache=None, prefix='[^#]*', suffix=''):
    '''
        检查 key [sep value] 在文件中是否存在
    参数：
        1. 定位：
            sep、prefix（默认：确保该行不是被注释掉的部分）、suffix

        2. 显示：
            1. show: grep时显示多少行数据，该数据用于后续分析等；
            2. mute：是否显示这些结果到屏幕，还是只保存在 result返回值中

        3. 其他
            1. 上下文：cache，使用 test_just，或者需要在一个内存数据中搜索时

    说明：
        1. grep：
            需要转义的字符：-
    '''
    file = file if file else default_config

    # 如果是多行
    if value and value.count('\n'):
        value, filter = filter_multi(value)
        tail = ''
        show = 0
    else:
        filter = ''
        tail = '$'

    ''' grep构造search语句
            1. 只查找，未被注释的选项，所以前缀prefix为 ^#
            2. {sep}*：可以出现多次sep的最后一个字符（如：[: ]，可以出现多次空格）
    '''
    data = '{sep}*{value}{tail}'.format(value=value, sep=sep, tail=tail) if value else ''
    search = '{prefix}{key}{suffix}{data}'.format(key=key, prefix=prefix, suffix=suffix, data=data)

    ''' 构造 option
            1. show：默认不显示任何数据
                  [show 0] grep [-q]; 已经设置了hide=True，该选项可以忽略；如果加上可以减少返回的数据量，实际作用不大
                  [show 1] 只显示匹配行
                  [show n] 显示上下n行
            2. 有时出现，把文本文件当做二进制文件，加上 -a 强制作为文本文件
    '''
    if show == 0:
        option = '-q'
    else:
        option = '' if show == 1 else "-C {show}".format(show=show)
    option += ' -a'

    command = "grep {option} '{search}'".format(option=option, search=search)
    # 在cache行中进行查询
    if cache:
        execute = "echo '{cache}' {filter} | {command}".format(cache=cache, command=command, filter=filter)
    else:
        # if filter:
        execute = "cat {file} {filter} | {command}".format(command=command, file=file, filter=filter)
        # else:
        #     execute = "{command} {file}".format(command=command, file=file, filter=filter)

    result = c.run(execute, warn=True, hide=True)
    print("[check exist]: {command} {file}, result: {result}"
          .format(command=command, file=file, result=result.ok))

    # 这里相当于执行invoke的命令时，使用 hide=False
    if show > 0 and not mute:
        print(result.stdout, end='')
    return result.ok, result

########################################################################################################################
@task
def update(c, key, value=None, file=None, sep=' ', show=0, prepare=True, check=True, prefix='', suffix=''):
    '''
    参数：
        1. 同 grep_data
        2. 检查
            1）prepare: 执行前检查对应的项目是否已经存在
            2）check：
    '''
    file = file if file else default_config

    ''' 检查item是否已经存在，并确保操作是幂等的
    '''
    if prepare:
        if grep_data(c, key=key, value=value, file=file, sep=sep, prefix=prefix, suffix=suffix)[0]:
            print("update, item [{key}{sep}{value}] already exist".format(key=key, value=value, sep=sep))
            return 2, None

    ''' 设置多种选项
    '''
    if test_just:
        option = ''
    else:
        option = '-i'

    ''' 可以影响显示的选项:
            1. sed -n "s/.*UsePAM.*/UsePAM yes/p" files/config_file
            2. c.run(command, hide=True)
    '''

    ''' sed中的语句构造
            1. {sep}*：可以出现多次sep的最后一个字符（如：[: ]，可以出现多次空格）
            2. {sep}*[.*]后边的部分，表示将key当前的value全部删除
    '''
    search = '{prefix}{key}{suffix}{sep}*.*'.format(key=key, prefix=prefix, suffix=suffix, sep=sep)

    if value:
        data = '{key}{sep}{value}'.format(key=key, value=value.replace('\n', '\\n'), sep=sep)
    else:
        data = '{key}'.format(key=key)

    command = 'sed {option} "s|{search}|{data}|" {file}'.\
        format(file=file, option=option, search=search, data=data)

    ''' 结果返回
            1. 可以直接在 sed ... | grep xxx
            2. 检查目标文件，或者上一次的output，然后 echo '' | grep xxx；使用此方式，grep_data 函数设计得更为通用一些
    '''
    result = c.run(command, warn=True, hide=True)
    print('[update]：{command}'.format(command=command))
    # print(result.stdout)

    # 这里仅用于保存 debug 信息
    if test_save:
        test_save.append((key, value.count('\n') if value else 0, search))

    # 检查执行结果：
    if check:
        # 如果是多行，直接进行检查，不匹配多个项目
        if value and value.count('\n'):
            show = 0
            if not grep_data(c, key=key, value=value, file=file, sep=sep, prefix=prefix, suffix=suffix,
                               cache=result.stdout if test_just else None)[0]:
                print("update, item [{key}{sep}{value}] not find"
                      .format(key=key, value=value, sep=sep))
                return -1, None

        # 检查是否只有一个项目匹配，以免同时改动多个项目
        else:
            # 如果是 test_mode，文件并未发生改变，所以不能直接 grep file
            watch = grep_data(c, key=key, value=value, file=file, sep=sep, prefix=prefix, suffix=suffix,
                                show=1, mute=True,
                                # -i 模式下，sed的输出为空，因此不能使用 result.stdout；必须重新从文件中读取；仅test_just模式下可用
                                cache=result.stdout if test_just else None)
            count = watch[1].stdout.count('\n')
            if count != 1:
                print("update, item [{key}{sep}{value}] ambiguous, count {count}, get:"
                      .format(key=key, value=value, sep=sep, count=count))
                print(watch[1].stdout)
                raise Exception("ambiguous {count}".format(count=count))

    # 如果需要，提取匹配行相关上下文输出
    if show > 0:
        grep_data(c, key=key, value=value, file=file, sep=sep, show=show,
                    cache=result.stdout if test_just else None)
    return result.ok, result

def delete(file=None, ):
    pass

def append(c, file=None, key=None, data=None, pos=0):
    '''
    参数：
        pos 是key的相对位置
            pos = 0，插入到下一行；key如果不存在，就插入到尾部
            pos = 1, 插入到下一行；key必须存在
            pos =-1，插入到上一行

    实现：
        通过行数来进行插入操作
    '''
    file = file if file else default_config

    # 对应的data 已经存在了
    index = grep_line(file, data)[0]
    if index != -1:
        # 调整参数，以免 pos = 0
        spos = 1 if pos == 0 else pos

        key_line = grep_line(file, key)[0]
        if key_line + spos == index:
            print("update: [{data}] already exist, line: {index}".format(data=data, index=index))
            return False, None
        elif key_line == -1:
            print("update: [{data}] already exist, line: {index}, key not exist".format(data=data, index=index))
            return False, None

    index = grep_line(file, key)[0]

    if index == -1:
        if pos == 0:
            # 没有指定相对位置，就加入到最后一行
            index = grep_line(file)[0]
        else:
            # 如果指定了pos， 则必须添加
            print("append, item [{key}] not find, can't append: {data}"
                  .format(key=key, data=data))
            raise Exception("append：not find {key}".format(key=key))
    else:
        index += pos
        if pos > 0:
            index -= 1

    ''' 设置多种选项
    '''
    if test_just:
        option = ''
    else:
        option = '-i'

    # 这里仅用于保存 debug 信息
    if test_save:
        test_save.append((key, pos, key))

    command = "sed {option} '{index}a\{data}' {file}".format(option=option, file=file, index=index, data=data)
    print("[append]: {command}".format(command=command))

    result = c.run(command, warn=True, hide=True)
    return result.ok, result


########################################################################################################################
''' 
    1. 使用不同的测试方式
        1. invoke 测试：使用 c = Context()
        2. fabric 测试：使用 c = Connection(
'''
# 测试方式 1）
# config = {
#     'host': 'localhost',
#     'port': 22,
#     'user': 'long',
#     'pass': '111111'
# }
# c = Connection(host=config['host'], user=config['user'], connect_kwargs={'password': config['pass']})
# import os
# with c.cd(os.getcwd()):
if 1:
    # 测试方式 2）
    c = Context()

    default_config = "config_file"

    # 生成内容，确保执行命令过程中，可以收集所有的key、value
    test_save = [(0, 0, '-n 0')]

    # 测试文件时是否每次生成新文件；
    #   1）使用新文件进行测试
    #   2）不生成新文件：可以检查是否是幂等操作
    # 注意测试完成后，将files/config_file内容复制出来
    new_file = True
    # new_file = False

    ##########################################################################
    # 测试中用到的函数

    def segment(info):
        print("\n##########################################################################")
        print("======================== {info}:".format(info=info))

    def asserts(info, value):
        # 不使用new_file, assert都不会成功
        if not new_file: return
        assert(info == value)

    def dump_change(c, file=None, clear=False, dump=True):
        ''' debug时显示所有修改的内容
        '''
        file = file if file else default_config

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
    ##########################################################################
    if 0:
        # 这里的测试不修改文件，只是内存中修改
        test_just = True

        # base
        if 1:
            segment("grep_data test")
            asserts(grep_data(c, key="UsePAM")[0], True)
            # print("show 1 line)
            asserts(grep_data(c, key="UsePAM", show=1)[0], True)
            # print("show 2 line)
            asserts(grep_data(c, key="UsePAM", show=2)[0], True)
            asserts(grep_data(c, key="UsePAM", value='no', show=2)[0], True)
            asserts(grep_data(c, key="UsePAM", value="yes")[0], False)

        # simple
        if 1:
            segment('update test')
            asserts(update(c, key="UsePAM", value='no')[0], 2)
            asserts(update(c, key="UsePAM", value='no', prepare=False)[0], True)

            # no output here
            # print("no output:")
            asserts(update(c, key="UsePAM", value='yes')[0], True)

            # print("output 1 line：")
            asserts(update(c, key="UsePAM", value='yes', show=1)[0], True)

            # print("output before and after：")
            asserts(update(c, key="UsePAM", value='yes', show=3)[0], True)

    ####################################################################################################################
    # actual
    if 1:

        if new_file:
            ''' 测试环境遇到的问题
                问题：使用vmware共享磁盘，sed修改文件时，文件的改动更新到磁盘，需要几秒时间；在该模式下，目标文件中末尾可能会被truncate；
                方案：将需要修改的文件，放在共享路径之外；执行完成后，再复制回来，便于测试检查结果
            '''
            temp_file = "~/tempfile"
            c.run("rm -rf {temp}".format(temp=temp_file))
            c.run("if [ ! -f {temp} ]; then cp {config} {temp}; fi".format(temp=temp_file, config=default_config))

            default_config = temp_file
            test_just = False

        if 1:
            # UsePAM no  --> UsePAM yes
            asserts(update(c, key="UsePAM", value='yes')[0], True)
            # cluster_name: 'Test Cluster'  --> cluster_name: my_cluster
            asserts(update(c, key="cluster_name", value='my_cluster', sep=': ', show=2)[0], True)

            # 特殊字符：- seeds: （-需要转义）
            #     - seeds: "127.0.0.1"   -->     - seeds: 192.168.10.1
            asserts(update(c, key="seeds", value='192.168.10.1', sep=': ', show=1)[0], True)

            # 多次出现：只有一个是匹配的，其他是注释
            #   listen_address: localhost --> listen_address: 192.168.10.1
            asserts(update(c, key="listen_address", value='192.168.10.1', sep=': ', show=1)[0], True)

            # key在行首，broadcast_rpc_address 和 rpc_address； key出现多次，只匹配在行首的
            #   rpc_address: localhost --> rpc_address: 192.168.10.11
            asserts(update(c, key='rpc_address', prefix="^", value='192.168.10.11', sep=': ', show=1)[0], True)

            # key在文件尾部，broadcast_rpc_address 和 rpc_address
            asserts(update(c, key='cc_address', prefix="^", value='192.168.10.11', sep=': ', show=1)[0], True)

            # 多行替换
            value = '''
    - /mnt/disk1
    - /mnt/disk2
    - /mnt/disk3'''
            asserts(update(c, key='data_file_directories', prefix=".*", value=value, sep=': ', show=0)[0], True)

        if 1:
            asserts(append(c, key='LOCAL_JMX=yes', data='bbc=3', pos=-1)[0], True)

            # append 在下边
            asserts(append(c, key='LOCAL_JMX=yes', data='LOCAL_JMX=no', pos=2)[0], True)
            # 再次添加，不会成功
            asserts(append(c, key='LOCAL_JMX=yes', data='LOCAL_JMX=no', pos=2)[0], False)

            # key不存在：插入到尾部
            asserts(append(c, key='LOCAL_JMX=111', data='not_find_insert')[0], True)

        if new_file:
            # 将共享目录之外的文件，复制进来，便于测试检查结果
            c.run("\cp {temp} temp_file -rf".format(temp=temp_file))

        dump_change(c)
        print("success")