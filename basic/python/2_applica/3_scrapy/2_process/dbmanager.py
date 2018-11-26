#coding=utf-8

'''
## Todo：
    1. python使用规范中，哪些地方要加try、catch
    2. 对except的判断，是否有最终基类？
    3. mysql库的详细使用

## server
    docker exec -it mariadb mysql -uroot -p111111
    CREATE DATABASE IF NOT EXISTS crawl;
'''
import hashlib

import mysql.connector
from   mysql.connector import errorcode

'''
    1. 数据库每次都是打开再关闭
            conn以及cursor是否是线程安全？
            这里使用了命名连接池，因此不会真的重建连接

    2. 多进程里的多线程，同时访问数据库
            select for update会锁定，只有执行commit之后，才会解锁？
'''

class DBConnector:

    config = {
        "host": '192.168.10.91',
        "user": "long",
        "password": "111111",
        'charset': 'utf8',
        "database": "crawl",
    }

    TABLES = {}
    TABLES['urls'] = (
        "CREATE TABLE IF NOT EXISTS `urls` ("
        "  `index` int(11) NOT NULL AUTO_INCREMENT,"  # index of queue
        "  `url` varchar(512) NOT NULL,"
        "  `md5` varchar(32) NOT NULL,"
        "  `status` varchar(11) NOT NULL DEFAULT 'new',"  # could be new, downloading and finish
        "  `depth` int(11) NOT NULL,"
        "  `queue_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
        "  `done_time` timestamp NOT NULL DEFAULT 0 ON UPDATE CURRENT_TIMESTAMP,"
        "  PRIMARY KEY (`index`),"
        "  UNIQUE KEY `md5` (`md5`)"
        ") ENGINE=InnoDB")

    def __init__(self, local_thread):
        self.local_thread = local_thread

        ''' 1. config中就设置database；但这样要先在外边把数据库创建好
            2. 后续再进行连接database；选择方案2
        '''
        try:
            temp = self.config.copy()
            temp.pop("database")
            # cnx = mysql.connector.connect(host=self.host, user=self.user, passwd=self.passwd)
            cnx = mysql.connector.connect(**temp)
        except mysql.connector.Error as err:
            print("-- connect db failed: {}".format(err))
            exit(1)

        cursor = cnx.cursor()

        ''' 创建database、table
        '''
        try:
            cursor.execute("CREATE DATABASE IF NOT EXISTS {} "
                           "DEFAULT CHARACTER SET 'utf8'".format(self.config['database']))
            # 这相当于执行一个函数，修改当前对应的database；可能会产生异常
            cnx.database = self.config['database']

            for name, ddl in self.TABLES.items():
                cursor.execute(ddl)

            self.clear()
        except mysql.connector.Error as err:
            print("-- create db or table failed: {}".format(err))
            exit(1)
        finally:
            cursor.close()
            cnx.close()

    def clear(self):
        con = mysql.connector.connect(pool_name="my_pool",
            pool_size=self.local_thread, **self.config)
        cursor = con.cursor()
        try:
            cursor.execute("Truncate table urls")
        except mysql.connector.Error as err:
            print("clear err: {}".format(err))
            return
        finally:
            cursor.close()
            con.close()

    def enqueue(self, url, depth):
        con = mysql.connector.connect(pool_name="my_pool",
            pool_size=self.local_thread, **self.config)
        cursor = con.cursor()

        try:
            insert = ("INSERT INTO urls (url, md5, depth) VALUES (%s, %s, %s)")
            params = (url, hashlib.md5(url.encode('utf8')).hexdigest(), depth)
            cursor.execute(insert, params)
            con.commit()
        except mysql.connector.Error as err:
            #print("enqueue err: {}".format(err))
            return
        finally:
            cursor.close()
            con.close()

    def dequeue(self):
        con = mysql.connector.connect(pool_name="my_pool",
                                      pool_size=self.local_thread, **self.config)
        cursor = con.cursor(dictionary=True)
        try:
            ''' 方式1：select for udpate，需要加锁，是旧方法不建议使用
                方式2：check_code设置为一个随机值
            '''
            select = ("SELECT `index`, `url`, `depth` FROM urls WHERE status='new' ORDER BY `index` ASC LIMIT 1 FOR UPDATE")
            cursor.execute(select)
            if cursor.rowcount is 0:
                return None

            row = cursor.fetchone()
            update = ("UPDATE urls SET `status`='downloading' WHERE `index`=%d") % (row['index'])
            cursor.execute(update)

            con.commit()
            return row
        except mysql.connector.Error as err:
            print("dequeue err: {}".format(err))
            return
        finally:
            cursor.close()
            con.close()

    def fininsh(self, index):
        con = mysql.connector.connect(pool_name="my_pool",
                                      pool_size=self.local_thread, **self.config)
        cursor = con.cursor()
        try:
            update = ("UPDATE urls SET `status`='done' WHERE `index`=%d") % (index)
            cursor.execute(update)
            con.commit()
        except mysql.connector.Error as err:
            print("finish err: {}".format(err))
            return
        finally:
            cursor.close()
            con.close()