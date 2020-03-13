package com.example.ee3rest;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author chenpiqian
 * @date: 2020-03-13
 */
public class TestMethod {

    class MethodBean{
        public String fn01(String message){
            return message;
        }
    }

    public static void main(String[] args) {
        Class c=MethodBean.class;

		Method[] m=c.getMethods();
		for(Method me:m) {
            Parameter[] parameters = me.getParameters();
            for (int i=0;i<parameters.length;i++){
                Parameter p = parameters[i];
                // 获取Method对象的参数列表，获取参数名
                System.out.println(p.getName());
            }
		}
    }
}
