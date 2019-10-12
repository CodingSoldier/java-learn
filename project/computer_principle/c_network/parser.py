# -*- encoding=utf-8 -*-
import socket
import struct


class IPParser:
    IP_HEADER_LENGTH = 20

    @classmethod
    def parse_ip_header(cls, ip_header):
        """
        IP报文格式
        1、4位ip-version  4位ip头长度  4位服务类型  16位总长度
        2、16位标识符  3位标记位  3位片偏移
        3、8位TTL  8位协议  16位IP头校验和
        4、32位源ip地址
        5、32位目的ip地址
        """

        # 第一行比特流转为1字节整数、1字节整数、2字节整数
        line1 = struct.unpack('>BBH', ip_header[:4])
        # 取第一行第一个整数字节的前4个比特位。11110000 右移4位 1111
        ip_version = line1[0] >> 4
        #  11111111 比特流与15 00001111 结果是高4位为0低4位不变 00001111
        iph_length = line1[0] & 15 * 4
        pkg_length = line1[2]

        # 第三行
        line3 = struct.unpack('>BBH', ip_header[8:12])
        TTL = line3[0]
        protocol = line3[1]
        iph_checksum = line3[2]

        # 第4行
        line4 = struct.unpack('>4s', ip_header[12:16])
        src_ip = socket.inet_ntoa(line4[0])

        # 第5行
        line5 = struct.unpack('>4s', ip_header[16:20])
        dst_ip = socket.inet_ntoa(line5[0])

        return {
            'ip_version': ip_version,
            'iph_length': iph_length,
            'packet_length': pkg_length,
            'TTL': TTL,
            'protocol': protocol,
            'iph_checksum': iph_checksum,
            'src_ip': src_ip,
            'dst_ip': dst_ip
        }

    @classmethod
    def parse(cls, packet):
        ip_header = packet[:20]
        return cls.parse_ip_header(ip_header)


class TransParser:
    IP_HEADER_OFFSET = 20
    UDP_HEADER_LENGTH = 8
    TCP_HEADER_LENGTH = 20

def data2str(data):
    l = len(data)
    data = struct.unpack(l*'B', data)
    string = ''
    for ch in data:
        if ch >= 127 or ch < 32:
            string += '.'
        else:
            string += chr(ch)
    return string

class UDPParser(TransParser):
    """
    1、 16位源端口  16位目的端口
    2、 16位UDP长度  16位校验和
    """
    @classmethod
    def parse_udp_header(cls, udp_header):
        udp_header = struct.unpack('>HHHH', udp_header)
        return {
            'src_port': udp_header[0],
            'dst_port': udp_header[1],
            'udp_length': udp_header[2],
            'udp_checksum': udp_header[3]
        }

    @classmethod
    def parse(cls, packet):
        udp_header = packet[cls.IP_HEADER_OFFSET:
                            cls.IP_HEADER_OFFSET+cls.UDP_HEADER_LENGTH]
        data = packet[cls.IP_HEADER_OFFSET+cls.UDP_HEADER_LENGTH:]
        result = cls.parse_udp_header(udp_header)
        data = data2str(data)
        result['data'] = data
        return result







