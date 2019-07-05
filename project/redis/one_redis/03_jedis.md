# Jedis直连

## 执行流程

1. 创建Jedis对象
2. 通过Jedis执行命令
3. 返回Jedis执行结果
4. 关闭Jedis连接

## 创建Jedis的方式

```java
/**
 *	@param host Redis节点所在机器的IP或域名
 *  @param port Redis服务的端口号
 *  @param connectionTimeout 客户端连接超时时间（毫秒）
 *  @param soTimeout 客户端读写超时时间（毫秒）
 */
public Jedis(String host , int port , int connectionTimeout , int soTimeout)
```





# Jedis连接池

## 执行流程

1. 创建一个JedisPool对象
2. 从资源池中获取一个Jedis对象
3. 通过Jedis执行命令
4. 返回Jedis执行结果
5. 关闭Jedis连接，将Jedis还给资源池

## 创建Jedis连接池的方式

```java
JedisPoolConfig config = new JedisPoolConfig();
JedisPool jedisPool = new JedisPool(config , "127.0.0.1" , 6379);
Jedis jedis = jedisPool.getResource();
jedis.close();
```





# Jedis直连 VS Jedis连接池

|             |                             优点                             |                             缺点                             |
| :---------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
|  Jedis直连  |            使用简单<br/>适用于少量长期连接的场景             | 存在每次新建/关闭TCP连接的开销<br/>资源无法控制，存在连接泄露的风险<br/>Jedis对象线程不安全 |
| Jedis连接池 | Jedis对象预先生成，降低使用开销<br/>连接池的形式保护和控制资源的使用 | 相对于直连，使用相对麻烦<br/>尤其在资源的管理上需要许多参数保证<br/>一旦参数不合理会出现很多问题 |

