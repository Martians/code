# coding=utf-8

'''
    方案：本地记录网址信息, 第三课
    参考：https://github.com/hezhen/spider-course-4/multithread/multi_thread_mfw.py

## Todo:
    1. 快速得到request头部，直接转换为python格式
        1) chrome可以直接获取，但是不是json格式
        2) 使用postman：https://www.getpostman.com/apps

    2.

## Check：
    1. python没有==？
    2. 没有++
    3. 一些常量宏，放在那里
    4. deque 与list的区别
    5. byte类型、string类型的差异
    6. 是否有好的线程池方案，不用每次都新建线程
    7. 线程启动时的参数设置，必要参数有哪些
    8. 这种阵型方式并发度如何？等待网络过程中，多线程是否可以一起执行

## Install
    pip install Cython
    pip install pybloomfiltermmap3
    pip install urllib3
    pip install lxml

## Question
    1. 设置一个较大延时，否则容易造成封IP
            0.6s是一个合适的时间？
'''

import hashlib
import os
import threading
from collections import deque
from pybloomfilter import BloomFilter
from lxml import etree
import time
import urllib3

'''
## 程序逻辑
    1. 每次启动后，对访问过的url，从文件中读取其md5

    2. 内存中记录本次访问过的url
            urls_bloom：访问过的url，记录在bloomfilter中，确保只会enqueue、dequeue一次，这个信息不回写入文件
            down_bloom：访问过的url的md5，记录在bloomfilter中，并会写入到文件中，用于下次启动后读取

    3. 原有程序逻辑的问题（并不是完整的爬虫下载逻辑）
            即使这些网址被记录下来，也不一定被解析过，还是应该继续访问的
            history 和 history md5，只记录一个就行了；这里，history用来查看下载过的网址，history md5用来减少重复下载

    4. 并不支持断点方式
            每次启动后，需要在内存中，重建所有后续需要访问的url，每次都是从root网站开始完整下载的，记录在queue中
            本来就难以记录哪些网页已经完成了，数量较大，不可能在内存中建所有url的map

## 增加优化：
    1. 文件硬在本地了，就直接访问，不需要再次下载
    2. 将访问的网址，都限制在www.mafengwo.cn
'''

dest_url = "http://www.mafengwo.cn"
#dest_url = "http://www.sohu.com"

class Crawling:

    request_headers = {
        'host': dest_url[7:],
        'connection': "keep-alive",
        'cache-control': "no-cache",
        'upgrade-insecure-requests': "1",
        'user-agent': "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36",
        'accept': "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
        'accept-language': "zh-CN,en-US;q=0.8,en;q=0.6"
    }

    level = 0
    max_level = 3

    dir_name = "download"

    if not os.path.exists(dir_name):
        os.mkdir(dir_name)

    curr_queue = deque()
    next_queue = deque()

    urls_bloom = BloomFilter(1024 * 1024 * 16, 0.01)
    down_bloom = BloomFilter(1024 * 1024 * 16, 0.01)

    def __init__(self, url):
        self.curr_queue.append(url)

        # 只是记录那些已经下载下来了, 目前没有其他作用
        self.down_file = open(self.dir_name + "/history.txt", 'a+')
        # 记录已经下载下来的url的hash
        self.hash_file = open(self.dir_name + "/history_md5.txt", "a+")
        #   导入到bloomfilter中区，因此不会重复下载
        for md5 in self.hash_file.readlines():
            self.down_bloom.add(md5[:-2])

    def enqueue(self, url):
        ''' urls_bloom 中只记录hash，是为了减少记录在文件中的大小；实际也可以记录url？原有做法更准确?
        '''
        hash = hashlib.md5(url.encode('utf8')).hexdigest()
        if url not in self.urls_bloom and hash not in self.down_bloom:
            self.urls_bloom.add(url)
            self.next_queue.append(url)
            print("enqueue: [{}]".format(url))

    def dequeue(self):
        try:
            url = self.curr_queue.popleft()
            return url
        except IndexError:
            return None

    def complete(self, url):
        hash = hashlib.md5(url.encode('utf8')).hexdigest()
        self.down_bloom.add(hash)
        self.hash_file.write(hash + "\r\n")
        self.down_file.write(url + "\r\n")
        #self.down_file.flush()

    def next_level(self):
        self.curr_queue = self.next_queue
        self.next_queue = deque()

    def close(self):
        self.down_file.close()

def file_path(url):
    return "{0}/{1}{2}".format(crawler.dir_name, url[7:].replace('/', '_'), "" if url.endswith(".html") else ".html")
''' 下载当前页面并解析，将获得的url放入到crawler中
'''
def get_page(url):

    try:
        path = file_path(url)
        if os.path.exists(path):
            print("get local [{0}] - {1}".format(crawler.level, url))
            file = open(path, "rb")
            page = file.read()
        else:
            print("try to download [{0}] - {1}".format(crawler.level, url))
            http = urllib3.PoolManager()
            response = http.request("GET", url, headers = crawler.request_headers)
            page = response.data

            ''' 判断较长字符串，是否有较大开销
                1. if len(page) == 0:
                2. if page == b"":
            '''
            if page != b"":
                file = open(path, "wb+")
                file.write(page)
                file.close()
            else:
                print("-- zero length for [{}]".format(url))
                return
    except urllib3.exceptions as err:
        print("download {}, urllib3 err: {}".format(url, err))
        return
    except IOError as err:
        print("download {}, IOError err: {}".format(url, err))
        return
    except Exception as err:
        print("download {}, excpetion err: {}".format(url, err))
        return

    global total_download
    total_download += 1

    crawler.complete(url)
    parse_page(url, page)
    return


def parse_page(url, page):

    try:
        ''' page 是 byte类型
        '''
        html = etree.HTML(page.lower().decode('utf-8'))
        hrefs = html.xpath(u"//a")
    except Exception as err:
        print("length: {}, parse {}, err: {}".format(len(page), url, err))
        time.sleep(0.5)
        return

    for href in hrefs:
        try:
            if 'href' in href.attrib:
                val = href.attrib['href']
                if val.find('javascript') != -1:
                    continue
                if val.startswith("http://") is False:
                    if val.startswith('/'):
                        val = dest_url + val
                    else:
                        continue
                ''' 限制本地只抓取马蜂窝内部网站
                '''
                if val.startswith(dest_url) is False:
                    continue
                if val[-1] == '/':
                    val = val[0:-1]

                crawler.enqueue(val)
        except ValueError as err:
            print("parse {}, err: {}".format(url, err))
            return

crawler = Crawling(dest_url)
threads = []

total_thread = 10
total_download = 0
start_time = time.time()
''' 获取第一个page是同步的
'''
get_page(crawler.dequeue())

CRAWL_DELAY = 0.6
while True:
    ''' 结束条件
        1. 等待同一级都下载完成，才开始下一级
        2. 达到最大level、或者没有下一级就退出
    '''
    url = crawler.dequeue()
    if url is None:
        for t in threads:
            t.join()
        crawler.level += 1
        if crawler.level is crawler.max_level:
            break
        if len(crawler.next_queue) == 0:
            break
        crawler.next_level()
        continue

    ''' 每次获取了一个任务
        1. 用一个新的线程来执行，等待直到有线程可以执行
        2. 每次开启一个新的任务，都等待一个较小的时间；释放cpu？
    '''
    while True:
        for t in threads:
            if not t.is_alive():
                threads.remove(t)

        if len(threads) >= total_thread:
            time.sleep(0.6)
            continue
        try:
            t = threading.Thread(target=get_page, name=None, args=(url,))
            t.setDaemon(True)
            t.start()
            threads.append(t)
            time.sleep(CRAWL_DELAY)
            break
        except:
            print("fail to start thread")
            exit(0)

print("{0} page downloaded, cost time {1:.2f} seconds".format(total_download, time.time() - start_time))


















