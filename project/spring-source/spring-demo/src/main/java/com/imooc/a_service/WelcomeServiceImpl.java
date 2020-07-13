package com.b.a_service;

/**
 * @author chenpiqian
 * @date: 2020-06-10
 */
public class WelcomeServiceImpl implements WelcomeService {

	@Override
	public String sayHello(String name) {
		System.out.println("欢迎你：" + name);
		return "success";
	}
}
