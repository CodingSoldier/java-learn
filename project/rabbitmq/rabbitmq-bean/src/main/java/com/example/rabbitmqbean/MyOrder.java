package com.example.rabbitmqbean;

import java.io.Serializable;

public class MyOrder implements Serializable {

	private String id;
	private String name;
	
	public MyOrder() {
	}
	public MyOrder(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "MyOrder{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
