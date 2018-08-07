package com.designpattern.strategy;

public class Client {
    public static void main(String[] args) {
        MemberStrategy strategy = new AdvancedMemberStrategy();
        Price price = new Price(strategy);
        double quote = price.quote(300);
    }
}
