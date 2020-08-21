package self.licw.simpleframework.inject;

import lombok.extern.slf4j.Slf4j;
import self.licw.simpleframework.core.BeanContainer;
import self.licw.simpleframework.inject.annotation.Autowired;
import self.licw.simpleframework.util.ClassUtil;
import self.licw.simpleframework.util.ValidationUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Set;

@Slf4j
public class DependencyInjector {
    /**
     * bean容器
     */
    private BeanContainer beanContainer;

    //在加载该类时就把bean容器实例取得
    public DependencyInjector(){
        beanContainer = BeanContainer.getInstance();
    }

    /**
     * 执行依赖注入
     * 在spring框架中 当遇到一个类中有多个实现类，提供了@Qualifier的注解指定bean实例。
     * 本自研框架为了简便，直接在autowired中新增value方法，指定多实现类时所用的bean实例
     */
    public void doIoc(){
        if (ValidationUtil.isEmpty(beanContainer.getClasses())){
            log.warn("nothing in beancontainer");
            return;
        }
        //遍历bean容器中所有class对象
        for (Class<?> clazz:beanContainer.getClasses()){
            //遍历class对象的所有成员变量
            Field[] fields = clazz.getDeclaredFields();
            if (ValidationUtil.isEmpty(fields)){
                continue;
            }
            for (Field field:fields){
                //找出被autowired标记的成员变量
                if (field.isAnnotationPresent(Autowired.class)){

                    /*
                    自研框架中，获取autowire注解实例中的value值，该值指定了注解所注入的bean实例
                     */
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String autowiredValue = autowired.value();

                    //获取这些成员变量的类型
                    Class<?> fieldclass = field.getType();
                    //获取这些成员变量的类型在容器里对应的实例
                    Object fieldValue = getFileInstance(fieldclass,autowiredValue);
                    if (fieldValue == null){
                        throw new RuntimeException("unable autowired" + fieldclass.getClasses());
                    }else{
                        //通过反射将对应的成员变量实例注入到成员变量所在类的实例里
                        Object targetBean = beanContainer.getBean(clazz);
                        ClassUtil.setField(field,targetBean,fieldValue,true);
                    }
                }
            }
        }
    }

    private Object getFileInstance(Class<?> fieldclass,String autowiredValue) {
        //先尝试获取类的bean
        Object fieldValue = beanContainer.getBean(fieldclass);
        if (fieldValue != null){
            return fieldValue;
        }else {
            //获取注解标注在接口上的对应的接口实现类的实例  或  继承了filedclass类的实现类实例
            //在spring框架中 当遇到一个类中有多个实现类，提供了@Qualifier的注解指定bean实例
            Class<?> implementClass = getImplementClass(fieldclass,autowiredValue);
            if (implementClass != null){
                return beanContainer.getBean(implementClass);
            }else {
                return null;
            }
        }
    }

    private Class<?> getImplementClass(Class<?> fieldclass,String autowiredValue) {
        Set<Class<?>> classSet = beanContainer.getClassesBySuper(fieldclass);
        if (!ValidationUtil.isEmpty(classSet)){
            //判断是否为默认值"" 即用户没有指定具体的实现类
            if (ValidationUtil.isEmpty(autowiredValue)){
                if (classSet.size() == 1){
                    return classSet.iterator().next();
                }else{
                    //多于两个实现类且用户未指定实现类 抛出异常
                    log.error("mutile implement classes for " + fieldclass.getName());
                    throw new RuntimeException("mutile implement classes for " + fieldclass.getName());
                }
            }else{
                //循环遍历子类
                for(Class<?> clazz:classSet){
                    //如果子类或实现类的实例等于用户传入的名称，则返回
                    if (autowiredValue.equals(clazz.getSimpleName())){
                        return clazz;
                    }
                }
            }
        }
        return null;
    }
}
