# coding=utf-8
'''

# install
    sudo pip install redis
    https://pypi.org/project/redis/

# Note:
    特别注意，文件不能起名为redis，否则import失败！！！！


'''

import redis

config = {
    'host': '192.168.10.7'
}

def simple():
    r = redis.Redis(host=config['host'])
    r.set("a", '111')
    print(r.get('a'))

''' pipline 和 mutli、exec是一样的，具有实务性
'''
def pooling():
    pool = redis.ConnectionPool(host=config['host'], decode_responses=True)
    r = redis.Redis(connection_pool=pool)
    r.set("a", '222')
    print(r.get('a'))

    pipe = r.pipeline(transaction=True)
    pipe.hset("11", "22", "111")
    pipe.hset("11", "11", "000")
    pipe.execute()
    print(r.hgetall("11"))

    # 可以可以直接写在一行
    pipe.set('hello','redis').sadd('faz','baz').incr('num').execute()


def subscrib():
    pool = redis.ConnectionPool(host=config['host'], decode_responses=True)
    r = redis.Redis(connection_pool=pool)

    chan = r.pubsub()
    chan.subscribe("11")

    # 每次订阅，会先收到一个消息
    msg = chan.parse_response()
    print(msg)

    r.publish("11", " -- notify message")
    msg = chan.parse_response()
    print("message: " + msg[2])


subscrib()