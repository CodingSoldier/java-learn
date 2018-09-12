package com;

//P187
public class E_TestClass {
    private int m;
    public int inc(){
        return m + 1;
    }

    public int inc2(){
        Integer x;
        try {
            x = 1;
            return x;  //将 1 放到slot中。执行return指令时，会读取slot，将slot中的值放到操作数栈顶
        }catch (Exception e){
            x = 2;
            return x;  //再次改变slot中的值
        } finally{
            x = 3;  //改变x的值不影响返回值，以为返回值已经存储到slot中，未改变slot
        }
    }

    public static void main(String[] args) throws Exception{
        System.out.println(new E_TestClass().inc2());
    }

}
