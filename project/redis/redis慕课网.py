redis安装
cd /soft
wget  http://download.redis.io/releases/redis-5.0.5.tar.gz
tar -xvf redis-5.0.5.tar.gz
建一个软连接，方便管理
ln -s redis-5.0.5 redis
cd redis
make 
make install

启动
redis-server
客户端连接，任意目录下执行
redis-cli -h 127.0.0.1 -p 6379

配置文件设置
mkdir config
去掉注释内容和空格
cat redis.conf | grep -v "#" | grep -v "^$" > ./config/redis-6381.conf

vim config/redis-6382.conf
protected-mode no
daemonize yes
port 6382
dir "/soft/redis/data"
logfile "6382.log"

在redis软连接目录下
mkdir data

使用配置文件启动
redis-server config/redis-6382.conf 
查看日志
cat data/6382.log

关闭
redis-cli -p 6382 shutdown

遍历所有key
keys *
keys [pattern]
mset hello world hehe haha php good phe his
keys he*  以he开头
keys he[h-l]*  he开头后面包含h-l
keys he?  he开头，后面还有一个字符

查看key的总数
dbsize

检查key是否存在
exists key

删除key-value
del key 

key在多少秒后过期
expire key seconds


jedis配置
maxTotal    资源池最大连接数，默认8
maxIdle     资源池允许最大空闲连接数，默认8，建议maxTotal=maxIdle
minIdle     资源池确保最少空闲连接数，默认0
jmxEnabled  是否开启jmx监控，建议开启

jedis借还参数
blockWhenExhausted   资源池连接用尽后，调用者是否要等待，只有设置为true时，下面的额maxWaitMilis才会生效   默认true
maxWaitMillis        资源池连接用尽后，调用者的最大等待时间，单位毫秒    默认-1永不超时，建议修改



慢查询只发生在命令执行阶段，不包括命令发送、排队、返回结果这3个阶段。

慢查询队列固定长度，默认128
slowlog-max-len  
慢查询阀值，单位微妙，默认10000
slowlog-log-slower-than

动态配置
config set slowlog-max-len 1000

slowlog get [n]   获取慢查询队列
slowlog len       获取慢查询队列长度
slowlog reset     清空慢查询队列


使用pipeline一次打包发送多个请求
Jedis jedis = new Jedis("localhost", "6379");
Pipeline Pipeline = jedis.pipelined();
for(int i=0; i++ ; i<1000){
	pipeline.hset("hashkey:"+i, "file"+i; "value"+i);
}
pipeline.syncAndReturnAll();


发布订阅

空间坐标
添加
geoadd city:locations 118.28 39.55 tianjing
获取坐标
geopos city:locations tianjing
两个坐标点之间的距离，单位 m/km/mi(英里)
geodist city:locations tianjing beijing m


RDB持久化策略
RDB是一个二进制文件，备份时，先生成临时文件，再替换旧文件
手动备份 save    会阻塞后面的命令
        bgsave  由子进程来备份，创建了子进程后，不会阻塞后面的命令

# 900s内发生1次写入，执行bgsave
save 900 1 
# 300s内发生10次写入，执行bgsave 
save 300 10
# 60s内发生1000次写入，执行bgsave 
save 60 1000

#rdb文件名
dbfilename  dump.rdb
#文件目录
dir ./
stop-writes-on-bgsave-error  yes
# rdb是否压缩
rdbcompression  yes
# 是否校验
rdbchecksum  yes


vim config/redis-6382.conf
protected-mode no
daemonize yes
port 6382

# rbd配置
save 900 1 
save 300 10
save 60 1000
dbfilename  redis-6382.rdb
dir "/soft/redis/data"
stop-writes-on-bgsave-error  yes
rdbcompression  yes
rdbchecksum  yes

logfile "log-6382.log"


查看内存使用情况
info memory



AOF使用日志功能保存数据操作
策略 
# appendfsync always #每次收到写命令就立即强制写入磁盘，是最有保证的完全的持久化，但速度也是最慢的，一般不推荐使用。  
appendfsync everysec #每秒钟强制写入磁盘一次，在性能和持久化方面做了很好的折中，是受推荐的方式。  
# appendfsync no     #完全依赖OS的写入，一般为30秒左右一次，性能最好但是持久化最没有保证，不被推荐。  


AOF重写，减小磁盘占用，比方说incr操作不需要把每次incr都记录，直接记录最后一次
bgrewriteaof

auto-aof-rewrite-min-size    aof文件重写需要的尺寸
auto-aof-rewrite-percentage  aof文件增长率

aof_current_size   aof当前尺寸，单位字节
aof_base_size      aof上次启动和重写的尺寸，单位字节

AOF重写过程
1、fork出子进程
2、子进程生成新的aof文件
3、子进程生成新的aof文件过程中，有新的写入命令，新数据会写入redis，新数据还会写入一个缓冲区，缓冲区的数据最后会写入新aof中
4、新aof文件替换旧aof文件

# 开启aof
appendonly yes
# 文件名
appendfilename  "appendonly-6382.aof"
#策略
appendfsync everysec
dir "/soft/redis/data"
# 重写会产生大量磁盘io，所以在aof重写期间，主进程不再写入aof文件，此时相当于appendonly no。但新数据还是会写入到新的aof中，若在aof重写期间宕机，则会造成数据丢失
no-appendfsync-on-rewrite yes
# 增长率
auto-aof-rewrite-percentage 100
# 尺寸
auto-aof-rewrite-min-size 64mb
#数据不完成时是否加载
aof-load-truncated yes


appendonly 支持热更新

config set appendonly yes
config get appendonly
config rewrite


看下文件大概长什么样子
cat data/appendonly.aof

重写下
bgrewriteaof


主从复制
主节点配置
redis-6379.conf
#bind 127.0.0.1
protected-mode no
daemonize yes
logfile "log-6379.log"
#save 900 1
#save 300 10
#save 60 10000
dbfilename dump-6379.rdb
dir /soft/redis/data
appendfilename "appendonly-6379.aof"

从节点配置，从节点不能执行set操作，从节点有数据会先清除旧数据
redis-6380.conf
protected-mode no
#bind 127.0.0.1
daemonize yes
logfile "log-6380.log"
#save 900 1
#save 300 10
#save 60 10000
dbfilename dump-6380.rdb
dir /soft/redis/data
appendfilename "appendonly-6380.aof"
slaveof 192.168.4.176 6379


启动主节点
redis-server config/redis-6379.conf
redis-cli -p 6379
分片信息
info replication

启动从节点
redis-server config/redis-6380.conf
redis-cli -p 6380
查看主从状态
info replication


从节点初始化全量复制的时间开销
1、主节点执行bgsave
2、将RDB 传输给从节点
3、从节点清空数据
4、从节点加载rdb
5、可能有AOF重写时间




哨兵模式sentinel，redis-sentinel也是一个小型redis实例，没有set功能，没有get key功能，只用于监控
6379主节点，6380、6381是从节点

port 26379
dir "/soft/redis/data"
logfile "log-26379.log"
# sentinel配置，mymaster主节点名字，2表示有2个sentinel发现master有问题则进行故障转移
sentinel monitor mymaster 192.168.4.176 6379 2
# master发生故障时间
sentinel down-after-milliseconds mymaster 30000
# 一次一个从节点从主节点同步数据
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 180000

cat sentinel.conf | grep -v "#" | grep -v "^$" > ./config/redis-sentinel-26379.conf

vim config/redis-sentinel-26379.conf
port 26379
daemonize yes
pidfile /var/run/redis-sentinel-26379.pid
logfile "log-sentinel-26379.log"
dir /tmp
# 必须填写主机ip，两个sentinel认为几点下线，则线下master，建议为 sentinel几点数/2 + 1
sentinel monitor mymaster 192.168.4.176 6379 2
# 每秒ping节点，30s没收到节点回复则认为几点下线了
sentinel down-after-milliseconds mymaster 30000
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 180000
sentinel deny-scripts-reconfig yes

启动sentinel
redis-sentinel config/redis-sentinel-26379.conf
可以进入redis-sentinel客户端
redis-cli -p 26379
信息
info
配置会被重写
cat config/redis-sentinel-26379.conf 

配置多份redis-sentinel，启动sentinel
vim config/redis-sentinel-26380.conf
vim config/redis-sentinel-26381.conf





查找value非常大的key
redis-cli --bigkeys


info memory  查看内存
used_memory  reids数据的内存量
used_memory_human  reids数据的内存量加个单位，让可读性更高
used_memory_rss   从操作系统的角度，redis进程占用的总内存
used_memory_peak   used_memory的历史峰值
used_memory_lua    lua引擎消耗的内存
mem_fregmentation_ratio     used_memory_rss/used_memory，表示内存碎片率
mem_allocator    Redis所使用的的内存分配器，默认jemalloc



复制缓冲区repl_back_buffer默认1M，可以设置得更大一点















