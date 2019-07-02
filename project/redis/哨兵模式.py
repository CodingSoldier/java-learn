redis-cli -p 6379 shutdown
redis-cli -p 6380 shutdown
redis-cli -p 6381 shutdown
redis-cli -p 26379 shutdown
redis-cli -p 26380 shutdown
redis-cli -p 26381 shutdown

redis-server config/redis-6379.conf
redis-server config/redis-6380.conf
redis-server config/redis-6381.conf

redis-cli -p 6379
info replication

redis-sentinel config/redis-sentinel-26379.conf
redis-sentinel config/redis-sentinel-26380.conf
redis-sentinel config/redis-sentinel-26381.conf


主节点配置  
vim config/redis-6379.conf

# bind 127.0.0.1 
port 6379
protected-mode no
daemonize yes
pidfile "/var/run/redis_6379.pid"
dir "/soft/redis/data"
dbfilename "dump-6379.rdb"
logfile "log-6379.log"


从节点1配置
vim config/redis-6380.conf

# bind 127.0.0.1
port 6380
protected-mode no
daemonize yes
pidfile "/var/run/redis_6380.pid"
dir "/soft/redis/data"
dbfilename "dump-6380.rdb"
logfile "log-6380.log"

replicaof 192.168.4.176 6379


从节点2配置
vim config/redis-6381.conf

# bind 127.0.0.1
port 6381
protected-mode no
daemonize yes
pidfile "/var/run/redis_6381.pid"
dir "/soft/redis/data"
dbfilename "dump-6381.rdb"
logfile "log-6381.log"

replicaof 192.168.4.176 6379



# bind 127.0.0.1  注释掉这配置，以便其他机器的能连接redis
protected-mode no 关闭保护模式，以便其他机器的能连接redis
daemonize后台进程模式启动
redis-v5版本使用replicaof替换旧的slaveof指令



哨兵节点1
vim config/redis-sentinel-26379.conf

port 26379
daemonize yes
pidfile "/var/run/redis-sentinel-26379.pid"
dir /tmp
logfile "log-sentinel-26379.log"
sentinel monitor mymaster 192.168.4.176 6379 2
sentinel down-after-milliseconds mymaster 30000
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 180000
sentinel deny-scripts-reconfig yes


哨兵节点2
vim config/redis-sentinel-26380.conf

port 26380
daemonize yes
pidfile "/var/run/redis-sentinel-26380.pid"
dir /tmp
logfile "log-sentinel-26380.log"
sentinel monitor mymaster 192.168.4.176 6379 2
sentinel down-after-milliseconds mymaster 30000
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 180000
sentinel deny-scripts-reconfig yes


哨兵节点3
vim config/redis-sentinel-26381.conf

port 26381
daemonize yes
pidfile "/var/run/redis-sentinel-26381.pid"
dir /tmp
logfile "log-sentinel-26381.log"
sentinel monitor mymaster 192.168.4.176 6379 2
sentinel down-after-milliseconds mymaster 30000
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 180000
sentinel deny-scripts-reconfig yes

配置说明
monitor mymaster 192.168.4.176 6379 2 
	mymaster是master的名称，192.168.4.176是master主机ip。后面的2表示有2个sentinel认为master下线了，则线下master，建议设置为 sentinel节点数/2 + 1
down-after-milliseconds 
	发送ping请求给redis节点，在指定时间内未收到回复，则认为该节点下线了
parallel-syncs   
	在执行故障转移时，最多可以有多少个从节点同时对新的主服务器进行同步。
















