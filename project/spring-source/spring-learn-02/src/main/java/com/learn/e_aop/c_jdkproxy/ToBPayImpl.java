package com.learn.e_aop.c_jdkproxy;

public class ToBPayImpl implements ToBPay {
	@Override
	public void pay() {
		System.out.println("###to b pay");
	}
}
