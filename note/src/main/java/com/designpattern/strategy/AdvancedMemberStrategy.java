package com.designpattern.strategy;

public class AdvancedMemberStrategy implements MemberStrategy{
    @Override
    public double calcPrice(double bookPrice) {
        System.out.println("对于高级会员的折扣是20%");
        return bookPrice * 0.8;
    }
}
