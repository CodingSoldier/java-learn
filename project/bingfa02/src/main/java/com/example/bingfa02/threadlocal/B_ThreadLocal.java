package com.example.bingfa02.threadlocal;

import com.sun.org.apache.xpath.internal.operations.String;
import lombok.Data;

import java.util.Random;

public class B_ThreadLocal {
}


class MyRandom{
    /**
     * ThreadLocal设置初始值。每次执行MyRandom.random都会执行initialValue()。这类似一种延迟加载的功能，在调用方法的时候才创建对象、返回对象
     */
    public static ThreadLocal<Integer> random = new ThreadLocal(){
        @Override
        protected Integer initialValue() {
            return new Random().nextInt(10);
        }
    };
}

class MyRandomTest{
    public static void main(String[] args) {
        Integer num = MyRandom.random.get();
        System.out.println(num);
    }
}





@Data
class Request{
    private String data;

    public Request(String data) {
        this.data = data;
    }
}
class RequestContextHolder {
    public static ThreadLocal<Request> holder = new ThreadLocal<>();
}

class Servlet {
    public void process(String data){
        Request request = new Request(data);
        // 线程局部变量设置值
        RequestContextHolder.holder.set(request);
        System.out.println("Servlet.request: "+request);
        new Controller().process();
    }
}
class Controller {
    public void process(){
        // 获取线程局部变量值
        Request request = RequestContextHolder.holder.get();
        System.out.println("Controller.request: "+request);
        new Service().process();
    }
}
class Service {
    public void process(){
        Request request = RequestContextHolder.holder.get();
        System.out.println("Service.request: "+request);

        // 最后要移除线程局部变量，避免内存溢出
        RequestContextHolder.holder.remove();
    }
}

class HolderTest {
    public static void main(String[] args) {
        new Servlet().process("http请求");
        new ThreadLocal<>().set();

        Object a = new Object();
        Object b = new Object();
        new ThreadLocal<>().set(obj1);
        new ThreadLocal<>().set(obj2);

        new ThreadLocal<>().set(obj1);
        new ThreadLocal<>().set(obj1);
    }





}










