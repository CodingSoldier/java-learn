# -*- encoding=utf-8 -*-

import  struct

# 8个字节
bin_str = b'ABCD1234'
print(bin_str)
# 8个字节转换为大端字节序整数ASC码，有8个字节所以有8个B
result = struct.unpack('>BBBBBBBB', bin_str)
print(result)

# 两个字节转换为整数
result = struct.unpack('>HHHH', bin_str)
print(result)

# 4个字节转换为整数
result = struct.unpack('>LL', bin_str)
print(result)

# 转8个asc码字符
result = struct.unpack('>8s', bin_str)
print(result)

# 混合使用
result = struct.unpack('>BBHL', bin_str)
print(result)




























