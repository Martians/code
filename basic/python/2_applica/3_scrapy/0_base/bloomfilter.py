# coding=utf-8

# https://media.readthedocs.org/pdf/pybloomfiltermmap3/latest/pybloomfiltermmap3.pdf

# Python 2 (linux)
#   git clone https://github.com/axiak/pybloomfiltermmap.git
#   python setup.py install

# Python 3
#   pip install pybloomfilter      # 运行后显示，缺乏c编译器，不可用
#   pip install bloom-filter==1.3


# pip install pybloomfilter (可能运行时会crash）

'''
import pybloomfilter
fruit = pybloomfilter.BloomFilter(100000, 0.1, '/tmp/words.bloom')
fruit.update(('apple', 'pear', 'orange', 'apple'))

print(len(fruit))
'''


# pip install lxml pybloomfiltermmap
# pybloom-live：
#   https://github.com/joseph-fox/python-bloomfilter
