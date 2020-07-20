package com.learn.a_start;

import org.springframework.stereotype.Service;

/**
 * @author chenpiqian
 * @date: 2020-06-10
 */
@Service
public class WelcomeServiceImpl implements WelcomeService {

	@Override
	public String sayHello(String name) {
		System.out.println("欢迎你：" + name);
		return "success";
	}
}
