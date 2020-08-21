package self.licw.test.proxy.cglibproxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;


public class cglibProxyUtil {
    public static <T> T createProxy(T targetObject, MethodInterceptor methodInterceptor){
        return (T) Enhancer.create(targetObject.getClass(),methodInterceptor);
    }
}
