package com.jvm;

public class G_Init {
    public static int value = 123;
    public G_Init() {
        System.out.println("构造器初始化");
    }

}

class C1{
    public static void main(String[] args){
        System.out.println(G_Init.value);
    }
}
