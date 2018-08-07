package com.designpattern.strategy;

public class PrimaryMemberStrategy implements MemberStrategy {
    @Override
    public double calcPrice(double bookPrice) {
        System.out.println("初级会员没有折扣");
        return bookPrice;
    }
}
