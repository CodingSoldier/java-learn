# 主从复制的问题

- 当master节点发生故障时，需要手动进行故障转移
- 写能力与存储能力受限，写能力和存储能力都依赖于master节点





# Redis Sentinel架构

​	在主从复制的基础上，新增多个Redis Sentinel节点，这些Sentinel不存储任何的数据。这些Sentinel节点会完成Redis的故障判断并故障转移的处理，然后通知客户端。一套Redis Sentinel集群可以监控多套Redis主从，每一套Redis主从通过master-name作为标识。

​	客户端不直接连接Redis服务，而连接Redis Sentinel。在Redis Sentinel中清楚哪个节点是master节点。

故障转移流程

1. 多个Sentinel发现并确认master有问题
2. 选举出一个Sentinel作为领导
3. 选出一个slave作为master
4. 通知其余slave成为新的master的slave
5. 通知客户端主从发生的变化
6. 等待老的master复活成为新master的slave





# Redis Sentinel的相关配置

| 配置                                            | 含义                                                         |
| ----------------------------------------------- | ------------------------------------------------------------ |
| port ${port}                                    | sentinel的端口号                                             |
| dir "/redisDataPath"                            | redis的工作目录                                              |
| logfile "${port}.log"                           | redis的日志文件                                              |
| sentinel monitor mymaster 127.0.0.1 7000 2      | 名称为mymaster的主从<br/>masterIP=127.0.0.1   masterPort=7000<br />2个sentinel发现这个master有问题后执行故障转移 |
| sentinel down-after-milliseconds mymaster 30000 | 每个sentinel在连续ping 30000ms不通后认为有问题               |
| sentinel parallel-syncs mymaster 1              | 在故障转移时，该名称为mymaster的集群中<br/>同一时间点只允许1个节点进行复制 |
| sentinel failover-timeout mymaster 180000       | 故障转移的超时时间                                           |



# Redis Sentinel的安装与配置

### 1.配置开启主从节点

- redis-7000.conf

~~~shell
port 7000
daemonize yes
pidfile /var/run/redis-7000.pid
logfile "7000.log"
dir /redisDataPath
~~~

- redis-7001.conf

~~~shell
port 7001
daemonize yes
pidfile /var/run/redis-7001.pid
logfile "7001.log"
dir /redisDataPath
slaveof 127.0.0.1  7000
~~~

- redis-7002.conf

~~~shell
port 7002
daemonize yes
pidfile /var/run/redis-7002.pid
logfile "7002.log"
dir /redisDataPath
slaveof 127.0.0.1 7000
~~~

### 2.配置开启sentinel监控主节点（sentinel是特殊的redis）

- redis-26379.conf（redis sentinel的默认端口是26379）

~~~shell
port 26379
daemonize yes
dir "/redisDataPath"
logfile "26379.log"
sentinel monitor mymaster 127.0.0.1 7000 2
sentinel down-after-milliseconds mymaster 30000
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 180000 
~~~

- redis-26380.conf

~~~shell
port 26380
daemonize yes
dir "/redisDataPath"
logfile "26380.log"
sentinel monitor mymaster 127.0.0.1 7000 2
sentinel down-after-milliseconds mymaster 30000
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 180000
~~~

- redis-26381.conf

~~~shell
port 26381
daemonize yes
dir "/redisDataPath"
logfile "26381.log"
sentinel monitor mymaster 127.0.0.1 7000 2
sentinel down-after-milliseconds mymaster 30000
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 180000
~~~





# 客户端接入基本原理

1. 客户端需要***所有的sentinel节点***以及对应的***masterName***
2. 客户端会遍历所有的sentinel节点，获取一个可用的sentinel节点
3. 向***可用的sentinel***调用  ***sentinel get-master-addr-by-name masterName***， 可用的sentinel将返回master节点信息。
4. 客户端连接该master节点，调用role或者role replication，确认该节点是master节点。
5. 如果master发生故障转移，sentinel是能够感知，并通过发布订阅模型将最新的master信息告知客户端

~~~java
Set<String> sentinelSet = new HashSet<String>() {{
    add("127.0.0.1:26379");
    add("127.0.0.1:26380");
    add("127.0.0.1:26381");
}};
JedisPoolConfig poolConfig = new JedisPoolConfig();
String masterName = "myMaster";
int timeout = 30_000;	//jedis连接sentinel的超时时间
JedisSentinelPool sentinelPool = new JedisSentinelPool(
    			masterName , sentinelSet , poolConfig , timeout);
Jedis jedis = sentinelPool.getResource();
jedis.close();
~~~





# 三个定时任务

- 每10秒每个sentinel对master和slave执行info
  - 发现slave节点
  - 确认主从关系
- 每2秒每个sentinel通过mster节点的channel交换信息（pub/sub)
  - 通过\__sentinel__:hello频道交互
  - 交互对节点的“看法”和自身信息
- 每1秒每个sentinel对其他sentinel和redis执行ping





# 主观下线与客观下线

- 主观下线：每个sentinel节点对Redis节点失败的看法。
  - sentinel down-after-milliseconds masterName timeout
  - 每个sentinel节点每秒会对Redis节点进行ping，当连续timeout毫秒之后还没有得到PONG，则sentinel认为redis下线。
- 客观下线：所有sentinel节点对Redis节点失败达成共识。
  - sentinel monitor masterName ip port quorum
  - 大于等于quorum个sentinel主观认为Redis节点失败下线
  - 通过sentinel is-master-down-by-addr提出自己认为Redis master下线





# 领导者选举

- 原因：只有sentinel节点完成故障转移
- 选举：通过 sentinel is-master-down-by-addr 命令都希望成为领导者
  - 每个主观下线的sentinel节点向其他sentinel节点发送命令，要求将它设置为领导者
  - 收到命令的Sentinel节点如果没有同意其他Sentinel节点发送的命令，那么将同意该请求，否则拒绝。
  - 如果该Sentinel节点发现自己的票数已经超过Sentinel集合半数且超过quorum，那么将它成为领导者
  - 如果此过程有多个Sentinel节点成为了领导者，那么将等待一段时间重新进行选举





# 故障转移（Sentinel领导者节点完成之后）

1. 从slave节点中选出一个“合适的”节点作为新的master节点
   - 选择slave-priority(slave节点优先级)最高的slave节点，如果存在则返回，不存在则继续
   - 选择复制偏移量最大的slave节点（复制的最完整性），如果存在则返回，不存在则继续
   - 选择runId最小的slave节点
2. 对上面的slave节点执行slave no one命令让其成为master节点
3. 向其余的slave节点发送命令，让它们成为新master节点的slave节点，复制规则和parallel-syncs参数有关。
4. 更新对原来master节点配置为slave，并保持对其“关注”，当其恢复后命令它去复制新的master节点





# 节点运维（上线与下线）

### 生产节点下线可能原因

- 机器下线：过保等情况
- 机器性能不足：例如CPU、内存、磁盘、网络等

### 1.主节点

~~~shell
##节点下线
##手动进行故障转移
sentinel failover ${masterName}
##跳过主观下线、客观下线与领导者选举，领导者即为当前连接的sentinel节点

##节点上线
config set slave-priority num  #调大新增节点的优先级
sentinel failover ${masterName}
~~~

### 2.从节点

需要区分是临时下线还是永久下线。例如需要做一些配置、AOF、RDB等方面的清理工作。

当上线时候，执行slaveof masterIp masterPort即可

### 3.Sentinel节点

需要区分是临时下线还是永久下线。例如需要做一些配置的清理工作。





# 高可用读写分离

可以考虑尝试扩展 JedisSentinelPool，维护master节点与slave节点池

并关注三条消息来维护节点池

- switch-master : 切换主节点（从节点晋升为主节点）
- convert-to-slave : 切换从节点（原主节点降为从节点）
- sdown : 主动下线































































