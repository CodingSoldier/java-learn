package pattern.factory.method;

import pattern.factory.Mouse;

public interface MouseFactory {

	/**
	 * 工厂方法模式：
	 * 		定义一个用于创建对象的接口，让子类决定实例化哪个类
	 * 	    对类的实例化延迟到其子类
	 */
	Mouse createMouse();

}
