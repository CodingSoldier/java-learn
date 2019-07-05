# 持久化的定义

​	redis将所有数据保持在内存中，对数据的更新将异步地保存在磁盘中





# 持久化的方式

### 快照

- MySQL Dump
- Redis RDB

###写日志

- MySQL Binlog
- Hbase HLog
- Redis AOF





# RDB

## 一、什么是RDB

经过RDB之后，redis会将内存中的数据创建一份快照到硬盘中，称为RDB文件（二进制）

当redis重新启动时，会加载硬盘中的RDB文件，加载到内存中完成数据恢复

## 二、RDB的触发方式

### 1.save（同步）

- 在save的同时，其他命令会阻塞等待

- 如果存在老的RDB文件，会先创建一个临时文件，然后对老文件进行替换
- 时间复杂度，O(n)

### 2.bgsave（异步）

- 调用bgsave后，会调用linux的fork()函数，创建一个子进程
- 如果存在老的RDB文件，会先创建一个临时文件，然后对老文件进行替换
- 时间复杂度，O(n)
- 子进程名称：redis-rdb-bgsave

### 3.save VS bgsave

|        |       save       |        bgsave        |
| :----: | :--------------: | :------------------: |
| IO类型 |       同步       |         异步         |
|  阻塞  |        是        | 是（阻塞发生在fork） |
| 复杂度 |       O(n)       |         O(n)         |
|  优点  | 不会消耗额外内存 |   不阻塞客户端命令   |
|  缺点  |  阻塞客户端命令  |  需要fork，消耗内存  |

## 三、通过配置自动进行RDB操作

- 内部相当于bgsave

- Redis默认的save配置

  | 配置 | second | changes |
  | :--: | :----: | :-----: |
  | save |  900   |    1    |
  | save |  300   |   10    |
  | save |   60   |  10000  |

## 四、RDB相关配置

|           配置项            |  默认值  |             含义             |
| :-------------------------: | :------: | :--------------------------: |
|         dbfilename          | dump.rdb |        RDB快照文件名         |
|             dir             |    ./    |   RDB快照文件生成所在目录    |
| stop-writes-on-bgsave-error |   yes    | bgsave时发生错误是否停止写入 |
|       rdbcompression        |   yes    |     RDB文件是否采用压缩      |
|         rdbchecksum         |   yes    |      是否对RDB进行校验       |

## 五、RDB最佳配置

- 不配置自动RDB操作
- dbfilename  dump-${port}.rdb
- dir /redisDataPath
- stop-writes-on-bgsave-error yes
- rdbcompression yes
- rdbchecksum yes

## 六、不容忽视的触发机制

- 主从复制时机的全量复制，master节点会执行bgsave
- debug reload
- shutdown
- flushDB 、 flushAll

## 七、RDB的缺点

- 耗时
- 耗性能
- 不可控，容易丢失数据





# AOF

## 一、什么是AOF

通过日志方式将redis中的写命令进行日志记录，保存在硬盘文件中

日志记录的实质是将写命令写在硬盘的缓冲区中，再根据相关策略把数据刷新到磁盘中

当redis服务器启动时候，执行硬盘中的日志文件以恢复redis中的数据

## 二、AOF的三种策略

### 1.always

- 含义：执行每条写命令都会将写命令写到磁盘中

### 2.everysec

- 含义：每秒将数据从缓冲区刷到磁盘中，可能会丢失一秒的数据

###3.no

- 含义：写命令何时刷新的磁盘中，由操作系统来决定

### 4.三种策略的比较

|   命令   |          优点           |                 缺点                  |
| :------: | :---------------------: | :-----------------------------------: |
|  always  |       不丢失数据        | IO开销较大，一般的SATA盘只有几百的TPS |
| everysec | 每秒一次fsync，保护硬盘 |         可能会丢失一秒的数据          |
|    no    |         不用管          |                不可控                 |

## 三、AOF重写

### 1.重写的作用

- 减少磁盘占用
- 加速AOF恢复速度
  - 例如一万次incr key 可以重写为 set key 10000

### 2.AOF重写实现方式-bgrewriteaof

客户端发送出一条bgrewriteaof命令后，redis会fork一个子进程完成AOF重写操作逻辑

### 3.AOF重写实现方式-AOF重写配置

- AOF配置项

|            配置             | 默认值 |                             含义                             |
| :-------------------------: | :----: | :----------------------------------------------------------: |
|  auto-aof-rewrite-min-size  |  64MB  |           AOF文件重写需要的尺寸，AOF多大时开启重写           |
| auto-aof-rewrite-percentage |  100   | AOF文件增长率<br/>(当前AOF文件大小超过上一次重写的AOF<br/>文件大小的百分之多少才会重写) |

- AOF统计项

  |      统计名      |                 含义                  |
  | :--------------: | :-----------------------------------: |
  | aof_current_size |       AOF当前尺寸（单位：字节）       |
  |  aof_base_size   | AOF上次启动和重写的尺寸（单位：字节） |

- 触发时机

  - ***当前AOF文件大小***超过***最小重写尺寸***
  - ***当前AOF文件大小***超过***上次重写完的AOF尺寸***的百分之多少（***auto-aof-rewrite-percentage***）

### 4.AOF重写原理

AOF重写不会读取老的AOF文件，而是根据当前服务器的状态生成一份新的AOF文件，将老的AOF文件进行替换

## 四、AOF相关配置

| 配置项                      | 最佳取值        | 含义                      |
| --------------------------- | --------------- | ------------------------- |
| appendonly                  | yes             | 开启AOF                   |
| appendfilename              | aof-${port}.aof | AOF文件名                 |
| appendfsync                 | everysec        | AOF策略                   |
| dir                         | /redisDataPath  | AOF文件所在目录           |
| no-appendfsync-no-rewrite   | yes             | 在执行重写时不进行AOF操作 |
| auto-aof-rewrite-percentage | 100             | AOF重写配置（见上文）     |
| auto-aof-rewrite-min-size   | 64MB            | AOF重写配置（见上文）     |





# RDB与AOF的抉择

### 1.RDB VS AOF

|            |    RDB     |     AOF      |
| :--------: | :--------: | :----------: |
| 启动优先级 |     低     |      高      |
|    体积    |     小     |      大      |
|  恢复速度  |     快     |      慢      |
| 数据安全性 | 容易丢数据 | 根据策略决定 |
|    轻重    |     重     |      轻      |

### 2.RDB的最佳策略

- 关闭RDB自动执行配置
- 手动管理时进行RDB操作
- 在从节点打开自动执行配置，但是不宜频繁执行RDB

###3.AOF的最佳策略

- 建议打开，但是如果只是纯作为缓存使用可以不开
- AOF重写集中管理
- everysec

### 4.最佳策略

- 小分片
  - 例如设置maxmemory参数设置每个redis只存储4个G的空间，这样各种操作都不会太慢
- 需要进行监控（硬盘、内存、负载、网络）
- 机器需要有足够的内存















































































































































