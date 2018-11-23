#coding=utf-8

'''
    方案：使用数据库记录信息，master协调多个client, 第五课、第六课
    参考：https://github.com/hezhen/spider-course-4/multi-process

## Install
    sudo pip install redis
    sudo pip install mongo

## Server
    docker run --name mongo -p 27017:27017 -d mongo
    docker run --name redis -h redis -d -p 6379:6379 redis

## Question
    比较完整的构建方案是哪些？
    这里边mysql如何起作用

    client启动后init(), 将mogondb数据清理？多client如何操作
'''