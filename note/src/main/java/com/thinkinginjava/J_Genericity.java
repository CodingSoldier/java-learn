package com.thinkinginjava;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class J_Genericity {}

class ClassAP<T> {}

class ClassA<T> extends ClassAP{
    private T obj;
    public T getObject() {
        return obj;
    }
    public void setObject(T obj) {
        this.obj = obj;
    }
}
class Test<T> extends ClassA<T> {
    public static void main(String args[]) throws Exception {
        Class<ClassA> cac = ClassA.class;
        ClassA ca = cac.newInstance();
        Class<? super ClassA> up = cac.getSuperclass();
        //Class<ClassAP> ap = cac.getSuperclass();
    }
}







/**  P340，静态代理
 *   1、真实类RealObj和代理类都实现同一个接口
 *   2、在代理类中注入真实类，将真实类向上转型为接口，则代理类可以注入多种真实类
 *   3、代理类方法中执行真实类方法（由于代理类和真实类都实现同一个接口，所以方法名是一样的）
 */
interface Interface{
    void doSomething();
    void doSomethingElse(String arg);
}
class RealObj implements Interface{
    @Override
    public void doSomething() {
        System.out.println("RealObj#doSomething");
    }
    @Override
    public void doSomethingElse(String arg) {
        System.out.println("RealObj#doSomethingElse  "+arg);
    }
}
class SimpleProxy implements Interface{
    private Interface target;
    public SimpleProxy(Interface target) {
        this.target = target;
    }
    @Override
    public void doSomething() {
        System.out.println("SimpleProxy#doSomething");
        target.doSomething();
    }
    @Override
    public void doSomethingElse(String arg) {
        System.out.println("SimpleProxy#doSomethingElse   "+arg);
        target.doSomethingElse(arg);
    }
}
class T1{
    public static void main(String[] args) {
        Interface realObj = new RealObj();
        realObj.doSomething();
        realObj.doSomethingElse("参数");
        System.out.println("***********************************");

        Interface simpleProxy = new SimpleProxy(new RealObj());
        simpleProxy.doSomething();
        simpleProxy.doSomethingElse("参数");
    }
}


/**  P340  动态代理
 *
 */
interface Interface1{
    void doSomething1();
    void doSomethingElse1(String arg);
}
class RealObj1 implements Interface1{
    @Override
    public void doSomething1() {
        System.out.println("RealObj#doSomething");
    }
    @Override
    public void doSomethingElse1(String arg) {
        System.out.println("RealObj#doSomethingElse  "+arg);
    }
}
class DynamicProxyHandler implements InvocationHandler {
    private Object proxied;
    public DynamicProxyHandler(Object proxied) {
        this.proxied = proxied;
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("**** proxy: " + proxy.getClass() + ", method: " + method + ", args: " + args);

        Object ret = method.invoke(proxied, args);
        return ret;
    }
}
class E22_SimpleDynamicProxyDemo {
    public static void main(String[] args) {
        /**  Proxy.newProxyInstance()实现方式类似于通过 Constructor.newInstance(new Object[]{outFill}) 来实现
         Class c=Class.forName("A");
         //{ InvocationHandler.class }是new Class[]{InvocationHandler.class} 的简写，是一个元素为Class类型的数组
         Constructor c1=c.getDeclaredConstructor({ InvocationHandler.class });
         c1.setAccessible(true);
         //通过  构造函数.newInstance 来创建实体对象，实体对象类型是DynamicProxyHandler
         A a1=(A)c1.newInstance(new Object[]{new DynamicProxyHandler(real)});

         Interface1.class.getClassLoader()   一个类加载器
         new Class<?>[]{ Interface1.class }   代理接口列表
         new DynamicProxyHandler(new RealObj1())  //InvocationHandler接口实现类实例
         */
        Interface1 proxy = (Interface1)Proxy.newProxyInstance(
                Interface1.class.getClassLoader(),
                new Class<?>[]{ Interface1.class },
                new DynamicProxyHandler(new RealObj1()));
        proxy.doSomething1();
        proxy.doSomethingElse1("   111111  ");
    }
}













//P347
interface I{
    void f();
}
class II implements I{
    @Override
    public void f() {

    }

    protected void f2(){
        System.out.println("f2");
    }
}










