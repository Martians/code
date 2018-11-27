# coding=utf-8

'''
## Todo
    1. 每发送一个消息，就链接一次


## Question
    1. 发送消息，为什么要转为utf-8
    2. 为何不用 socket.connect
    3. 通常用什么框架，或者直接实现了该功能

## Check
    1. 检查类是否有某个属性

'''

import threading
import socket
import sys

class SocketClient:

    def __init__(self, host='127.0.0.1', port=10000):
        self.address = (host, port)

    def send(self, message):
        try:
            self.socket = socket.create_connection(self.address)
            print("connect to: {}".format(self.socket.getsockname()))

            self.socket.sendall(message.encode('utf-8'))
            data = self.socket.recv(1024)
            return data.decode('utf-8')

        except Exception as err:
            print("send message err: {}".format(err))
        finally:
            if hasattr(self, 'socket'):
                self.socket.close()

if __name__ == '__main__':
    client = SocketClient()
    resp = client.send("11223344")
    print("resp: {}".format(resp))