package self.licw.simpleframework.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import self.licw.simpleframework.aop.annotation.Aspect;
import self.licw.simpleframework.core.annotation.Component;
import self.licw.simpleframework.core.annotation.Controller;
import self.licw.simpleframework.core.annotation.Repository;
import self.licw.simpleframework.core.annotation.Service;
import self.licw.simpleframework.util.ClassUtil;
import self.licw.simpleframework.util.ValidationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanContainer {
    /**
     * 设置该容器是否已经被加载过
     */
    private boolean loaded = false;

    public boolean isLoaded(){
        return loaded;
    }

    /**
     * 存放所有被配置标记的目标对象的Map
     * ConcurrentHashMap 能更好地支持并发
     * key:class对象    value:对象的实例
     */
    private final Map<Class<?>,Object> beanMap = new ConcurrentHashMap();

    /**
     * 加载bean的注解列表，标识了该注解的类（bean）都要被容器所管理
     */
    private static final List<Class<? extends Annotation>> BEAN_ANNOTATION = Arrays.asList(Component.class, Controller.class, Repository.class, Service.class, Aspect.class);


    /**
     * 获取bean容器实例
     * @return
     */
    public static BeanContainer getInstance(){
        return ContainerHolder.HOLDER.instance;
    }

    private enum ContainerHolder{
        HOLDER;
        private BeanContainer instance;
        ContainerHolder(){
            instance = new BeanContainer();
        }
    }

    /**
     * 扫描加载指定包名下的所有bean
     * 方法中使用同步锁synchronized 这样就能保证该方法只有一个线程在执行，从而保证loadbean只会被执行一次
     * @param basepackage
     */
    public synchronized void loadBeans(String basepackage){
        if (isLoaded()){
            log.warn("beancontainer has been loaded.");
            return;
        }
        Set<Class<?>> classSet = ClassUtil.extractPackageClass(basepackage);
        if (ValidationUtil.isEmpty(classSet)){
            log.warn("nothing from package" + basepackage);
            return;
        }
        for (Class<?> clazz:classSet){
            //判断类是否标记了定义的注解
            for (Class<? extends Annotation> annotation:BEAN_ANNOTATION){
                //标记了则将目标类本身作为键，目标类的实例作为值，放入到beanMap中
                if (clazz.isAnnotationPresent(annotation)){
                    beanMap.put(clazz,ClassUtil.newInstance(clazz,true));
                }
            }
        }
        loaded = true;
    }


    /*
        以下是IOC容器的操作方法
    */


    /**
     * 添加一个class对象以及其bean实例
     * @param clazz class对象
     * @param object class对象的bean实例
     * @return 原来的bean实例，因为map的put会返回value里边的值
     */
    public Object addBean(Class<?> clazz,Object object){
        return beanMap.put(clazz, object);
    }

    /**
     * 移除一个IOC容器管理的对象
     * @param clazz class对象
     * @return class对象的bean实例
     */
    public Object removeBean(Class<?> clazz){
        return beanMap.remove(clazz);
    }

    /**
     * 根据class对象获取bean实例
     * @param clazz class对象
     * @return  bean实例
     */
    public Object getBean(Class<?> clazz){
        return beanMap.get(clazz);
    }

    /**
     * 获取容器管理的所有class对象集合
     * @return class对象集合
     */
    public Set<Class<?>> getClasses(){
        return beanMap.keySet();
    }

    /**
     * 获取所有bean实例集合
     * @return  bean集合
     */
    public Set<Object> getBeans(){
        //因为beanMap.values返回的是Collection 集合类型 所以将其转为HashSet集合
        return new HashSet<>(beanMap.values());
    }

    /**
     * 根据注解筛选出bean的class集合
     * @param annotation 要筛选的注解
     * @return class集合
     */
    public Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation){
        //获取容器里边的所有class对象
        Set<Class<?>> keySet = getClasses();
        if (ValidationUtil.isEmpty(keySet)){
            log.warn("nothing in beanMap");
            return null;
        }

        //通过注解筛选出被注解标记的class对象，并添加到classset中
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz:keySet){
            if (clazz.isAnnotationPresent(annotation)){
                classSet.add(clazz);
            }
        }
        //如果size=0返回null即可
        return classSet.size()>0?classSet:null;
    }

    /**
     * 根据接口或者父类筛选出bean的class集合,不包括其本身
     * @param interfaceOrClass
     * @return
     */
    public Set<Class<?>> getClassesBySuper(Class<?> interfaceOrClass){
        //获取容器里边的所有class对象
        Set<Class<?>> keySet = getClasses();
        if (ValidationUtil.isEmpty(keySet)){
            log.warn("nothing in beanMap");
            return null;
        }

        //判断keyset里的元素是否是传入的接口或者是类的子类
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz:keySet){
            //a.isAssignableFrom(b) 判断a是否是b的父类(也包括自己本身），a接口是否继承b接口   或者b是否实现了a的接口(也包括a接口等于b接口）
            if (interfaceOrClass.isAssignableFrom(clazz) && !classSet.equals(interfaceOrClass)){
                classSet.add(clazz);
            }
        }
        return classSet.size()>0?classSet:null;
    }

    /**
     * 返回容器的bean实例数量
     * @return
     */
    public int size(){
        return beanMap.size();
    }
}
