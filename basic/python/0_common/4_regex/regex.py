# coding=utf-8

import re;
# Python核心编程，第三版第一章

''' asc字符和正则表达式的特殊字符存在冲突
        \b是退格符，同时也表示正则表达式钟登额特殊符号（单词边界）；因此在正则中要用 \\b
        为了消除影响，简化操作，使用原始字符串 r''
'''

''' re.match  必须从头匹配，因此用的不多；匹配的意思是更严格，需要创建一个表达式来匹配整个行
    re.search 可以从任何位置匹配；可以指定start、end
    flag: 
'''
def match(match, string):
    m = re.search(match, string)
    if m is not None:
        print string + " - [" + match + "], result: [" + m.group() + "]"
    else:
        print "---- " + string + " - [ " + match + "]"
    return m

#######################################################################################
''' 正则表达式对象、正则匹配对象，12 P
    使用预编译对象：使用预编译的代码对象，比直接使用字符串要快; 他们名称相同
'''
print "\n============ compile and object"

# 已经预编译
#     使用时不需要编译，就是用的是方法
ob = re.compile("foo")
print ob.match('foo').group()

# 需要编译
#     就是用模块的函数，内部会自动编译
print re.match('foo', 'foo').group()

#######################################################################################
''' . 匹配任一字符

频次匹配：
    ? 依附于前边的字符，0、1次
    + 依附于前边的字符，>= 1次
    * 依附于前边的字符，任意次数
    {}依附于前边的字符，指定次数
'''
# match()

''' 字符边界
    \b 匹配单词的边界
    \B 匹配非单词的边界
'''
print "\n============ boundary"
''' 注意，这里必须使用r包含，否则\b将被转义
'''
match(r'\bthe', "it is the man")
match(r"\bthe\b", "it is the man")
match(r"\Bthe", "it is match_theman")

''' 默认是贪婪匹配，引擎将试图吸收匹配该模式的尽可能多的字符
    ? 跟在闭包操作符之后，将要求正则表达式匹配尽可能少的次数
'''
s = "birth: 1983-11-09"
#   1) 全部匹配，用于后续比较
#      这里输出的是group, 后边输出的是group(1)
print re.search("\d+-\d+-\d+", s).group()
#   2) 贪婪匹配：
#           从左到右按顺序取值，.+会试图匹配尽可能多的字符
#           给剩下的模式保留足够的匹配即可：这里只要保留一个数字
print re.search(".+(\d+-\d+-\d+)", s).group(1)
#   3) 非贪婪匹配
print re.search(".+?(\d+-\d+-\d+)", s).group(1)
exit(1)

#######################################################################################
''' 字符集
    [0-9]、[a-Z]、[^\t\n] [^r-u]， 在[]中就不需要转义使用 \t\n了
    
特殊字符
    \w 代表字母数字 [0-9a-zA-Z_]
    \s 代表空格字符，空格、制表符、回车等
    \d 代表数字
    他们的大写，表示不匹配
'''

''' 分割字符串
        类似于string.split
        但这里可以使用正则语句
'''
print "\n============ split"
print re.split(":", "aa:bb:cc")

#######################################################################################
''' 使用圆括号指定分组
        两个不同的正则，来匹配同一个字符串  (abd|bcd)；而不是像[abc]这样，只能匹配单个字符
        对整个正则表达式是使用重复操作符（而不是仅针对单个字符使用）(abc)*，匹配字符串的重复多次出现
    提取匹配成功的字符串
'''
print "\n============ group"

# (\w+\.)? -> www.xxx.
match('\w+@(\w+\.)?\w+\.com', "nobody@www.xxx.com")

''' group返回的是完整匹配，放在一个匹配对象中
    group(n)返回的是某一个匹配
    groups返回的是全部子组的元组
'''
m = match("(\w\w\w)-(\d\d\d)", "abc-123")
print m.group(1) + ", " + m.group(2)
print m.groups()

# 这个将不会匹配，因为没有完整匹配，即使有部分子组匹配成功
match("(\w\w\w)(\d\d\d)", "abc-123")

''' findall，总是返回一个列表；每次匹配都成为一个元组
    finditer 使用遍历方式依次找到下一个匹配
'''
s = 'this and that, they and they'
print re.findall(r"(th\w+).*(th\w+)", s, re.I)

# Python 2中不支持？
# it = re.finditer(r"(th\w+)", s, re.I)
# print it.next()

''' sub、subn 进行替换
    subn 返回的是一个元组，元组第二项是替换的总数
'''
print re.sub("[ae]", "111", "a good day e!")
print re.subn("[ae]", "111", "a good day e!")

''' 引用分组，只用匹配的顺序
'''
print re.sub(r"(\d{1,2})-(\d{1,2})", r"\2,\1", "25-32")
#######################################################################################
''' 扩展表示
    ?i re.I、re.Ignore
    ?m re.M、re.MULTILINE
    ?x re.X、re.VERBOSE, 所有的空格、#将被忽略，除非使用转义字符
'''
print "\n============ extend"

# 不区分大小写
print re.findall(r'(?i)yes', "yes, Yes, YES!")

# 允许匹配模式中出现多个空字符，以便编写易读的正则表达式
#   [ ] 指出必须真的存在一个空字符
match(r'''(?x) (\d{3})  
    [ ]   
    (\d{3}  -)
    ''', "12, 888 111-88")

''' 匹配分组，?:
        但是不保留下来. 只是用来辅助定位
        第一个分组，匹配了网址前边所有的xxx. 
        只保留最后一个xxx.com
    search、findall在语义上有差异
'''
s = "www.work.google.com, www.gps.g.com"
#   1）使用group输出，会找到最大匹配组：result: [www.work.google.com]
#       不符合最初意思，需要使用？除非取消贪婪匹配！！！
match(r"(?:\w+\.)*(\w+\.com)", s)
#   2）使用findall，会尽可能的匹配所有选项：['google.com', 'g.com']
print re.findall(r"(?:\w+\.)*(\w+\.com)", s)

print "\n============ name group"
''' 给分组起名字
        此时可以对匹配对象，调用groupDict了
'''
m = match(r"(?P<first>\w+\.)(?P<last>\w+\.com)", "www.google.com")
print m.groupdict()

# 给子组起名，并在替换时使用匹配子组的内容
print re.sub(r"(?P<first>\w+\.)(?P<last>\w+\.com)", "\g<last> --", "www.google.com")

# 给子组起名，并在正则表达式中使用子组的内容
match(r"(?P<src>\w+) - (?P=src)\.", "www - www.com")

print "\n============ locate group"
''' 前视匹配: ?=  ?!
        辅助定位子组，但是其内容不包含在子组中
    后视匹配: ?<= ?<!
'''
#       1) 一串字符后，跟 last的, 后边再跟一个空格的，才算匹配
#          这里如果使用search，只会返回 [bb]
print re.findall(r"\w{2,}(?= last,) ", '''bb last, sbc , qq last,''')
#       2) 负前视匹配：一串字符后，不跟 last的才算匹配,
print re.findall(r"\w{2,}(?! last,) ", '''bb last, sbc , qq last,''')

''' 条件选择 (?(1)y|x)
    分组存在，就匹配y；不存在匹配x
'''
