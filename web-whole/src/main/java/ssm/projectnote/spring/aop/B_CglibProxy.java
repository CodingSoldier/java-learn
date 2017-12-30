package ssm.projectnote.spring.aop;

import org.junit.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

class B{
    public int say(){
        System.out.println("say..........");
        return 1;
    }
}
class CGLIBProxy implements MethodInterceptor{
    private  Object target;

    //返回代理对象
    public Object createProxyInstance(Object target){
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        try {
            System.out.println("方法执行前");
            result = methodProxy.invoke(this.target, objects);
            System.out.println("方法执行后");
        }catch (Exception e){
            System.out.println("发生异常");
        }
        return result;
    }
}

/**
 * http://www.cnblogs.com/jianjianyang/p/4904353.html
 */
public class B_CglibProxy {
    @Test
    public void t(){
        CGLIBProxy cglibProxy = new CGLIBProxy();
        B b = new B();
        b.say();
        b = (B)cglibProxy.createProxyInstance(b);
        int num = b.say();
        System.out.println(num);
    }

}
