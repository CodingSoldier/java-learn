package com.learn.d_create_bean.mode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@Scope("prototype")
public class Staff {
	private Company company;

	@Autowired
	public Staff(Company company) {
		//this.company = company;
	}
}
