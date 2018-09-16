package com.jvm;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class D_BTraceTest {

    public int add(int a, int b){
        return a + b;
    }

    public static void main(String[] args) throws Exception{
        D_BTraceTest bTraceTest = new D_BTraceTest();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        for (int i=0; i<10; i++){
            reader.readLine();
            int a = (int) Math.round(Math.random() * 1000);
            int b = (int) Math.round(Math.random() * 1000);
            System.out.println(bTraceTest.add(a, b));
        }
    }

}


/*
@BTrace
public class TracingScript{
	    @OnMethod(
        clazz="com.D_BTraceTest",
        method="add",
        location=@Location(Kind.RETURN)
    )

public static void func(@Self com.D_BTraceTest instance,int a,int b,@Return int result) {
        println("调用堆栈:");
        jstack();
        println(strcat("方法参数A:",str(a)));
        println(strcat("方法参数B:",str(b)));
        println(strcat("方法结果:",str(result)));
    }
}
*/


