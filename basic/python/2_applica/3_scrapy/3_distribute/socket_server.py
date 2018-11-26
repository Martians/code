#coding=utf-8

import threading
import socket
import sys

class SocketServer:

    def __init__(self, handle, host='0.0.0.0', port=10000):
        self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.handle = handle

        try:
            self.server.bind((host, port))
        except socket.error as err:
            print("sock bind err: {}".format(err))
            sys.exit(1)

        self.server.listen(10)

    def start_listen(self):
        print("start listen at: {}".format(self.server.getsockname()))
        while True:
            client, addr = self.server.accept()
            print("--> connect from: {}".format(addr))
            thread = threading.Thread(target=self.client_thread, args=(client,))
            thread.start()

    def client_thread(self, client):
        data = client.recv(1023)
        print("    recv: {}".format(data))
        resp = self.handle(data.decode('utf-8'))

        client.sendall(resp.encode("utf-8"))
        client.close()

    def start(self):
        thread = threading.Thread(target=self.start_listen)
        thread.start()

    def close(self):
        self.server.close()

#############################################################################################
def test_hadnele(message):
    # print("recv message: " +message)
    return "good"

import signal
import time
def exit_signal_handler(signal, frame):
    print("recv int signal")
    sys.exit(1)
    pass

if __name__ == '__main__':
    server = SocketServer(test_hadnele)
    server.start()

    signal.signal(signal.SIGINT, exit_signal_handler)
    signal.pause()
    server.close()
    sys.exit(1)

