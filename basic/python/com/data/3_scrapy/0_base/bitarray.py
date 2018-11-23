

from bitarray import bitarray
import mmh3

url = "www.hao123.com"
offset = 2147483647 // 2^31 - 1
bit_array = bitarray(4*1024*1024*1024)
bit_array.setall(0)

# mmh3 hash value 32 bit signed int
# add offset to make it unsigned int 0 ~ 2^32-1
b1 = mmh3.hash(url, 42) + offset

bit_array[b1] = 1
