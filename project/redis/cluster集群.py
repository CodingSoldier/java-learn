###################安装ruby
wget https://cache.ruby-lang.org/pub/ruby/2.6/ruby-2.6.3.tar.gz
tar -zxvf ruby-2.6.3.tar.gz
cd ruby-2.6.3/ 
./configure -prefix=/usr/local/ruby
make
make install
cd /usr/local/ruby/
cp bin/ruby /usr/local/bin/
cp bin/gem /usr/local/bin/

ruby --version


###############创建配置
vim config/redis-7000.conf

port 7000
protected-mode no
daemonize yes
dir "/soft/redis/data"
dbfilename "dump-7000.rdb"
logfile "log-7000.log"

cluster-enabled yes
cluster-config-file nodes-7000.conf
cluster-require-full-coverage no

配置详解
cluster-enabled yes  启动集群
cluster-require-full-coverage no  有一个节点不可用，依旧运行集群

创建其他5份配置
sed 's/7000/7001/g' config/redis-7000.conf > config/redis-7001.conf
sed 's/7000/7002/g' config/redis-7000.conf > config/redis-7002.conf
sed 's/7000/7003/g' config/redis-7000.conf > config/redis-7003.conf
sed 's/7000/7004/g' config/redis-7000.conf > config/redis-7004.conf
sed 's/7000/7005/g' config/redis-7000.conf > config/redis-7005.conf

####################启动6个实例
redis-server config/redis-7000.conf
redis-server config/redis-7001.conf
redis-server config/redis-7002.conf
redis-server config/redis-7003.conf
redis-server config/redis-7004.conf
redis-server config/redis-7005.conf


###################创建集群
redis-cli --cluster create  --cluster-replicas 1 \
192.168.4.176:7000 192.168.4.176:7001 192.168.4.176:7002 192.168.4.176:7003 192.168.4.176:7004 192.168.4.176:7005

yes

在客户端查看集群状态
cluster info

使用集群模式客户端
redis-cli -c -p 7000

查看key所在的槽
cluster keyslot hello

键值存储在当前节点
set hello world

当前连接重定向到其他节点，键值存储
set php best

当前连接又重定向回7000节点
get hello


jedis配置
spring.redis.cluster.nodes=192.168.4.176:7000, 192.168.4.176:7001, 192.168.4.176:7002, 192.168.4.176:7003, 192.168.4.176:7004, 192.168.4.176:7005















