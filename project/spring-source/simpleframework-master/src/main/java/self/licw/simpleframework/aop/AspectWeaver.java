package self.licw.simpleframework.aop;

import self.licw.simpleframework.aop.annotation.Aspect;
import self.licw.simpleframework.aop.annotation.Order;
import self.licw.simpleframework.aop.aspect.AspectInfo;
import self.licw.simpleframework.aop.aspect.DefaultAspect;
import self.licw.simpleframework.core.BeanContainer;
import self.licw.simpleframework.util.ValidationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 按业务逻辑对不同的类织入不同的aspect
 * 从bean容器里边获得被代理类 aspect 以及aspect织入被代理类生成代理类实例等过程
 */
public class AspectWeaver {
    private BeanContainer beanContainer;
    public AspectWeaver(){
        this.beanContainer = BeanContainer.getInstance();
    }

    public void doAop(){
        //1.获取所有切面类 ASPECT标签标记的类
        Set<Class<?>> aspectSet = beanContainer.getClassesByAnnotation(Aspect.class);
        if (ValidationUtil.isEmpty(aspectSet)){return;}
        //2.拼装AspectInfoList
        List<AspectInfo> aspectInfoList = packAspectInfoList(aspectSet);
        //3.遍历容器里的类
        Set<Class<?>> classSet = beanContainer.getClasses();
        for (Class<?> targetClass:classSet){
            //排除AspectClass自身
            if (targetClass.isAnnotationPresent(Aspect.class)){
                continue;
            }
            //4.对当前classes进行符合条件的Aspect的初筛,将符合该目标类的aspect放到一个初晒列表中，过滤掉一些无用的aspect类
            List<AspectInfo> roughMatchedAspectedeList = collectRoughAspect(aspectInfoList,targetClass);
            //5.利用进行初筛后的aspect列表尝试对该class进行aspect的织入
            wrapIfNecessary(roughMatchedAspectedeList,targetClass);
        }
    }

    private void wrapIfNecessary(List<AspectInfo> roughMatchedAspectedeList, Class<?> targetClass) {
        if (ValidationUtil.isEmpty(roughMatchedAspectedeList)){return;}
        //创建动态代理对象
        AspectListExecutor aspectListExecutor = new AspectListExecutor(targetClass,roughMatchedAspectedeList);
        Object proxyBean = ProxyCreator.createProxy(targetClass,aspectListExecutor);
        beanContainer.addBean(targetClass,proxyBean);
    }

    private List<AspectInfo> collectRoughAspect(List<AspectInfo> aspectInfoList, Class<?> targetClass) {
        List<AspectInfo> roughMatchedAspectList = new ArrayList<>();
        for (AspectInfo aspectInfo : aspectInfoList){
            if (aspectInfo.getPointcutLocator().roughMatches(targetClass)){
                roughMatchedAspectList.add(aspectInfo);
            }
        }
        return roughMatchedAspectList;
    }

    private List<AspectInfo> packAspectInfoList(Set<Class<?>> aspectSet) {
        List<AspectInfo> aspectInfos = new ArrayList<>();
        for (Class<?> aspectClass:aspectSet){
            if (verifyAspect(aspectClass)){
                Order orderTag = aspectClass.getAnnotation(Order.class);
                Aspect aspectTag = aspectClass.getAnnotation(Aspect.class);
                DefaultAspect defaultAspect = (DefaultAspect) beanContainer.getBean(aspectClass);
                //初始化表达式定位器
                PointcutLocator pointcutLocator = new PointcutLocator(aspectTag.pointcut());
                AspectInfo aspectInfo = new AspectInfo(orderTag.value(),defaultAspect,pointcutLocator);
                aspectInfos.add(aspectInfo);
            }else{
                throw new RuntimeException("aspect error");
            }
        }
        return aspectInfos;
    }

    private boolean verifyAspect(Class<?> aspectClass) {
        return aspectClass.isAnnotationPresent(Aspect.class) &&
                aspectClass.isAnnotationPresent(Order.class) &&
                DefaultAspect.class.isAssignableFrom(aspectClass);
    }


}
