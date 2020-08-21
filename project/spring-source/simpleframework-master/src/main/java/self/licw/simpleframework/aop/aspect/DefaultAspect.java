package self.licw.simpleframework.aop.aspect;

import java.lang.reflect.Method;

/**
 * 定义抽象类，定义了三个钩子方法，让业务选择事前or事后的织入
 */
public abstract class DefaultAspect {
    /**
     * 事前拦截（钩子方法）
     * @param targetClass   被代理的目标类
     * @param method        被代理的目标方法
     * @param objects       被代理目标方法对应的参数列表
     * @throws Throwable    抛出异常
     */
    public void before(Class<?> targetClass, Method method,Object[] objects) throws Throwable{}

    /**
     * 被代理方法正常返回后调用
     * @param targetClass   被代理的目标类
     * @param method        被代理的目标方法
     * @param objects       被代理目标方法对应的参数列表
     * @param returnValue   被代理目标方法执行后的返回值（是原方法执行后的返回值）
     * @return
     * @throws Throwable    抛出异常
     */
    public Object afterReturning(Class<?> targetClass,Method method,Object[] objects,Object returnValue) throws Throwable{return returnValue;}

    /**
     * 被代理方法抛出异常后调用
     * @param targetClass
     * @param method
     * @param objects
     * @param e             被代理目标方法抛出的异常
     * @throws Throwable
     */
    public void afterThrowing(Class<?> targetClass,Method method,Object[] objects,Throwable e) throws Throwable{}

}
