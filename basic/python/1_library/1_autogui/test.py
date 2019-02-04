import pyperclip
import pyautogui

""" http://hugit.app/posts/doc-pyautogui.html
    https://pyautogui.readthedocs.io/en/latest/install.html
"""
def paste(foo):
    pyperclip.copy(foo)
    pyautogui.hotkey('ctrl', 'v')

foo = u'学而时习之'
#  移动到文本框
pyautogui.click(130,30)
paste(foo)