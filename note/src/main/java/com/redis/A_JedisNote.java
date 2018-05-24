package com.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/*
 java和redis整合，使用Jedis。

 查看6379端口是否开启  iptables -L -n|grep 6379
 开启6379端口  iptables -I INPUT -p tcp -m state --state NEW -m tcp --dport 6379 -j ACCEPT
 保存策略：iptables-save > iptables.rules
 重新加载策略：iptables-restore iptables.rules
 策略初始化保存： /usr/libexec/iptables/iptables.init save

 vim /usr/local/redis/redis.conf
 注释掉 #bind 127.0.0.1
 将保护模式设置为no  protected-mode no
 */
public class A_JedisNote {
    //单实例链接redis数据库
    @Test
    public void t(){
        Jedis jedis = new Jedis("192.168.40.129", 6379);
        jedis.set("key1", "val1");
        System.out.println(jedis.get("key1"));
    }

    //使用Jedis链接池连接redis数据库
    @Test
    public void t2(){
        //1、设置连接池的配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        //连接池中最大连接数，可选参数
        config.setMaxTotal(50);
        //连接池中包邮的最大连接数，可选参数
        config.setMaxIdle(2);
        //2、设置连接池对象
        JedisPool pool = new JedisPool(config, "192.168.40.129", 6379);
        //3、从池中获取对象
        Jedis jedis = pool.getResource();
        System.out.println(jedis.get("key1"));
        //4、链接归还到池中
        jedis.close();
    }
}
