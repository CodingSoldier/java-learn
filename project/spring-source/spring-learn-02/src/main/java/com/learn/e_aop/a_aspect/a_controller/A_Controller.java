package com.learn.e_aop.a_aspect.a_controller;

import com.learn.e_aop.a_aspect.a_service.A_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class A_Controller {

	@Autowired
	A_Service a_service;

	public void test01(){
		a_service.sayHello();
		a_service.justThrowException();
	}

}
