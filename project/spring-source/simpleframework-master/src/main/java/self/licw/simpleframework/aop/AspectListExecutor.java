package self.licw.simpleframework.aop;

import lombok.Getter;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import self.licw.simpleframework.aop.aspect.AspectInfo;
import self.licw.simpleframework.util.ValidationUtil;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 某个被代理类的织入过程
 * 某个被代理的类所被代理的aspect 列表 以及aspect列表中advice的执行顺序before afterreturning afterthrowing）
 *
 * targetClass 被代理的目标类
 * AspectList  该目标类下所有aspect列表，并已将@Order@Aspect信息封装好
 */
public class AspectListExecutor implements MethodInterceptor {
    //被代理的类
    private Class<?> targetClass;
    @Getter
    private List<AspectInfo> AspectList;

    public AspectListExecutor(Class<?> targetClass, List<AspectInfo> aspectList) {
        this.targetClass = targetClass;
        this.AspectList = sortAspectInfoList(aspectList);
    }

    /**
     * 按照order的值进行升序排序，确保order值小的aspect先被织入
     * @param aspectList
     * @return
     */
    private List<AspectInfo> sortAspectInfoList(List<AspectInfo> aspectList) {
        //使用java util自带的Collections工具进行排序，并传入匿名Comparator比较器作为参数，设定自定义的比较方式
        Collections.sort(aspectList, new Comparator<AspectInfo>() {
            @Override
            //return o1 - o2 是升序，return o2 - o1 是降序
            public int compare(AspectInfo o1, AspectInfo o2) {
                return o1.getOrderIndex()-o2.getOrderIndex();
            }
        });
        return aspectList;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        //对Aspect列表进行精筛
        collectAccurateMatchedAspectList(method);
        Object returnValue = null;
        if (ValidationUtil.isEmpty(AspectList)){
            //精筛后如果为空，则直接执行原来的方法
            returnValue = methodProxy.invokeSuper(o, objects);
            return returnValue;
        }
        //1.按照order升序执行所有aspect的before方法
        invokeBeforeAdvices(method,objects);
        //2.执行被代理类的方法
        try {
            returnValue = methodProxy.invokeSuper(o, objects);
            //3.如果被代理方法正确返回，按order降序执行所有aspect的afterreturning方法
            returnValue = invokeAfterReturningAdvices(method,objects,returnValue);
            return returnValue;
        }catch (Exception e){
            //4.如果被代理方法抛出异常，按order降序执行所有aspect的afterthrowing方法
            invokeAfterThrowingAdvices(method,objects,e);
        }
        return returnValue;
    }

    /**
     * 把该class下对应的method跟Aspect列表进行精筛
     * @param method
     */
    private void collectAccurateMatchedAspectList(Method method) {
        if (ValidationUtil.isEmpty(AspectList)){return;}
        //使用Iterator 因为在后边remove时，可以同时删除原列表和遍历列表副本
        //如果使用foreach 在删除的时候会抛错
        Iterator<AspectInfo> it = AspectList.iterator();
        while(it.hasNext()){
            AspectInfo aspectInfo = it.next();
            if (!aspectInfo.getPointcutLocator().accurateMatches(method)){
                it.remove();
            }
        }

    }


    private void invokeBeforeAdvices(Method method, Object[] objects) throws Throwable {
        for (AspectInfo aspectInfo:AspectList){
            aspectInfo.getAspect().before(targetClass,method,objects);
        }
    }

    private Object invokeAfterReturningAdvices(Method method, Object[] objects, Object returnValue) throws Throwable {
        Object res = null;
        for (int i=AspectList.size() -1;i>=0;i--){
            res = AspectList.get(i).getAspect().afterReturning(targetClass,method,objects,returnValue);
        }
        return res;
    }

    private void invokeAfterThrowingAdvices(Method method, Object[] objects, Exception e) throws Throwable {
        for (int i=AspectList.size() -1;i>0;i--){
            AspectList.get(i).getAspect().afterReturning(targetClass,method,objects,e);
        }
    }
}
