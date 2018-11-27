# coding=utf-8

from fabric import Connection, SerialGroup as Group, Config

conf_item = ['host', 'user', 'pass', 'port']

# 按照加入的顺序列出的host
host_array = []
host_index = {}
host_data = {}

def parse_hosts(debug=0):
    ''' 解析配置文件，并根据需要建立相关索引
        1. name、alias：作为yaml中项目的key存在，以及key的value
        2. host ip 添加为索引
        3. ip_last part：如果没有冲突时，自动添加为索引
    '''
    config = Config()
    for item in config.hosts:
        host = item.copy()
        for key in item:
            # 对不在 conf_item 中的项目，检查是否要添加内容
            #   获取第一项，即为 Key
            if key not in conf_item:
                # 如果key是一个独立的字符串，就将其作为host的名字，建立名字索引：host['name'] = key
                add_host_index(key, host)

                if item[key]:
                    # 名字索引后有值，就建立别名索引
                    # host['alia'] = item[key]
                    add_host_index(item[key], host)
                # 不需要在host中保留索引的信息
                del host[key]

        host_array.append(host)
        # 添加 ip index
        add_host_index(host['host'], host)

    add_host_iplast()

    if debug:
        # https://docs.python.org/3/library/pprint.html
        import pprint
        # pprint.pprint(host_array)
        pp = pprint.PrettyPrinter(indent=4)
        print("host list:")
        pp.pprint(host_array)

        print("\nhost dict:")
        pp.pprint(host_index)

def add_host_index(name, host):
    if name in host_index:
        print("name {} already regist, as: {}".format(name, host_index[name]))
        exit(-1)
    host_index[name] = host

def add_host_iplast():
    for host in host_array:
        ip = host['host'].split('.')[-1]
        if ip in host_index:
            print("add_host_ip_last, but last part: {} already registed", ip)
            return

    for host in host_array:
        ip = host['host'].split('.')[-1]
        add_host_index(ip, host)

parse_hosts(True)


def host_info(index):
    # 检查key作为字符串时是否存在
    #   1. ip 最后一位
    #   2. host
    #   3. name、alias
    if index in host_index:
        return host_index[index]

    # 检查是否作为index进行查找，其他类型的索引（包括 ip last 已经查找过了）
    else:
        # 本来应该传入数字，但是这里传入了字符串，转换为index
        if isinstance(index, str):
            if index.isdigit():
                index = int(index)

        if isinstance(index, int):
            if index >= 0 and index <= len(host_array):
                return host_array[index]
            else:
                print("host_info: index {} exceed host count: {}".format(index, len(host_array)))
                exit(-1)
        else:
            print("host_info: key {} not exist".format(index))
            exit(-1)


def get_host(data):
    ''' 建立到某个host的连接
            可以指定 ip最后一位、name
    '''
    host = host_info(data)

    if 'conn' not in host:
        user = host['user'] if 'user' in host else None
        port = host['port'] if 'port' in host else None
        kwarg = {'password': host['pass']} if 'pass' in host else None
        # print("try connect to {}".format(host['host']))
        host['conn'] = Connection(host=host['host'], user=user, port=port, connect_kwargs=kwarg)
    return host['conn']

###############################################################################################################
def test_host_info():
    # ip last
    print(host_info("10")['host'])
    print(host_info("113")['host'])

    # host name alias
    print(host_info("192.168.10.113")['host'])
    print(host_info("ubuntu")['host'])

    # index
    print(host_info(1)['host'])
    print(host_info('1')['host'])

    # wrong
    # print(host_info("109")['host'])

def test_get_host():
    get_host(0).run("hostname")
    get_host(1).run("hostname")

test_host_info()
test_get_host()