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

开机启动
vim /etc/systemd/system/redis.service

[Unit]
Description=redis-server
After=network.target

[Service]
Type=forking
ExecStart=/usr/local/bin/redis-server /soft/redis/redis.conf
PrivateTmp=true

[Install]
WantedBy=multi-user.target

systemctl enable redis
systemctl start redis
systemctl stop redis

incr key
# key自增1，如果key不存在，则设置值为1，返回自增后的值
decr key
# key自减1，如果key不存在，则设置为-1，返回自减后的值
incrby key k
# key自增k，key必须已经存在
decrby key k
# key自减k，key必须已经存在

setnx key value / set key value nx
# 不存在才能设置成功
set key value xx 
# key存在才能设置成功

time
#获取Unix时间（秒为单位），后面加上3个0就是时间戳
expireat key unix时间(秒)
#后面的时间单位是时间戳少3个0

mset hello world java zhazha php verygood
# 批量设置key-value
mget hello java php
# 批量获取key

getset key newvalue
# 设置新值并返回旧的值
append key value
# 将value拼接到旧值后
strlen key
# 返回字节串长度，一个中文为3个字节

incrbyfloat key 3.5
# 浮点自增，但是没有浮点数自减
getrange key start end
# 获取字符串指定下标所有值
setrange key index value
# 设置指定下标所有对应的值


hset key field value
# 设置hash key对应的field的value
hget key field
# 获取hash key对应的field的value
hdel key field
# 删除hash key对应的field的value
hexists key field
# 判断hash key是否有field
hlen key
# 获取hash key field数量
hmget key field1 field2 ...
# 批量获取hash key的一批field对应的值
hmset key field1 value1 field2 value2 ...
# 批量设置hash key的一批field value
hgetall key
# 返回hash key对应所有的field和value
hvals key
# 返回hash key对应的所有field的value
hkeys key
# 返回hash key对应的所有field
hsetnx key field value
# 设置hash key对应field的value(如果field已经存在，则失败)
hincrby key field intCounter
# hash key对应的field的value自增intCounter
hincrbyfloat key field floatCounter
# 自增浮点数版本




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


分布式锁
setnx key value      key不存在就设置值
expire key seconds   设置过期时间
del key              代码执行完成后要删除锁
这有个bug，假如服务A在执行完 setnx key value 之后就重启或宕机，锁就已经存在，但没有设置过期时间。其他服务执行 setnx key value永远返回0，当服务A重启后 set key value 也是返回0

双重防死锁
setnx key value     value设置为 时间戳+timeout
expire key seconds  设置过期时间timeout/1000
    if (setnx返回1)
        del key   删除key
    else
        获取旧的value  时间戳+time
        if (时间戳+time != null && 当前时间戳 > value){
            getSet key 当前时间戳 + timeout

            # getSet是原子性的
            # getSet的结果 == null 说明key对应的值不存在了，也就是获取了锁
            # getSet的结果 != 旧的value时间戳+time 说明还有一个线程X执行了getSet把key对应的value给改了，也就是当前线程不能获取锁，是线程X获取了锁
            if (getSet的结果 == null || (getSet的结果 == 旧的value时间戳+time)){
                获取到锁了
            }
        }





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
慢查询阀值，单位微妙，默认
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
3、子进程生成新的aof文件过程中，有新的写入命令，新数据会写入redis内存，新数据还会写入一个缓冲区，缓冲区的数据最后会写入新aof中
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

一般预留30%内存余量
设置内存上限
config set maxmemory 1GB
config rewrite
或者在配置文件中加入
maxmemory 1gb


noeviction: 不进行置换，表示即使内存达到上限也不进行置换，所有能引起内存增加的命令都会返回error
allkeys-lru: 优先删除掉最近最不经常使用的key，用以保存新数据，如果没有可删除的建对象，回退到noeviction
volatile-lru: 只从设置失效（expire set）的key中选择最近最不经常使用的key进行删除，用以保存新数据
allkeys-random: 随机从all-keys中选择一些key进行删除，用以保存新数据
volatile-random: 只从设置失效（expire set）的key中，选择一些key进行删除，用以保存新数据
volatile-ttl: 只从设置失效（expire set）的key中，选出存活时间（TTL）最短的key进行删除，，如果没有，回退到noevition策略


/etc/sysctl.conf
vm.overcommit_momory=1 建议设置为 1
vm.swappiness=1    1可以用swap，0宁愿杀死进程也不用swap，如果是高可用可以考虑设置为0，让从redis起来
输出值
cat /proc/sys/vm/overcommit_memory

禁用thp
/sys/kernel/mm/transparent_hugepage/enabled   
never


安全7法
1、设置密码
2、bind限制的是网卡，并不是客户端ip
3、不使用root用户启动












