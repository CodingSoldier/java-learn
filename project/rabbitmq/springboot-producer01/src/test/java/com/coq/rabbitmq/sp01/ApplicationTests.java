//package com.coq.commonproducer.springboot;
//
//import com.coq.commonproducer.springboot.producer.RabbitSender;
//import com.example.rabbitmqbean.MyOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.text.SimpleDateFormat;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ApplicationTests {
//
//	@Test
//	public void contextLoads() {
//	}
//
//	@Autowired
//	private RabbitSender rabbitSender;
//
//	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//
//	//@Test
//	//public void testSender1() throws Exception {
//	//	 Map<String, Object> properties = new HashMap<>();
//	//	 properties.put("number", "12345");
//	//	 properties.put("send_time", simpleDateFormat.format(new Date()));
//	//	 rabbitSender.send("Hello RabbitMQ For Spring Boot!", properties);
//	//}
//
//	@Test
//	public void testSender2() throws Exception {
//		 MyOrder order = new MyOrder("2", "第2222个订单");
//		 rabbitSender.sendOrder(order);
//	}
//
//
//	@Test
//	public void testSender3() throws Exception {
//		 MyOrder order = new MyOrder("3", "第333333个订单");
//		 rabbitSender.sendOrder(order);
//	}
//
//
//	@Test
//	public void testSenderNum() throws Exception {
//		for (int i=1; i<50; i++){
//			MyOrder order = new MyOrder(String.valueOf(i), "第"+i+"个订单");
//			rabbitSender.sendOrder(order);
//		}
//	}
//
//}
