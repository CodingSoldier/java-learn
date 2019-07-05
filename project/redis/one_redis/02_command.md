# 通用命令

### 1.keys pattern

- 含义：查找所有符合给定模式（pattern）的key
  - keys *	            遍历所有key
  - keys he[h-l]*     遍历以he开头，第三个字符为h-l之间的所有key
  - keys ph?             ?代表一个字符
- 注意：keys命令一般不在生产环境中使用
  - 生产环境的key比较多，keys这个命令是一个O(n)的命令
  - 由于Redis是单线程的，这个命令的操作会阻塞其他操作
  - 可以在从节点上执行keys命令
  - 使用SCAN命令代替keys命令

### 2.dbsize

- 含义：查找当前数据库的key的数量

### 3.exists key

- 含义：检查给定的key是否存在

### 4.del key1 [key2...]

- 含义：在key存在时删除key

### 5.expire key second

- 含义：为给定key设置过期时间，单位为秒

### 6. ttl key

- 含义：以秒为单位，返回给定key的剩余生存时间（TTL , time to live)
  - 不存在key返回-2
  - 无过期时间的key返回-1

### 7.persist key

- 含义：移除key的过期时间，key将持久保存

### 8.type key

- 含义：返回key所存储的值的类型
  - 不存在的key返回none





# String数据类型

## String-结构

- 结构：Key-Value对
- Value：可以是字符串、数字，也可以是二进制数组
- 限制：Value最大值为512MB

## String-常用命令

### 1.get  key

- 含义：获取指定key的值

### 2.set key value

- 含义：设置指定key的值

### 3.incr key

- 含义：将key中存储的数字值增一

### 4.incrby key increment

- 含义：将key所存储的数字值加上给定的增量

### 5.decr key

- 含义：将key中存储的数字值减一

### 6.decrby key decrement

- 含义：将key所存储的数字值减去给定的减量

### 7.setnx key value

- 含义：只有当key不存在时才设置key的值

### 8.set key value nx

- 含义：与⑦相同，只有当key不存在时才设置key的值

###9.setex key second value

- 含义：设置指定key的值，同时设置该key的过期时间，单位为秒

### 10.set key second value ex

- 含义：与⑨相同，设置指定key的值，同时设置该key的过期时间，单位为秒

### 11.set key value xx

- 含义：只有key存在时才设置key的值

### 12.mget key1 [key2...]

- 含义：获取所有（一个或多个）指定key的值

### 13.mset key1 value1 [key2 value2...]

- 含义：同时设置一个或多个key-value对

## String-不太常用命令

### 1.getset key value

- 含义：将给定key的值设置为value，并返回key的旧值

### 2.append key value

- 含义：将value追加到旧值的末尾

### 3.strlen key

- 含义：返回key所存储的字符串值的长度
  - 当key不存在时，返回0
  - 一个中文占2个字节
  - 时间复杂度为O(1)，strlen在redis内部不需要查询整个字符串来得到长度

### 4.incrbyfloat key increment

- 含义：将key所存储的值加上给定的浮点值

### 5.getrange key start end

- 含义：返回key中字符串区间为[start,end]的子串，索引从0开始

### 6.setrange key offset value

- 含义：用value擦书覆盖指定key所存储的字符串值，从偏移量offset开始，索引从0开始
  - key = content的时候，执行setrange key 1 haha 之后，key=chahant





# Hash数据类型

## Hash-常用命令

### 1.hget key field

- 含义：获取存储在哈希表中指定field的值

### 2.hset key field value

- 含义：将哈希表中指定field的值设置为value

### 3.hdel key field1 [field2...]

- 含义：删除哈希表中一个或多个field

### 4.hexists key field

- 含义：判断哈希表中，指定field是否存在

### 5.hlen key

- 含义：获取哈希表中字段的数量

### 6.hmget key field1 [field2...]

- 含义：获取哈希表中所有给定field的值

### 7.hmset key field1 value1 [field2 value2...]

- 含义：同时将一个或多个field-value对设置到哈希表中

### 8.hincrby key fiel increment

- 含义：为哈希表中指定field的值加上一个整型增量

### 9.hgetall key

- 含义：获取哈希表中所有字段和值
- 时间复杂度为O(n)，不建议使用

### 10.hkeys key

- 含义：获取哈希表中的所有字段
- 时间复杂度为O(n)，不建议使用

### 11.hvals key

- 含义：获取哈希表中的所有值
- 时间复杂度为O(n)，不建议使用

### 12.hsetnx key field value

- 含义：只有当哈希表中field不存在时，才设置该field的值

### 13.hincrbyfloat key field increment

- 含义：为哈希表中指定field的值加上一个浮点数增量





# List数据类型

## List-结构

- 列表：有序、可以有重复元素
- 索引相关知识
  - 索引从左往右，从0开始逐个增大      0      1      2      3      4      5
  - 索引从右往左，从-1开始逐个减小    -6     -5     -4    -3      -2    -1     

## List-常用命令

### 1.rpush key value1 [value2...]

- 含义：在列表后侧添加一个或多个值

### 2.lpush key value1 [value2...]

- 含义：在列表左侧添加一个或多个值

### 3.linsert key before/after value newValue

- 含义：在列表指定的value前/后插入newValue，时间复杂度为O(n)

### 4.lpop key

- 含义：从列表左侧弹出一个值

### 5.rpop key

- 含义：从列表右侧弹出一个值

### 6.lrem key count value

- 含义：根据count值，从列表中删除值为value的项，时间复杂度为O(n)
  - count > 0 时，从左往右遍历，删除最多count个与value相等的值
  - count < 0 时，从右往左遍历，删除最多Math.abs(count)个与value相等的值
  - count = 0 时，删除所有与value相等的值

### 7.ltrim key start end

- 含义：对一个列表进行修剪，只保留指定区间内的元素，不在区间内的元素都将被删除，时间复杂度为O(n)

### 8.lrange key start end

- 含义：获取列表指定索引范围的所有元素，时间复杂度为O(n)

### 9.lindex key index

- 含义：获取列表指定索引的元素，时间复杂度为O(n)

### 10.llen key

- 含义：获取列表长度，时间复杂度为O(1)

### 11.lset key index newValue

- 含义：设置列表指定索引的值为newValue，时间复杂度为O(n)
- 注意：
  - 必须存在这个值才能设置成功，否则会报错

### 12.blpop key timeout

- 含义：移除并获取列表左边第一个元素，如果列表没有元素会阻塞直到等待超时或可弹出元素为止
  - timeout单位为秒，timeout=0时不阻塞

### 13.brpop key timeout

- 含义：移除并获取列表右边第一个元素，如果列表没有元素会阻塞直到等待超时或可弹出元素为止
  - timeout单位为秒，timeout=0时不阻塞





# Set数据类型

## Set-结构

- 集合：无序、不能包含重复元素

## Set-集合内操作

### 1.sadd key memebr1 [member2...]

- 含义：向集合中添加一个或多个成员

### 2.srem key member1 [member2...]

- 含义：从集合中删除一个或多个成员

### 3.scard key

- 含义：获取集合中的元素个数

### 4.sismember key member

- 含义：判断member元素是不是集合的成员

### 5.srandmember key count

- 含义：随机从集合中取出count个成员

### 6.spop key

- 含义：随机移除并返回集合中的一个成员

### 7.smembers key

- 含义：获取集合中的所有成员
- 时间复杂度为O(n)，不建议使用，类似的操作可以使用SSCAN

## Set-集合间操作

### 1.sdiff key1 [key2...]

- 含义：返回给定所有集合的差集

### 2.sdiffstore destKey key1 [key2...]

- 含义：计算给定所有集合的差集，并存入destKey

### 3.sinter key1 [key2...]

- 含义：返回给定所有集合的交集

### 4.sinterstore destKey key1 [key2...]

- 含义：计算给定所有集合的交集，并存入destKey

### 5.sunion key1 [key2...]

- 含义：返回给定所有集合的并集

### 6.sunionstore destKey key1 [key2...]

- 含义：计算给定所有集合的并集，并存入destKey





# ZSet数据类型

## ZSet-结构

- 有序集合：有序、不能包含重复元素
- 每个节点包含：score和value两个属性，根据score进行排序
- 索引相关知识
  - 索引从左往右，从0开始逐个增大      0      1      2      3      4      5
  - 索引从右往左，从-1开始逐个减小    -6     -5     -4    -3      -2    -1    

## ZSet-常用命令

### 1.zadd key score1 member1 [score2 member2...]

- 含义：向有序集合添加一个或多个成员，或者更新已存在成员的分数

### 2.zrem key member1 [member2...]

- 含义：从有序集合中删除一个或多个成员

### 3.zscore key member

- 含义：获取有序集合中成员的分数

### 4.zincrby key increment member

- 含义：对有序集合中指定成员的分数加上增量increment

### 5.zcard key

- 含义：返回有序集合中元素的总个数

### 6.zrange key start end [withscores]

- 含义：通过索引返回有序集合中指定区间的成员信息
  - withscores 参数，加上代表一并将score数据返回
- 时间复杂度：O(log(n) + m) , n=有序集合中的元素个数，m=返回的总个数

###7.zrangebyscore key min max \[withscores]\[limit]

- 含义：通过score返回有序集合中指定分数区间的成员信息
  - withscores 参数，加上代表一并将score数据返回
  - limit参数，加上代表限制返回多少条数据
- 时间复杂度：O(log(n) + m) , n=有序集合中的元素个数，m=返回的总个数

###8.zscore key min max

- 含义：返回有序集合中指定分数范围内的元素个数
- 时间复杂度：O(log(n) + m) , n=有序集合中的元素个数，m=指定分数范围内的元素个数

### 9.zremrangebyscore key min max

- 含义：删除有序集合中指定分数区间的所有成员
- 时间复杂度：O(log(n) + m) , n=有序集合中的元素个数，m=指定分数范围内的元素个数

### 10.zremrangebyrank key start end

- 含义：删除有序集合中给定索引区间的所有成员
- 时间复杂度：O(log(n) + m) , n=有序集合中的元素个数，m=指定索引范围内的元素个数





# String VS Hash

## 相似的API

|  String   |    Hash     |
| :-------: | :---------: |
|    get    |    hget     |
| set setnx | hset hsetnx |
|    del    |    hdel     |
|  incrby   |   hincrby   |
| mget mset | hmget hmset |

## 保存对象的三种方式与优缺点

- 方式一：采用string，将整个对象进行序列化，设计Key为对象的业务ID
- 方式二：采用string，设计Key为对象的业务ID+属性名，对各个属性分散存储
- 方式三：采用hash，设计Key为对象的业务ID，属性名为field

|        |                             优点                             |                    缺点                    |
| :----: | :----------------------------------------------------------: | :----------------------------------------: |
| 方式一 |        访问Redis编程简单<br />相比方式二比较节约内存         |  序列化开销<br />修改属性需要操作整个数据  |
| 方式二 |           Redis数据直观可查<br />可以方便更新属性            |       内存占用较大<br />key较为分散        |
| 方式三 | Redis数据直观可查<br />比起方式二比较节约内存<br />可以方便更新属性 | 访问Redis编码稍微比较复杂<br />TTL不好控制 |


































































































































