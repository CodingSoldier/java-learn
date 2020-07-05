package pattern.factory.method;

import pattern.factory.Mouse;

public interface MouseFactory {

	/**
	 * 工厂方法模式：
	 * 		定义一个用于创建对象的接口，让子类决定实例化哪个类
	 * 	    对类的实例化延迟到其子类
	 * 	优点：
	 * 		遵循开闭原则
	 * 		对客户端隐藏对象的创建细节
	 * 		遵循单一责任原则
	 * 	缺点：
	 * 		添加子类的时候“拖家带口”
	 * 		只支持同一类产品的创建
	 */
	Mouse createMouse();

}
