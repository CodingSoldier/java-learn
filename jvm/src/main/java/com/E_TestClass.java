package com;

public class E_TestClass {
    private int m;
    public int inc(){
        return m + 1;
    }

    public int inc2(){
        Integer x;
        try {
            x = 1;
            return x;
        }catch (Exception e){
            x = 2;
            return x;
        } finally{
            x = 3;
        }
    }

    public static void main(String[] args) throws Exception{
        System.out.println(new E_TestClass().inc2());
    }

}
