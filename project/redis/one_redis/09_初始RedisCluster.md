# 为什么需要Redis集群

- 需要提高更大的并发量
  - Redis官方提出拥有10万QPS的请求量
  - 如果业务需要Redis拥有100万的QPS
  - 可以通过集群来提升并发量。

- 需要存储更大的数据量
  - 一般服务器的机器内存为16G-256G
  - 如果业务需要500G的数据量
  - 可以通过集群的分区技术来扩展数据量





# 数据分区

### 1.顺序分区

例如一共有编号为1~100的100条数据，一共有3个分区ABC，则需要预先设计

- 1~33号数据落入A分区

- 34~66号数据落入B分区
- 67~100号数据落入C分区

### 2.哈希分区

hash(key) % node_count

### 3.顺序分区 VS 哈希分区

| 分布方式 |                          特点                          |                       典型产品                        |
| :------: | :----------------------------------------------------: | :---------------------------------------------------: |
| 哈希分布 |  数据分散度高<br/>键值分布与业务无关<br/>无法顺序访问  | 一致性哈希Memcache<br/>Redis Cluster<br/>其他缓存产品 |
| 顺序分布 | 数据分散度易倾斜<br/>键值分布与业务有关<br/>可顺序访问 |                  BigTable<br/>HBase                   |





# 哈希分区

### 1.节点取余分区

- 含义：hash(key) % node_count
- 优点：hash+取余的方式计算节点的分区很简单
- 缺点：当节点伸缩时候，数据节点关系发生变化，导致数据迁移
- 扩容的时候建议翻倍扩容，可以降低数据的迁移量。

###2.一致性哈希分区

- 含义：哈希+顺时针（优化取余）
  - 约定长度2<sup>32</sup>位的哈希环，在其中分布若干个hash点。
  - 第一步对每个key做哈希处理得到hashVal
  - 第二步将hashVal顺时针偏移，得到的第一个hash点，即为分区的落点
- 优点：节点伸缩的时候，只会影响邻近节点，但是还是会有数据迁移
- 翻倍的伸缩，可以保证最小的迁移数据且达到数据的负载均衡

### 3.虚拟槽分区

- 预设虚拟槽，每个槽映射一个数据子集，一般比节点数大
- 采用CRC16(key) & 16383来决定节点
- 每个节点顺序地平均分布16384个槽，即当有5个节点时
  - A    0  ~ 3276
  - B    3277 ~ 6553
  - C    6554 ~ 9830
  - D    9831 ~ 13107
  - E    13108 ~ 16383





# RedisCluster架构

## 一、节点

由多个master主节点组成，各个master都负责去读写，每个master都有各自的slave节点。

每个node的cluster_enabled配置为yes

## 二、Gossip协议

多个master节点之间会使用Gossip协议进行通信

### 1.meet消息

​	用于通知新节点加入。消息发送者通知接收者加入到当前集群，meet消息通信正常完成后，接收节点会加入到集群中并进行周期性的ping、pong消息交换。

​	当A meet B以及A meet C之后，B就可以与C做交互了 

### 2.ping消息

​	集群中交换最频繁的消息，集群内各个节点每秒向多个其他节点发送ping消息，用于检测节点是否在线和交换彼此状态信息。

​	ping消息发送封装了自身节点和部分其他节点的状态数据。

### 3.pong消息

​	当接收到ping、meet消息时，作为响应消息回复给发送方确认消息正常通信。

​	pong消息内部封装了自身状态数据。

​	节点也可以向集群内广播自身的pong消息来通知整个集群对自身状态进行更新

### 4.fail消息

​	当节点判定集群内另一个节点下线时，会向集群内广播一个fail消息，其他节点接收到fail消息之后把对应节点更新诶下线状态

## 三、指派槽

需要为RedisCluster指派槽，指定各个master节点的槽范围，让它进行正常的读写

## 四、复制

每个master节点包含若干个slave节点，形成主从复制的形式，以提高高可用性。

通过各个节点之间相互监控来达到Sentinel的目的





# RedisCluster安装

RedisCluster主要配置

|               命令               |                             含义                             |
| :------------------------------: | :----------------------------------------------------------: |
|       cluster-enabled yes        |                     节点是否开启集群模式                     |
|    cluster-node-timeout 15000    |                  节点主观下线超时时间，毫秒                  |
| cluster-config-file "nodes.conf" |                         集群配置文件                         |
| cluster-require-full-coverage no | 是否需要所有节点全都可用，集群才能对外服务，此处推荐设置为no |

## 一、原生命令安装

### 1.配置开启节点

port : 7000、7001、7002、7003、7004、7005

~~~shell
port ${port}
daemonize yes
dir "/redisDataPath"
dbfilename "dump-${port}.rdb"
logfile "${port}.log"
cluster-enabled yes
cluster-config-file nodes-${port}.conf
~~~

### 2.meet

~~~shell
cluster meet ip port
#例如在7000端口上依次执行
cluster meet 127.0.0.1 7001
cluster meet 127.0.0.1 7002
cluster meet 127.0.0.1 7003
cluster meet 127.0.0.1 7004
cluster meet 127.0.0.1 7005
~~~

### 3.指派槽

~~~shell
cluster addslots slot [slot...]
#可以通过编写脚本shell方便实现
#addslot.sh
port=$1
start=$2
end=$3
for slot in `seq ${start} ${end}`
do
	echo "slot:${slot}"
	redis-cli -p ${port} cluster addslots ${slot}
done
#
./addslot.sh 7000 0 5461
./addslot.sh 7001 5462 10922
./addslot.sh 7002 10922 16383
~~~

### 4.设置主从

~~~shell
#可以在任一节点上执行cluster nodes命令查看所有节点的node_id等信息
redis-cli -p 7000 cluster nodes
#在从节点上执行以下操作表示复制master节点
cluster replicate node-id
#
redis-cli -h 127.0.0.1 -p 7003 cluster replicate ${node-id-7000}
redis-cli -h 127.0.0.1 -p 7004 cluster replicate ${node-id-7001}
redis-cli -h 127.0.0.1 -p 7005 cluster replicate ${node-id-7002}
~~~

## 二、官方工具安装

~~~shell
./redis-trib.rb create --replicas 1 127.0.0.1:8000 127.0.0.1:8001 
	127.0.0.1:8002 127.0.0.1:8003 127.0.0.1:8004 127.0.0.1:8005
	
# 1 代表每个master有1个slave
# 此时，一共6个节点，每个master有1个slave，即前3个会形成master节点，后3个会形成slave节点
~~~

### 三、原生命令 VS redis-trib.rb

|               | 优点                            | 缺点                             |
| ------------- | ------------------------------- | -------------------------------- |
| 原生命令      | 易于理解Redis Cluster架构       | 生产环境不使用，太麻烦，容易犯错 |
| redis-trib.rb | 高效，准确<br/>生产环境可以使用 |                                  |























































































































































































































