import urllib2


def download(url, user_agent='wswp', num_retires=2):
    print "download: ", url
    headers = {'User-agent': user_agent }
    request = urllib2.Request(url, headers=headers)
    try:
        html = urllib2.urlopen(request).read()
    except urllib2.URLError as e:
        print 'download error', e.reason
        html = None
        if num_retires > 0:
            if hasattr(e, 'code') and 500 <= e.code < 600:
                return download(url, user_agent, num_retires - 1)
    return html


print download("http://www.sina.com")

print download("http://www.meetup.com")