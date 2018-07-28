package com.designpattern.builder.insurance;

public class Client {
    public static void main(String[]args){
       InsuranceContract.ConcreteBuilder builder = new InsuranceContract.ConcreteBuilder("9567", 123L, 55L);
       InsuranceContract contract = builder.setPersonName("小明").setOtherData("test").build();
       contract.someOperation();
    }
}
