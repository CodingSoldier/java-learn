package com.atguigu.gmall.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.service.OrderService;
import com.atguigu.gmall.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * 1、将服务提供者注册到注册中心（暴露服务）
 * 		1）、导入dubbo依赖（2.6.2）\操作zookeeper的客户端(curator)
 * 		2）、配置服务提供者
 * 
 * 2、让服务消费者去注册中心订阅服务提供者的服务地址
 * @author lfy
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	/**
	 * @Reference表示dubbo的远程调用类
	 *
	 * url = "127.0.0.1:8081"
	 * dubbo直连，填写dubbo的地址
	 * zk（注册中心）宕机，消费者也能通过本地缓存连接提供者
	 * 直连无法使用负载均衡策略，因为没有了注册中心
	 *
	 * 负载均衡：random 随机、roundrobin 轮询、leastactive 最少活跃调用数、consistenthash 一致性Hash
	 * https://dubbo.apache.org/zh/docs/v2.7/user/examples/loadbalance/#m-zhdocsv27userexamplesloadbalance
	 *
	 * 设置权重可在dubbo控制台，应用 -> 提供者 -> 倍权、半权
	 */
	// @Reference(url = "127.0.0.1:20880")
	@Reference(loadbalance = "roundrobin")
	// @Reference
	UserService userService;
	
	@HystrixCommand(fallbackMethod="initOrderFallBack")
	@Override
	public List<UserAddress> initOrder(String userId) {
		// TODO Auto-generated method stub
		System.out.println("用户id："+userId);
		//1、查询用户的收货地址
		List<UserAddress> addressList = userService.getUserAddressList(userId);
		return addressList;
	}
	
	
	public List<UserAddress> initOrderFallBack(String userId) {
		// TODO Auto-generated method stub
	
		return Arrays.asList(new UserAddress(10, "Hystrix-fallback", "1", "测试", "测试", "Y"));
	}
	

}
