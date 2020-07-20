package com.learn.b_ioc_learn.factory;

import com.learn.b_ioc_learn.User;
import org.springframework.beans.factory.FactoryBean;


public class PrefixUserFactoryBean implements FactoryBean<User> {

	@Override
	public User getObject() throws Exception {
		return new User();
	}

	@Override
	public Class<?> getObjectType() {
		return User.class;
	}

}
