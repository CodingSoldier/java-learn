package com;

public class C_Bar {
    int a = 1;
    static int b = 2;
    public int sum(int c){
        return a + b + c;
    }
    public static void main(String[] args){
        new C_Bar().sum(3);
    }
}

// 113 无法演示
// java -XX:+PrintAssembly -Xcomp -XX:CompileCommand=dontinline,*Bar.sum -XX:CompileCommand=compileonly,*Bar.sum test.Bar