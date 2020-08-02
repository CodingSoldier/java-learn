package com.learn.b_bean.factory;

import com.learn.a_start.User;
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
