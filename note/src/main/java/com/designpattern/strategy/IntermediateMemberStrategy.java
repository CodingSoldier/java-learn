package com.designpattern.strategy;

public class IntermediateMemberStrategy implements MemberStrategy {
    @Override
    public double calcPrice(double bookPrice) {
        System.out.println("对于中级会员的折扣为10%");
        return bookPrice * 0.9;
    }
}
