/**
 * @(#)JedisTest.java, 2018/12/26.
 * <p/>
 * Copyright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.chapter03Jedis;

import com.google.common.base.Joiner;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import redis.clients.jedis.*;

import java.util.*;

public class JedisTest {
	@Test
	public void testJedisCluster() {
		Set<HostAndPort> nodeList = new HashSet<>();
		nodeList.add(new HostAndPort("127.0.0.1" , 7000) );
		nodeList.add(new HostAndPort("127.0.0.1" , 7001) );
		nodeList.add(new HostAndPort("127.0.0.1" , 7002) );
		nodeList.add(new HostAndPort("127.0.0.1" , 7003) );
		nodeList.add(new HostAndPort("127.0.0.1" , 7004) );
		nodeList.add(new HostAndPort("127.0.0.1" , 7005) );
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		int timeout = 30_000;
		JedisCluster jedisCluster = new JedisCluster(nodeList , timeout , poolConfig);
		jedisCluster.set("hello" , "world");
		System.out.println(jedisCluster.get("hello"));

	}

	@Test
	public void testConnectRedis() {
		Jedis jedis = new Jedis("127.0.0.1" , 6379);
		System.out.println(jedis.ping());
		jedis.set("hello" , "world");
		System.out.println(jedis.get("hello"));
	}

	@Test
	public void testJedisPool() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		JedisPool jedisPool = new JedisPool(poolConfig , "127.0.0.1" , 6379);
		Jedis jedis = jedisPool.getResource();
		System.out.println(jedis.ping());
		jedis.set("hello" , "world");
		System.out.println(jedis.get("hello"));
	}

	@Test
	public void testWithoutPipeline() {
		Jedis jedis = new Jedis("127.0.0.1" , 6379);
		for(int i = 0; i < 10000 ; i++ ) {
			jedis.hset("hashKey-" + i , "field-" + i , "value-" + i);
		}
	}

	@Test
	public void testPipeline() {
		Jedis jedis = new Jedis("127.0.0.1" , 6379);
		for(int i = 0 ; i < 100 ; i++ ) {
			Pipeline pipeline = jedis.pipelined();
			for(int j = i * 100 ; i < (i+1) * 100 ; j++ ) {
				pipeline.hset("hashKey-" + i , "field-" + i , "value-" + i);
			}
			pipeline.syncAndReturnAll();
		}
	}

	@Test
	public void testSubscribe() {
		Jedis jedis = new Jedis("127.0.0.1" , 6379);
		jedis.subscribe(new JedisPubSub() {
			@Override
			public void onMessage(String channel, String message) {
				System.out.println("receive channel = [" + channel + "] message = [" + message + "]");
			}
		} , "aliTV" , "googleTV");
	}

	@Test
	public void testPublish() {
		Jedis jedis = new Jedis("127.0.0.1" , 6379);
		jedis.publish("aliTV" , "I am xuyinan");
		jedis.publish("googleTV" , "My age is 27");
	}

	@Test
	public void testSentinelPool() {
		Set<String> sentinelSet = new HashSet<String>() {{
			add("127.0.0.1:26379");
			add("127.0.0.1:26380");
			add("127.0.0.1:26381");
		}};
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		String masterName = "myMaster";
		int timeout = 30_000;
		JedisSentinelPool sentinelPool = new JedisSentinelPool(masterName , sentinelSet , poolConfig , timeout);
		Jedis jedis = sentinelPool.getResource();
		jedis.set("hello" , "world");
		System.out.println(jedis.get("hello"));
		jedis.close();
	}

	
}
