# -*- encoding=utf-8 -*-
import json
import socket
from b_system.pool import ThreadPool as tp
from b_system.task import AsyncTask
from c_network.parser import IPParser, UDPParser


class ProcessTask(AsyncTask):
    def __init__(self, packet, *args, **kwargs):
        self.packet = packet
        super(ProcessTask, self).__init__(func=self.process, *args, **kwargs)

    def process(self):
        headers = {
            'network_header': None,
            'transport_header': None
        }
        ip_header = IPParser.parse(self.packet)
        headers['network_header'] = ip_header
        if ip_header['protocol'] == 17:
            udp_header = UDPParser.parse(self.packet)
            headers['transport_header'] = udp_header

        return headers

class Server:

    def __init__(self):
        # 工作协议类型、套接字类型、工作具体的协议
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_RAW,
                                  socket.IPPROTO_IP)
        # 更改为自己的主机IP
        self.ip = '192.168.3.43'
        self.port = 8888
        self.sock.bind((self.ip, self.port))

        # 混杂模式
        self.sock.ioctl(socket.SIO_RCVALL, socket.RCVALL_ON)

        self.pool = tp(10)
        self.pool.start()

    def loop_serve(self):
        while True:
            # 1. 接收
            packet, addr = self.sock.recvfrom(65535)
            # 2. 生成Task
            task = ProcessTask(packet)
            # 3. 提交
            self.pool.put(task)
            # 4. 获取结果
            result = task.get_result()
            result = json.dumps(
                result,
                indent=4
            )
            print(result)
        pass


if __name__ == '__main__':
    # server = Server()
    # server.loop_serve()

    # 00001111
    print(15 >> 1)
    print(4 >> 1 & 1)
    # print(a >> 1 & 1)





