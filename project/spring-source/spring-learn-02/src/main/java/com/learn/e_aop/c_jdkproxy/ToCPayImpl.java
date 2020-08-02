package com.learn.e_aop.c_jdkproxy;

public class ToCPayImpl implements ToCPay {
	@Override
	public void pay() {
		System.out.println("#####to c pay");
	}
}
