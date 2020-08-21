package self.licw.o2o.aspect;

import lombok.extern.slf4j.Slf4j;
import self.licw.simpleframework.aop.annotation.Aspect;
import self.licw.simpleframework.aop.annotation.Order;
import self.licw.simpleframework.aop.aspect.DefaultAspect;
import self.licw.simpleframework.core.annotation.Controller;

import java.lang.reflect.Method;

@Slf4j
@Aspect(pointcut = "within(self.licw.o2o.controller.superadmin.*)")
@Order(1)
public class ControllerRecordAspect extends DefaultAspect {
    @Override
    public void before(Class<?> targetClass, Method method, Object[] objects) throws Throwable {
        log.info("开始执行，执行的类是【{}】  执行的方法是【{}】  方法的参数是【{}】",targetClass,method,objects);
    }

    @Override
    public Object afterReturning(Class<?> targetClass, Method method, Object[] objects, Object returnValue) throws Throwable {
        log.info("顺利完成，执行的类是【{}】  执行的方法是【{}】  方法的参数是【{}】",targetClass,method,objects);
        return returnValue;
    }

    @Override
    public void afterThrowing(Class<?> targetClass, Method method, Object[] objects, Throwable e) throws Throwable {
        log.info("抛出异常，执行的类是【{}】  执行的方法是【{}】  方法的参数是【{}】",targetClass,method,objects);
    }
}
