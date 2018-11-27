# coding=utf-8

import paramiko

config = {
    'host': '192.168.0.80',
    'port': 22,
    'user': 'long',
    'pass': '111111'
}

command = 'df -h'

''' 执行远程命令
'''
ssh = paramiko.SSHClient()

''' ssh连接并执行命令
    传输文件
'''
# 允许链接不在本地 known_hosts 中的主机
ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy)
ssh.connect(hostname=config['host'], port=config['port'],
            username=config['user'], password=config['pass'])

stdin, stdout, stderr = ssh.exec_command(command)
print(stdout.read().decode())

stdin, stdout, stderr = ssh.exec_command("ls")
print(stdout.read().decode())

ssh.close()

''' 执行sftp
'''
src = '/etc/hosts'
dst = "/home/long/hosts"

transport = paramiko.Transport((config['host'], config['port']))
transport.connect(username=config['user'], password=config['pass'])

sftp = paramiko.SFTPClient.from_transport(transport)
sftp.put(src, dst)
sftp.get(dst, "/tmp/pp.txt")


''' 使用已有的链接执行命令
'''
command = "ls"
ssh = paramiko.SSHClient()
ssh._transport = transport

stdin, stdout, stderr = ssh.exec_command(command)
print(stdout.read().decode())

stdin, stdout, stderr = ssh.exec_command(command)
print(stdout.read().decode())

transport.close()