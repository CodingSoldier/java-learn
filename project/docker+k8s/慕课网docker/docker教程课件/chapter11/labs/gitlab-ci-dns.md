# Gitlab CI DNS server

配置一个DNS服务器，能让其他容器解析到 gitlab.example.com

首先，在gitlab ci服务器上把 gitlab.example.com从 /etc/hosts里删除

这时候在gitlab ci服务器上是ping不通gitlab.example.com的。


## 启动DNS服务器

找一台新的Linux host，装好Docker，大家可以用vagrant或者docker-machine创建一台，我这里是用docker machine createde


然后在这台机器上，创建一个dnsmasq的容器，并运行。


```
docker run -d -p 53:53/tcp -p 53:53/udp --cap-add=NET_ADMIN --name dns-server andyshinn/dnsmasq
```


## 配置DNS服务

进入容器

```
docker exec -it dns-server /bin/sh
```

首先配置上行的真正的dns服务器地址，毕竟你只是个本地代理，不了解外部规则。创建文件：

vi /etc/resolv.dnsmasq

添加内容：

nameserver 114.114.114.114
nameserver 8.8.8.8

配置本地解析规则，这才是我们的真实目的。新建配置文件


vi /etc/dnsmasqhosts

添加解析规则,其中192.168.211.10是gitlab服务器地址

192.168.211.10 gitlab.example.com

修改dnsmasq配置文件，指定使用上述我们自定义的配置文件,

vi /etc/dnsmasq.conf
修改下述两个配置

resolv-file=/etc/resolv.dnsmasq
addn-hosts=/etc/dnsmasqhosts


回到宿主，重启dns-server容器服务。

docker restart dns-server


这时候这台docker host就是一个DNS服务器了，假如他的地址是192.168.99.100

## 测试

在gitlab ci机器上修改 sudo vim /etc/resolv.conf

```
[vagrant@gitlab-ci ~]$ more /etc/resolv.conf
nameserver 192.168.99.100
[vagrant@gitlab-ci ~]$
```

192.168.99.100是DNS服务器地址。

这时候在本地就可以ping通gitlab.example.com了

```
[vagrant@gitlab-ci ~]$ ping gitlab.example.com
PING gitlab.example.com (192.168.211.10) 56(84) bytes of data.
64 bytes from gitlab.example.com (192.168.211.10): icmp_seq=1 ttl=64 time=0.018 ms
64 bytes from gitlab.example.com (192.168.211.10): icmp_seq=2 ttl=64 time=0.047 ms
64 bytes from gitlab.example.com (192.168.211.10): icmp_seq=3 ttl=64 time=0.054 ms
^C
--- gitlab.example.com ping statistics ---
3 packets transmitted, 3 received, 0% packet loss, time 2001ms
rtt min/avg/max/mdev = 0.018/0.039/0.054/0.017 ms
[vagrant@gitlab-ci ~]$
```


在gitlab CI服务器上创建一个container，然后在里面ping gitlab.example.com也通，说明就成功了。


```
[vagrant@gitlab-ci ~]$ docker run -it --rm busybox sh
/ # ping gitlab.example.com
PING gitlab.example.com (192.168.211.10): 56 data bytes
64 bytes from 192.168.211.10: seq=0 ttl=63 time=0.632 ms
64 bytes from 192.168.211.10: seq=1 ttl=63 time=0.513 ms
^C
--- gitlab.example.com ping statistics ---
2 packets transmitted, 2 packets received, 0% packet loss
round-trip min/avg/max = 0.513/0.572/0.632 ms
/ # exit
[vagrant@gitlab-ci ~]$
```
