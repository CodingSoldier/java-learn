package reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class FieldTest {
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
		Class<?> clazz = Class.forName("reflect.ReflectTarget");

		System.out.println("############所有公有字段");
		Field[] fields = clazz.getFields();
		for (Field f:fields){
			System.out.println(f);
		}

		System.out.println("############所有字段");
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field f:declaredFields){
			System.out.println(f);
		}

		System.out.println("############单个公有字段");
		Field f = clazz.getField("name");
		ReflectTarget o = (ReflectTarget)clazz.getConstructor().newInstance();
		f.set(o, "反射名字");
		System.out.println("反射设置值："+o);

		System.out.println("############公有字段");
		Field f2 = clazz.getDeclaredField("targetInfo");
		f2.setAccessible(true);
		f2.set(o, "24342423");
		System.out.println("反射设置值："+o);

	}
}
