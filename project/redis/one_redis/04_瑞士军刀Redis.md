# Redis命令的生命周期

1. 客户端向Redis服务器发送命令
2. 命令请求在请求队列中排队等待处理
3. 执行命令
4. 返回命令结果





# Redis慢查询

- 慢查询发生在生命周期的第三阶段，是指仅仅执行命令阶段比较慢被称为慢查询。
- 客户端超时不一定是慢查询，但是慢查询时是客户端超时的一个可能因素。





# Redis慢查询相关配置

### 1. slowlog-max-len

​	它决定了慢查询日志最多能保存多少条日志，slow log本身是一个内存中的FIFO队列，当队列大小超过slowlog-max-len时，最旧的一条日志将被删除，而最新的一条日志加入到slow log中。

- 默认值：128
- 支持动态配置

### 2.slowlog-log-slower-than

​	它决定要对执行时间大于多少微妙（microsecond ， 1秒=1,000,000 微妙）的查询进行记录

- 默认值：10000

- slowlog-log-slower-than = 0 ，记录所有命令
- slowlog-log-slower-than < 0 ,   不记录任何命令
- 支持动态配置





# 慢查询相关命令

### 1.slowlog get [n]

- 含义：获取慢查询列表中的慢查询信息
  - n：获取出多少条慢查询数据信息

### 2.slowlog len

- 含义：获取慢查询队列长度

###3.slowlog reset

- 含义：清空慢查询队列





# 慢查询信息字段

1. 字段1：唯一性的日志标识符（Integer）
2. 字段2：被记录命令的执行时间点，已UNIX时间戳格式表示（Integer）
3. 字段3：查询执行时间，以微秒为单位
4. 字段4：完整的执行命令





# 慢查询运维经验

- slowlog-log-slower-than不要设置过大，默认10ms，通常设置1ms
  - 因为Redis的qps是万级别的，即每秒应能执行10000次请求
  - 当一条命令执行1ms时，那每秒只能执行1000次请求

- slowlog-max-len不要设置地过小，通常设置1000左右
- 需要理解命令的生命周期
- 定期持久化慢查询
  - 因为慢查询只存储于内存中，一宕机慢查询数据就会丢失
  - 通过定期slowlog get将慢查询数据转存到MySQL或者ES中





# pipeline的好处

- 省略由于单线程导致的命令排队时间，一次命令的消耗时间=一次网络时间 + 命令执行时间
- 比起命令执行时间，网络时间很可能成为系统的瓶颈
- pipeline的作用是将一批命令进行打包，然后发送给服务器，服务器执行完按顺序打包返回。
- 通过pipeline，一次pipeline（n条命令）=一次网络时间 + n次命令时间

|  命令  |   N个命令操作   | 1次pupeline（n个命令） |
| :----: | :-------------: | :--------------------: |
|  时间  | n次网络+n次命令 |    1次网络+n次命令     |
| 数据量 |     1条命令     |        n条命令         |





# pipeline-Jedis使用

~~~java
//没有使用pipieline的情况下
public void testWithoutPipeline() {
    Jedis jedis = new Jedis("127.0.0.1" , 6379);
    for(int i = 1 ; i <= 10000 ; i++ ) {
        jedis.hset("hashKey-" + i , "field-" + i , "value-" + i);
    }
}

//使用pipeline的情况下
public void testPipeline() {
    Jedis jedis = new Jedis("127.0.0.1" , 6379);
    for(int i = 0 ; i < 100 ; i++ ) {
        Pipeline pipeline = jedis.pipelined();
        for(int j = i * 100 ; i < (i+1) * 100 ; j++ ) {
            pipeline.hset("hashKey-" + j , "field-" + j , "value-" + j);
        }
        pipeline.syncAndReturnAll();
    }
}
~~~





# pipeline VS M操作（mget、mset）

- M操作在Redis队列中是一个原子操作，pipeline不是原子操作
- pipeline与M操作都会将数据顺序的传送与顺序地返回





# pipeline注意事项

- 每次pipeline携带数量不推荐过大，否则会影响网络性能
- pipeline每次只能作用在一个Redis节点上





# 发布订阅的角色与通信模型

### 1.角色

- 发布者（publisher）
- 订阅者（subscriber）
- 频道（channel）

### 2.通信模型

- RedisServer中可以创建若干channel
- 一个订阅者可以订阅多个channel
- 当发布者向一个频道中发布一条消息时，所有的订阅者都将会收到消息
- Redis的发布订阅模型没有消息积压功能，即新加入的订阅者收不到发布者之前发布的消息
- 当订阅者收到消息时，消息内容如下
  - 第一行：固定内容message
  - 第二行：channel的名称
  - 第三行：收到的新消息





# 发布订阅的API

### 1.publish channel message

- 含义：向指定的channel中发布消息

### 2.subscribe channel1 [channel2...]

- 含义：订阅给定的一个或多个渠道的消息

### 3.unsubcribe [channel1 [channel2...]]

- 含义：取消订阅给定的一个或多个渠道的消息

###4.psubscribe pattern1 [pattern2...]

- 含义：订阅一个或多个符合给定模式的频道

###5.punsubscribe [pattern1 [pattern2...]]

- 含义：退订所有给定模式的频道

### 6.pubsub channels

- 含义：列出至少有一个订阅者的频道

###7.pubsub numsub [channel...]

- 含义：列出给定频道的订阅者数量





# 发布订阅-Jedis

~~~java
//订阅
public void testSubscribe() {
    Jedis jedis = new Jedis("127.0.0.1" , 6379);
    jedis.subscribe(new JedisPubSub() {
        @Override
        public void onMessage(String channel, String message) {
            System.out.println("receive channel ["+channel+"] message ["+message+"]");
        }
    } , "aliTV" , "googleTV");
}

//发布
public void testPublish() {
    Jedis jedis = new Jedis("127.0.0.1" , 6379);
    jedis.publish("aliTV" , "I am xuyinan");
    jedis.publish("googleTV" , "My age is 27");
}
~~~





# BitMap的API

### 1.getbit key offset

- 含义：对key所存储的字符串值，获取指定偏移量上的位（bit）

### 2.setbit key offset value

- 含义：对key所存储的字符串值，设置或清除指定偏移量上的位（bit）
  - 返回值为该位在setbit之前的值
  - value只能取0或1
  - offset从0开始，即使原位图只能10位，offset可以取1000

### 3.bitcount key [start end]

- 含义：获取位图指定范围中位值为1的个数
  - 如果不指定start与end，则取所有

### 4.bitop op destKey key1 [key2...]

- 含义：做多个BitMap的and（交集）、or（并集）、not（非）、xor（异或）操作并将结果保存在destKey中

### 5.bitpos key tartgetBit [start end]

- 含义：计算位图指定范围第一个偏移量对应的的值等于targetBit的位置
  - 找不到返回-1
  - start与end没有设置，则取全部
  - targetBit只能取0或者1





# BitMap的使用场景

统计每日用户的登录数。每一位标识一个用户ID，当某个用户访问我们的网页或执行了某个操作，就在bitmap中把标识此用户的位设置为1。





#HyperLogLog简介

- 基于HyperLogLog算法，使用极小空间完成独立数量统计的功能
- 本质还是一个字符串
- 十分节约内存
- 官方给出存在0.81%的错误率
- 无法取出单条数据





# HyperLogLog的相关命令

### 1.pfadd key element1 [element2...]

- 含义：向HyperLogLog中添加元素

### 2.pfcount key1 [key2...]

- 含义：计算HyperLogLog的独立总数

### 3.pfmerge destKey key1 [key2...]

- 含义：合并多个hyperLogLog到destKey中





# GEO简介

- Redis 3.2添加新特性
- 功能：存储经纬度、计算两地距离、范围计算等
- 基于ZSet实现
- 删除操作使用zrem





# GEO相关命令

### 1.geoadd key longitude latitude member [lon lat member...]

- 含义：增加地理位置信息
  - longitude ：经度
  - latitude     :  纬度
  - member   :  标识信息

### 2.geopos key member1 [member2...]

- 含义：获取地理位置信息

###3.geodist key member1 member2 [unit]

- 含义：获取两个地理位置的距离
- unit取值范围
  - m（米，默认）
  - km（千米）
  - mi（英里）
  - ft（英尺）

###4.georadius key longitude latitude unit [withcoord] \[withdist] \[withhash] [COUNT count] \[sort] \[store key] \[storedist key]

- 含义：以给定的经纬度为中心，返回包含的位置元素当中，与中心距离不超过给定最大距离的所有位置元素。
- unit取值范围
  - m（米）
  - km（千米）
  - mi（英里）
  - ft（英尺）
- withcoord：将位置元素的经度与纬度也一并返回
- withdist：在返回位置元素的同时，将距离也一并返回。距离的单位和用户给定的范围单位保持一致
- withhash：以52位的符号整数形式，返回位置元素经过geohash编码的有序集合分值。用于底层应用或调试，实际作用不大。
- sort取值范围
  - asc：根据中心位置，按照从近到远的方式返回位置元素
  - desc：根据中心位置，按照从远到近的方式返回位置元素
- store key：将返回结果而的地理位置信息保存到指定键
- storedist key：将返回结果距离中心节点的距离保存到指定键

### 5.georadiusbymember key member radius unit [withcoord]\[withdist]\[withhash]\[COUNT count]\[sort]\[store key]\[storedist key]

- 含义：以给定的元素为中心，返回包含的位置元素当中，与中心距离不超过给定最大距离的所有位置元素。
- unit取值范围
  - m（米）
  - km（千米）
  - mi（英里）
  - ft（英尺）
- withcoord：将位置元素的经度与纬度也一并返回
- withdist：在返回位置元素的同时，将距离也一并返回。距离的单位和用户给定的范围单位保持一致
- withhash：以52位的符号整数形式，返回位置元素经过geohash编码的有序集合分值。用于底层应用或调试，实际作用不大。
- sort取值范围
  - asc：根据中心位置，按照从近到远的方式返回位置元素
  - desc：根据中心位置，按照从远到近的方式返回位置元素
- store key：将返回结果而的地理位置信息保存到指定键
- storedist key：将返回结果距离中心节点的距离保存到指定键

























































































