package com.learn.e_aop.a_service;

import org.springframework.stereotype.Service;

@Service
public class A_ServiceImpl implements A_Service {

	@Override
	public void sayHello() {
		System.out.println("@@@@@@@sayHello()@@@@@@@@");
	}

	@Override
	public void justThrowException() {
		throw new RuntimeException("!!!!!!!justThrowException!!!!!!");
	}
}
