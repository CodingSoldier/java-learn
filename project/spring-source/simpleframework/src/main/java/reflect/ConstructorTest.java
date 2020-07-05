package reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorTest {
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Class<?> clazz = Class.forName("reflect.ReflectTarget");

		System.out.println("############所有公有构造方法");
		Constructor<?>[] constructors = clazz.getConstructors();
		for (Constructor c:constructors){
			System.out.println(c);
		}

		System.out.println("############所有构造方法");
		Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
		for (Constructor c:declaredConstructors){
			System.out.println(c);
		}

		System.out.println("############指定公有构造方法");
		Constructor<?> constructor = clazz.getConstructor(String.class, int.class);
		ReflectTarget c1 = (ReflectTarget)constructor.newInstance("字符串", 1);

		System.out.println("############指定公有构造方法");
		Constructor<?> constructor2 = clazz.getDeclaredConstructor(int.class);
		constructor2.setAccessible(true);
		ReflectTarget o = (ReflectTarget)constructor2.newInstance(2);
	}
}
