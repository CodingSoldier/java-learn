package com.example.rabbitmqbean;

import java.io.Serializable;

public class MyOrder2 implements Serializable {

	private String id;
	private String name;

	public MyOrder2() {
	}
	public MyOrder2(String id, String name) {
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
