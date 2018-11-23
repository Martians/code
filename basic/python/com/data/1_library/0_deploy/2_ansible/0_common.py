#coding=utf-8

'''
    中文：http://www.ansible.com.cn/docs/intro_getting_started.html#gs-about
    官网：https://docs.ansible.com/ansible/latest/user_guide/intro_getting_started.html
    例子：https://github.com/ansible/ansible-examples

功能
    1. 不同的组件完成不同的事情，对常用的软件定制了 module
        shell、yum、git、service、在一个时间范围内运行后台进程

    2. 自动化部署、参看书上介绍

各组件功能：
操作都记录在远端的syslog中，log_path，除非task标记为 no_log: True
Ansible Tower：提供日志管理功能

ansible-pull：定期提供pull功能，而不需要
Galaxy：共享平台

3， 应对负载的构建
配置文件分开存放，并可以以目录形式；将不同的数据中心、服务类型隔开

都是幂等操作

以配置文件的形式写出代码


实现
    1. 默认使用 ControlPersist，更高效的ssh方案; 如果不支持，会退化到 paramiko

Todo：
    1. ssh各种验证方式，Kerberos；配置密码方式登录
    2. 如何与docker结合
    查看默认配置是怎样
'''

########################################################################################################################
''' 配置案例

    ssh-copy-id root@192.168.10.111
    ssh-copy-id root@192.168.10.112
    ssh-copy-id root@192.168.10.113

    sudo mkdir /etc/ansible/
    sudo sh -c 'echo "[cluster]
host1 ansible_host=192.168.10.111
host2 ansible_host=192.168.10.112
host3 ansible_host=192.168.10.113

[cluster:vars]
ansible_ssh_pass = 111111
ansible_user = root" > /etc/ansible/hosts'

    sudo apt-get install sshpass

    sudo chown long:long /etc/ansible/hosts
    sudo sh -c 'echo "
[defaults]
host_key_checking = False
remote_user=root" > /etc/ansible/ansible.cfg'

    ## 设置ssh代理，防止重新输入密码
    ssh-agent bash
    ssh-add ~/.ssh/id_rsa
'''

########################################################################################################################
''' 常用命令

    ansible all -m ping  -C  # 只检查
    ansible all -a "/bin/echo hello"

    ansible cluster --list-hosts
    ansible cluster -m shell -a 'echo $PPID'

    ansible-config view -v
    ansible-config dump --only-changed            # 查看所有配置，或者只是更改的

    ansible-playbook test.yml --list-hosts

命令说明
    1. --sudo：是否使用sudo，用--becoome替换；--sudo-user：指定root之外的另一个用户，用--become-user替换

'''

########################################################################################################################
''' 配置：方便用git管理起来
    1. 软件配置
        https://docs.ansible.com/ansible/latest/reference_appendices/config.html#environment-variables    默认选项、环境变量
            默认：https://raw.githubusercontent.com/ansible/ansible/devel/examples/ansible.cfg
            索引：https://docs.ansible.com/ansible/latest/reference_appendices/config.html#ansible-configuration-settings-locations

        常用选项：
            remote_user

    2. host配置，即 inventory
        https://docs.ansible.com/ansible/latest/user_guide/intro_inventory.html
            ini 方式、yaml方式
            常用的选项：ansible_user、ansible_ssh_pass等，相当于在host层面进行设置
            与docker的结合

        1. 使用别名：
        2. 自动生成项目：www[01:50].example.com
        3. 变量设置：vars - host变量、group变量，多级别设置
                变量尽量设置在其他位置 /etc/ansible/group_vars/group_name、/etc/ansible/host_vars/host_name
        4. 层级关系：children - 树状结构；在ini中构建树状结果比较奇怪 [atlanta:vars]，需要单独建个session存放key value
        5. 分组方式：默认分组 all、ungrouped（所有未在分组的host）

    3. 变量设置
        1. host变量、group变量
        3. 可以存放在 playbook 目录，invetnroy目录

    3. 辅助检查错误，http://www.yamllint.com/


仓库：



变量：https://docs.ansible.com/ansible/latest/user_guide/playbooks_variables.html




'''

########################################################################################################################
''' ssh 登陆
ssh-agent，将秘钥都交给它管理，是ssh代理程序
如何使用：

--private-key, --key-file
--vault-password-file

    1. inventory中：/etc/ansible/hosts 配置group变量，设置 ansible_user、ansible_ssh_pass
            group设置，
            ansible_ssh_private_key_file

    2. config 中：ansible.cfg 配置默认用户 remote_user
            全局设置

连接速度上是否有影响


'''

########################################################################################################################
''' shell module
模块列表：https://docs.ansible.com/ansible/latest/modules/list_of_all_modules.html#all-modules
'''

########################################################################################################################
''' shell module
使用单引号将不会
ansible cluster -m shell -u root -a 'echo $PPID'


shell 和 command的区别


'''

########################################################################################################################
''' shell module

文件复制
ansible cluster -m copy -u root -a 'src=/etc/hosts dest=/tmp/hosts'

文件属性,mkdir -p的功能
ansible webservers -m file -a "dest=/srv/foo/b.txt mode=600 owner=mdehaan group=mdehaan"
ansible all -m user -a "name=foo password=<crypted password here>"

'''

########################################################################################################################
''' playbook
yaml语法：https://docs.ansible.com/ansible/latest/reference_appendices/YAMLSyntax.html#yaml-syntax

执行时使用 -v，查看执行过程


https://docs.ansible.com/ansible/latest/user_guide/playbooks_intro.html

不同host执行不同命令，执行负载的程序1
command and shell，可以不适用 k=v 形式给出参数

有序执行、同步异步

如果需要执行sudo，并执行时卡主，那就需要--ask-become-pass

可以对host的执行顺序进行调整，乱序、倒序等
失败的任务，只要全部重新执行即可

handlers:
    notify中；在发生改动时执行，只执行一次
    可以定义一个listen字段，由task notify中设置listen的名字，可以将handler和name解耦

-f 10

ansible-playbook test.yml --list-hosts

https://docs.ansible.com/ansible/latest/user_guide/playbooks_reuse_includes.html

import static：直接立即执行所有命令，相当于当做是两个文件拼接在一起
include dynamic：跟随task一起执行，相当于函数；经常用于执行循环
    不能触发 来自于 include 的 handler
    不能断点方式，执行一个include的task


变量的使用：
    使用 Jinja2 模板


    系统信息收集：ansible hostname -m setup
    系统 facts 收集

local fact
https://docs.ansible.com/ansible/latest/user_guide/playbooks_variables.html#local-facts-facts-d

/etc/ansible/facts.d/*.fact
怎么使用本地变量？


mkdir -p /etc/ansible/facts.d/
echo "[general]
asdf = 1
bar = 1" >> /etc/ansible/facts.d/file1.fact
ansible host1 -m setup -a "filter=ansible_local"

保存fact，可以加快执行速度

系统自定义变量
ansible_play_hosts、inventory_dir

在命令行、新的文件中，指定各种变量传入

handlers：event system（事件系统）,可以响应这种改动.
常用于重启服务


'''


-c指令


















