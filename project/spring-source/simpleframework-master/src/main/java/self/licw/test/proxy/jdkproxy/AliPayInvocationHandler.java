package self.licw.test.proxy.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

//代理类必须实现invocationHandler接口，并实现里边invoke方法，也就是代理的方法
public class AliPayInvocationHandler implements InvocationHandler {
    private Object targetObject;

    public AliPayInvocationHandler(Object targetObject) {
        this.targetObject = targetObject;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        before();
        Object result = method.invoke(targetObject,objects);
        after();
        return result;
    }

    private void after() {
        System.out.println("给淘宝");
    }

    private void before() {
        System.out.println("银行取款");
    }
}
