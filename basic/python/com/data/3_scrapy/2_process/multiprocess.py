#coding=utf-8

'''
    方案：数据库记录网址信息，本地多进程拉取, 第三课
    参考：https://github.com/hezhen/spider-course-4/multi-process

## Install
    pip install ConfigParser
    ~/.pyenv/versions/3.6.5/lib/python3.6$ cp configparser.py ConfigParser.py
    pip install mysql-connector

## Question
    1. 下载过程中，发生的编码问题等
    2. 遇到下载失败，该如何处理？需要重试避免信息遗漏
    3. 如何试探一个网站允许的，单ip最大并发数、拉取频率；有怎样的试探策略
'''

import hashlib
import os
import threading
from dbmanager import DBConnector
from lxml import etree
import time
import urllib3

'''
## 程序说明
    1. 多进程多线程同时开始拉取，数据库层面自动确保了冲突解决
    2. 不支持完全的断点续传
            如果处于downloading状态，那么重启后不会再次执行

    3. 已经拉取过的网站，数据库会自动记录下来，就不需要专门的bloomfilter了

    4. 程序默认启动时，清理了所有数据，需要注释后才能多进程同时使用

## 真正逻辑（对每个线程一致）
    1. 获取任务url，包括处于downlading状态、但是已经超时的；或者处于new状态的
    2. 设置任务状态为downloading，并记录一个超时时间
    3. 开始任务下载，并解析子网址，将子网址加入到数据库中
    4. 该任务全部完成后，设置任务状态为done
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

    max_level = 3
    dir_name = "download"

    if not os.path.exists(dir_name):
        os.mkdir(dir_name)


def file_path(url):
    return "{0}/{1}{2}".format(crawler.dir_name, url[7:].replace('/', '_'), "" if url.endswith(".html") else ".html")
''' 下载当前页面并解析，将获得的url放入到crawler中
'''
def get_page(url, index, depth):

    try:
        path = file_path(url)
        print("try to download [{0}] - {1}".format(depth, url))
        http = urllib3.PoolManager()
        response = http.request("GET", url, headers = crawler.request_headers)
        page = response.data

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

    ''' 这里限制depth
    '''
    if depth > crawler.max_level:
        print("url [{}], exceed depth {}".format(url, depth))
    else:
        parse_page(url, index, depth, page)

    dbmanager.fininsh(index)
    return


def parse_page(url, index, depth, page):

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

                if val.startswith(dest_url) is False:
                    continue
                if val[-1] == '/':
                    val = val[0:-1]

                dbmanager.enqueue(val, depth + 1)
        except ValueError as err:
            print("parse {}, err: {}".format(url, err))
            return

total_thread = 3
threads = []
start_time = time.time()

crawler = Crawling()
dbmanager = DBConnector(total_thread)


dbmanager.enqueue(dest_url, 0)
task = dbmanager.dequeue()
get_page(task['url'], task['index'], task['depth'])

CRAWL_DELAY = 0.6

while True:
    task = dbmanager.dequeue()
    if task is None:
        for t in threads:
            t.join()
        break

    while True:
        for t in threads:
            if not t.is_alive():
                threads.remove(t)

        if len(threads) >= total_thread:
            time.sleep(0.6)
            continue
        try:
            t = threading.Thread(target=get_page, name=None,
                args=(task['url'], task['index'], task['depth']))
            t.setDaemon(True)
            t.start()
            threads.append(t)
            time.sleep(CRAWL_DELAY)
            break
        except:
            print("fail to start thread")
            exit(0)


















