# coding=utf-8

from fabric import Connection


########################################################################################################################
''' 
    1. 文档：
            http://docs.fabfile.org/en/latest/api/transfer.html
            
    2. 参数：
            1. 路径不用使用 ~，使用绝对路径（某些sftp server不支持这些）
            2. 某些不填的参数，将使用当前路径 os.getcwd
            3. preserve_mode=True，保留文件属性
'''

#######################################################################################################################
''' 环境变量
'''
config = {
    'host': '192.168.0.80',
    'port': 22,
    'user': 'long',
    'pass': '111111'
}
command = "uname -a"

if 1:
    kwargs = {'user': config['user'], 'connect_kwargs': {'password': config['pass']}, 'connect_timeout': 1}

    def connect(host=config['host'], **args):
        return Connection(host, **kwargs, **args)

    def show_info(c):
        return c.run(command)

#######################################################################################################################
if 1:
    src = '/etc/hosts'
    dst = "/home/long/test_hosts1"
    fil = "/home/long/test_hosts2"
    c = connect()

    c.run("rm " + dst + " -rf")

    result = c.put(src, remote=dst)
    print("Uploaded {0.local} to {0.remote}".format(result))

    result = c.get(dst, local=fil)
    c.run("rm %s -rf" % dst)
    c.run("rm %s -rf" % fil)