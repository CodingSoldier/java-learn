package com;


class A_RCGC{

    public Object instance = null;
    private static final int _1mb = 1024 *1024;

    //VM options加上 -verbose:gc
    public static void main(String[] args){
        A_RCGC rcgcA = new A_RCGC();
        A_RCGC rcgcB = new A_RCGC();

        rcgcA.instance = rcgcB;
        rcgcB.instance = rcgcA;

        rcgcA = null;
        rcgcB = null;

        System.gc();
    }

}
