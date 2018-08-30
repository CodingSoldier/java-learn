package com.demo.jvm;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/jvm")
public class D_BTraceTest {

    public int add(int a, int b){

        return a + b;
    }

    public static int trace(Integer num) {
        D_BTraceTest bTraceTest = new D_BTraceTest();
        //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //for (int i=0; i<10; i++){
            //reader.readLine();
            int a = (int) Math.round(Math.random() * 1000);
            int b = (int) Math.round(Math.random() * 1000);
            int r = bTraceTest.add(a, b);
            System.out.println(r);
        //}
        return r;
    }

    @GetMapping("/btrace")
    public Integer btrace(HttpServletRequest request, HttpServletResponse response) throws Exception{

        return trace(1000);

    }

}


/*@BTrace
public class TracingScript{
	    @OnMethod(
        clazz="com.demo.jvm.D_BTraceTest",
        method="add",
        location=@Location(Kind.RETURN)
    )

public static void func(@Self com.demo.jvm.D_BTraceTest instance,int a,int b,@Return int result) {
	    int aa = 0;
	     //btrace调试代码报错不影响目标代码，但是不允许这样做，只能使用BTrace提供的方法
	    // 比如：println()
	    int bb = 1/aa;
        println("调用堆栈:");
        jstack();
        println(strcat("方法参数A:",str(a)));
        println(strcat("方法参数B:",str(b)));
        println(strcat("方法结果:",str(result)));
    }
}*/


