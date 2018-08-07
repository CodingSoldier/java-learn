package com.designpattern.template;

public abstract class Account {
    public final double calculateInterest(){
        double interestRate = doCalculateInterestRate();
        String accountType = doCalculateAccountType();
        double amount=calculateAmount(accountType);
        return amount * interestRate;
    }

    protected abstract String doCalculateAccountType();

    protected abstract double doCalculateInterestRate();

    private double calculateAmount(String accountType){
        return 7243.00;
    }
}
