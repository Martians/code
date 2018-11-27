# coding=utf-8

''' 操作流程
## 整体
    1. 实际上还是不支持断点
        
## master
    1. master 进行批量穿行操作，批量写入到monggo
    
## client
    1. 从mongo中获取任务，从mongo 的 db.mfw 中一次读取一个
    2. 选择一个线程执行下载，下载完成后，修改刚获取的 db.mfw 的 row状态为done
    3. 解析网页，并网页中包含的下一级网址，写入到 mongo 的 db.mfw
    4. 额外存储一份信息，将从该网页获得的其他网址，放在一个记录中，db.urlpr，仅用于查看
    
    
    1. client向master发送注册消息，获得自己的client id
    2. client定期向master发送heartbeat消息，返回消息中包括了server的状态
        发现master已经关闭了
'''

''' 数据库使用
## mongo与redis的关系
    redis 检查 url是否访问过，mongo进行持久化记录
    
## Redis
    1. 只有client会连接上来，server不需要关注
    2. 提供一个read cache层，确保本次启动不会有重复的 url被插入到 Mongo中
            重复的网址很多，不断的读取mongon会导致其性能下降
            真正的获取网址，然后进行下载，这个数量相对稳定
    
## Mongo
    1. 数据库模式
            对db.mfw 这个table，对其status建立索引，这样获取相应status的数据就很快
            这里做了一个优化，将_id只是设置为url的hash，而没有使用默认的 objectid
            大大减少空间占用；缺点是有一个的冲突风险
    2. 插入：解析网页过程中，插入要下载的：
            先检查redis中是否已经记录了这个url，提升性能
    3. 获取：
            使用了原子操作，获取db.mfw中第一个状态为new的，并设置状态为downloading
    4. 完成：
    
## 问题
    1. 如果redis发生重启，数据丢失，那么很多url还是会被重复插入到 mongo中
            其状态会被改成 new
'''