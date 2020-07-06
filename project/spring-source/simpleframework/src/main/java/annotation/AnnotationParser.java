package annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AnnotationParser {

	public static void parseTypeAnnotation() throws ClassNotFoundException {
		Class clazz = Class.forName("annotation.ImoocCourse");
		//这里获取的是class对象的注解，而不是其里面的方法和成员变量的注解
		Annotation[] annotations = clazz.getAnnotations();
		for(Annotation annotation : annotations){
			CourseInfoAnnotation courseInfoAnnotation =  (CourseInfoAnnotation) annotation;
			System.out.println("课程名:" + courseInfoAnnotation.courseName() + "\n" +
					"课程标签：" + courseInfoAnnotation.courseTag() + "\n" +
					"课程简介：" + courseInfoAnnotation.courseProfile() + "\n" +
					"课程序号：" + courseInfoAnnotation.courseIndex() );
		}
	}

	public static void parseFieldAnnotation() throws ClassNotFoundException {
		Class<?> clazz = Class.forName("annotation.ImoocCourse");
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field:declaredFields){
			boolean hasAnnotation = field.isAnnotationPresent(PersonInfoAnnotation.class);
			if (hasAnnotation){
				PersonInfoAnnotation personinfoannotation = field.getAnnotation(PersonInfoAnnotation.class);
				System.out.println("名字："+personinfoannotation.name());
				System.out.println("年龄："+personinfoannotation.age());
			}
		}
	}

	public static void parseMethodAnnotaition() throws ClassNotFoundException {
		Class<?> clazz = Class.forName("annotation.ImoocCourse");
		Method[] declaredMethods = clazz.getDeclaredMethods();
		for (Method method:declaredMethods){
			boolean hasAnnotation = method.isAnnotationPresent(CourseInfoAnnotation.class);
			if (hasAnnotation){
				CourseInfoAnnotation annotation = method.getAnnotation(CourseInfoAnnotation.class);
				System.out.println("课程名 "+annotation.courseName());
			}
		}
	}

	public static void main(String[] args) throws ClassNotFoundException {
		//parseTypeAnnotation();
		parseFieldAnnotation();
		//parseMethodAnnotaition();
		/**
		  配置 -Dsun.misc.ProxyGenerator.saveGeneratedFiles=true 输出代理类
		  $Proxy1.class中的super.h.invoke，super.h是 sun.reflect.annotation.AnnotationInvocationHandler 代理类，创建动态代理对象实例，调用invoke()方法实现动态代理

		  -Dsun.misc.ProxyGenerator.saveGeneratedFiles=true -XX:+TraceClassLoading

		 */
	}

}
