# 搭建一个私有的docker registry


找一台docker host，然后运行 

```
docker run -d -v /opt/registry:/var/lib/registry -p 5000:5000 --restart=always --name registry registry:2
```

docker registry 绑定了到本地的80端口。 接下来我们配置DNS server，假如我们这台运行registry的机器地址是192.168.99.100,

然后我们找到上次配置gitlab ci的那个dns container

执行

```
docker exec -it dns-server /bin/sh
```

添加一条新的记录

```
/ # more /etc/dnsmasqhosts
192.168.211.10 gitlab.example.com
192.168.99.100 registry.example.com
/ #
```

然后重启container

```
docker restart dns-server
```

最后我们去gitlab-ci服务器，ping一下


```
[vagrant@gitlab-ci ~]$ ping registry.example.com
PING registry.example.com (192.168.99.100) 56(84) bytes of data.
64 bytes from registry.example.com (192.168.99.100): icmp_seq=1 ttl=63 time=0.395 ms
64 bytes from registry.example.com (192.168.99.100): icmp_seq=2 ttl=63 time=0.537 ms
64 bytes from registry.example.com (192.168.99.100): icmp_seq=3 ttl=63 time=0.683 ms
^C
--- registry.example.com ping statistics ---
3 packets transmitted, 3 received, 0% packet loss, time 2003ms
rtt min/avg/max/mdev = 0.395/0.538/0.683/0.119 ms
[vagrant@gitlab-ci ~]$
```

成功。


# 测试

在gitlab-ci服务器上，编辑

创建一个文件

```
sudo vim /etc/docker/daemon.json
```

然后写入内容：

```
[vagrant@gitlab-ci ~]$ sudo more /etc/docker/daemon.json
{ "insecure-registries":["registry.example.com:5000"] }
[vagrant@gitlab-ci ~]$
```

从docker hub拉取一个busybox，然后打一个tag

```
docker pull busybox
docker tag busybox registry.example.com:5000/busybox
```

然后push到我们的私有registry里

```
[vagrant@gitlab-ci ~]$ docker push registry.example.com:5000/busybox
The push refers to repository [registry.example.com:5000/busybox]
c5183829c43c: Pushed
latest: digest: sha256:c7b0a24019b0e6eda714ec0fa137ad42bc44a754d9cea17d14fba3a80ccc1ee4 size: 527
[vagrant@gitlab-ci ~]$
```

成功。