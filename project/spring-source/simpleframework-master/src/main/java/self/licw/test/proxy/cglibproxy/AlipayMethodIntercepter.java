package self.licw.test.proxy.cglibproxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class AlipayMethodIntercepter implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        before();
        Object object = methodProxy.invokeSuper(o,objects);
        after();
        return object;
    }

    private void after() {
        System.out.println("给淘宝");
    }

    private void before() {
        System.out.println("银行取款");
    }
}
