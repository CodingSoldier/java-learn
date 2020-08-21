package self.licw.o2o.aspect;

import lombok.extern.slf4j.Slf4j;
import self.licw.simpleframework.aop.annotation.Aspect;
import self.licw.simpleframework.aop.annotation.Order;
import self.licw.simpleframework.aop.aspect.DefaultAspect;
import self.licw.simpleframework.core.annotation.Controller;

import java.lang.reflect.Method;

@Aspect(pointcut = "execution(* self.licw.o2o.controller.frontend..*.*(..))")
@Order(2)
@Slf4j
public class ControllerTimeAspect extends DefaultAspect {
    private long begin_time;
    @Override
    public void before(Class<?> targetClass, Method method, Object[] objects) throws Throwable {
      log.info("开始计时，执行的类是【{}】  执行的方法是【{}】  方法的参数是【{}】",targetClass,method,objects);
        begin_time = System.currentTimeMillis();
    }

    @Override
    public Object afterReturning(Class<?> targetClass, Method method, Object[] objects, Object returnValue) throws Throwable {
        long end_time = System.currentTimeMillis();
        long cost_time = end_time - begin_time;
        log.info("结束计时，执行的类是【{}】  执行的方法是【{}】  方法的参数是【{}】执行时间是【{}】",targetClass,method,objects,cost_time);
        return returnValue;
    }
}
