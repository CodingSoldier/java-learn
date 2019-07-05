# Redis特性

### 1.速度快

- 官方称可以达到10W的qps
- 将数据存储在内存中
- 由C语言编写
- 线程模型为单线程

### 2.持久化

- Redis将所有数据保持在内存中，并异步更新到磁盘上

### 3.支持多种数据结构

- String
- Hash
- List
- Set
- ZSet
- BitMap（位图）
- HyperLogLog（超小的内存唯一值计数器）
- GEO（地理信息定位）

### 4.支持多种编程语言

- Java
- Python
- Ruby
- Lua
- NodeJS

### 5.功能丰富

- 支持类MQ的发布订阅功能
- 支持Lua脚本
- 支持事务
- 支持pipeline

### 6.非常简单

- Redis 3.0的单机核心代码只有2300行代码
- 单线程模型

### 7.支持主从复制

### 8.高可用、分布式

- 在 V2.8 中支持Redis Sentinel
- 在 V3.0 中支持Redis Cluster





# Redis启动方式

### 1.最简启动：使用默认配置

- ./redis-server

### 2.动态参数启动

./redis-server --port 6379

### 3.指定配置文件启动

./redis-server configPath

生产环境推荐此方式：单机多实例配置





# Redis基础常用配置

### 1.daemonize

​	是否是守护进程（yes/no)，设置为yes时，Redis会把启动日志输出到日志文件中

### 2.port

​	Redis端口号

### 3.logfile

​	log 输出到标准设备，logs 不写文件，输出到空设备，/deb/null 

### 4.dir

​	数据库存放路径





# Redis数据结构与内部编码

- string	：raw ， int ， embstr
  - int的范围相当于Java中的long，为8个字节长整型
  - 字符长度少于等于39个长度时为embstr
  - 字符长度超过39个长度时为raw，比起embstr，是不连续的
- hash       ：hashtable（哈希表） ， ziplist（压缩列表）
  - hash-max-ziplist-entries 512
  - hash-max-ziplist-value 64
  - 当hash结构的field数量小于等于512并且每个field与value的长度小于等于64时，hash会采用ziplist
- list           ：linkedlist（双向循环链表） ， ziplist（压缩列表）
  - list-max-ziplist-entries 512
  - list-max-ziplist-value 64
  - 当list结构的元素数量小于等于512并且每个item的长度小于等于64时，list会采用ziplist
- set           ：hashtable（哈希表） ， intset（整数集合）
  - set-max-intset-entries 512
  - 当set结构的元素数量小于等于512时候，set将采用intset
- zset         ：skiplist（跳跃列表） ， ziplist（压缩列表）
  - zset-max-ziplist-entries 512
  - zset-max-ziplist-value 64
  - 当zset结构的元素数量小于等于512并且每个member的长度小于等于64时，zset会采用ziplist
- 当key的数量比较少时，Redis采取以时间换空间的策略
- 当key的数量增多时，Redis采取以空间换时间的策略
- 查看内部编码的方式 object encoding ${key}
- ziplist的特点
  - 连续内存
  - 读写有指针位移，最坏O(n<sup>2</sup>)
  - 新增删除有内存重分配





# Redis单线程为什么速度还那么快

- 纯内存
- 使用epoll创建非阻塞IO
- 由于是单线程，因此避免了线程切换和竞态消耗





# Redis单线程需要注意的点

- 一次只会运行一条命令
- 拒绝长命令/慢命令
  - keys
  - flushall
  - flushdb
  - slow lua script
  - mutil/exec
  - operate





# Redis配置生效方法

- 修改config配置文件，然后重启redis服务
- config set configName configValue  （前提需要当前配置支持动态配置）















