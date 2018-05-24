package com.spring.iocdemo;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IOCUtil {
    //ioc容器
    Map<String, Object> beanContainer = new HashMap<String, Object>();

    //使用dom4j读取xml配置的bean，并通过反射实例化bean
    private Map<String, Object> xmlBean(String path){
        try {
            //classLoader.getResourceAsStream默认定位到classpath根目录，因为斜杠/也表示根目录，所以path不能以斜杠/开头。
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(is);
            Element beans = document.getRootElement();
            for (Iterator<Element> beanList = beans.elementIterator(); beanList.hasNext();){
                Element element = beanList.next();
                //通过bean标签的class属性，用反射实例化bean
                Object clazz = Class.forName(element.attributeValue("class")).newInstance();
                //将bean的id、实例化的bean放到容器中
                beanContainer.put(element.attributeValue("id"), clazz);
            }
        }catch (Exception e){
            System.out.println("***********出错***********");
        }
        return beanContainer;
    }

    //扫描scanPackage包中的带有@ComponentCustom注解的类
    private Map<String, Object> componentBean(String scanPackage){
        try {
            //获取扫描包路径
            String path = this.getClass().getClassLoader().getResource(scanPackage.replaceAll("\\.","/")).getPath();
            File dir = new File(path);
            //目录不存在、非目录，beanContainer不新增bean，直接返回
            if(!dir.exists() && !dir.isDirectory()){
                return beanContainer;
            }
            //筛选出目录中以class结尾的文件
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".class");
                }
            });
            for (File file: files){
                //获取文件名，不包含后缀
                String fileName = file.getName().split(".class")[0];
                //反射获取创建类
                Object clazz = Class.forName(String.format("%s.%s", scanPackage, fileName)).newInstance();
                Class classObj = clazz.getClass();
                //类是否有@ComponentCustom注解
                if(classObj.isAnnotationPresent(ComponentCustom.class)){
                    //获取@ComponentCustom注解的参数值
                    ComponentCustom component = clazz.getClass().getAnnotation(ComponentCustom.class);
                    //判断@ComponentCustom(name = "dao2")、@ComponentCustom("dao2")、 @ComponentCustom()、@ComponentCustom这4种写法。使用@ComponentCustom这种写法时，类名首字母转小写后作为key
                    String name = !"".equals(component.name()) ?
                            component.name() : !"".equals(component.value()) ?
                            component.value() : fileName.substring(0,1).toLowerCase()+fileName.substring(1);
                    //实例化后的类放到容器中
                    beanContainer.put(name, clazz);
                }
            }
        }catch (Exception e){
            System.out.println("扫描目录失败");
        }
        return beanContainer;
    }

    //IOC容器中的bean，若其成员变量使用了@ResourceCustom且IOC中有这个类，则注入，详情请看：Service1这个类
    private void injectBean(){
        try {
            for (String key: beanContainer.keySet()){
                Object bean = beanContainer.get(key);
                //获取所有成员变量
                Field[] fields = bean.getClass().getDeclaredFields();
                for (Field field:fields){
                    //判断成员变量是否包含了@ResourceCustom注解
                    if(field.isAnnotationPresent(ResourceCustom.class)){
                        //获取@ResourceCustom注解的参数值
                        ResourceCustom resource = field.getAnnotation(ResourceCustom.class);
                        //判断@ResourceCustom(name = "dao1")、@ResourceCustom("dao1")、 @ResourceCustom()、@ResourceCustom这4种写法
                        String name = !"".equals(resource.name()) ?
                                resource.name() : !"".equals(resource.value()) ?
                                resource.value() : field.getName();
                        String beanId = name != "" ? name : field.getName();
                        //通过反射给私有成员赋值
                        field.setAccessible(true);
                        field.set(bean, beanContainer.get(beanId));
                    }
                }
            }
        }catch (Exception e){
            System.out.println("bean属性注入失败");
        }
    }

    //获取IOC容器中的bean
    public Object getBean(String id){
        //读取xml中的bean
        xmlBean("com/spring/iocdemo/bean.xml");
        //扫描包中带有@ComponentCustom注解的bean
        componentBean("com.spring.iocdemo.scan");
        //使用了@ResourceCustom注解的成员变量注入值（值为实例化后的类）
        injectBean();
        //返回IOC容器中的类
        return beanContainer.get(id);
    }

    //运行此方法测试
    @Test
    public void t(){
        Service1 s = (Service1) getBean("service1");
        s.fieldMethod();
    }
}
