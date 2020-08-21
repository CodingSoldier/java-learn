package self.licw.test.annotation;

import java.lang.reflect.Field;

public class Test {
    @PersonInfoAnnotation(name = "haha",language = {"1","2"})
    private String author;

    public static void parseFieldAnnotation() throws ClassNotFoundException {
        Class clazz = Class.forName("self.licw.test.annotation.Test");
        Field[] fields = clazz.getDeclaredFields();
        for (Field f:fields){
            //判断成员变量中是否有指定注解类型的注解
            boolean hasAnnotation = f.isAnnotationPresent(PersonInfoAnnotation.class);
            if (hasAnnotation){
                PersonInfoAnnotation personInfoAnnotation = f.getAnnotation(PersonInfoAnnotation.class);
                System.out.println(personInfoAnnotation.age() + "\n" + personInfoAnnotation.name() + "\n");
            }
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
       parseFieldAnnotation();
    }
}
