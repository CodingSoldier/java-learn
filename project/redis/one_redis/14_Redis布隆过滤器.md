# 布隆过滤器引出

- 问题引出：现有50亿个电话号码，有10万个电话号码，要***快速准确***判断这些电话号码是否已经存在
  1. 通过数据库查询：实现***快速***有点难
  2. 数据预先放在内存集合中：50亿 * 8字节 = 40GB（***内存***浪费或不够）
  3. hyperloglog：***准确***有点难





# 布隆过滤器原理

- 需要参数
  - m个二进制向量（m个二进制位数）
  - n个预备数据（类似于问题中的50亿个电话号码）
  - k个hash函数（每个数据，逐个进行hash，将指定向量标识为1）
- 构建布隆过滤器
  - 将n个数据逐个走一遍hash流程
- 判断元素是否存在
  - 将元素逐个hash进行执行
  - 如果得到的hash结果再向量中全都是1，则表明存在
  - 反之当前元素不存在





# 布隆过滤器误差率

- 对的数据的返回结果必然是对的，但是错误的数据也可能是对的

- 直观因素
  - m / n 的比例：比例越大，误差率越小
  - hash函数的个数：个数越多，误差率越小





# 本地布隆过滤器

- 实现类库：Guava
  ~~~java
  Funnel<Integer> funnel = Funnels.integerFunnel();
  int size = 1000_000;
  double errorChance = 0.001;		//错误率
  BloomFilter<Integer> filter = BloomFilter.create(funnel , size , errorChance);
  for(int i = 0 ; i < size ; i++ ) {
      filter.put(i);
  }
  for(int i = 0 ; i < size ; i++ ) {
      if( !filter.mightContain(i) ) {
          System.out.println("发现不存在的数据 : " + i);
      }
  }
  ~~~

- 布隆过滤器的问题
  - 容量受到限制
  - 多个应用存在多个布隆过滤器，构建同步过滤器复杂





# Redis单机布隆过滤器

- 基于bitmap实现，利用setbit、getbit命令实现
- hash函数：
  - MD5
  - murmur3_128
  - murmur3_32
  - sha1
  - sha256
  - sha512
- 基于Redis单机实现存在的问题
  - 速度慢：与本机比，输在网络
    - 解决方法1：单独部署、与应用同机房甚至同机架部署
    - 解决方法2：使用pipeline
  - 容量限制：Redis最大字符串为512MB、Redis单机容量
    - 解决方法：基于RedisCluster实现





# Redis Cluster实现布隆过滤器

- 多个布隆过滤器：二次路由
- 基于pipeline提高效率





























