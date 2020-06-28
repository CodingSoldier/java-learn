package pattern.factorysimple;

public class SimpleMouseFactory {

	/**
	 * 简单工厂模式：
	 *     定义一个工厂类，根据传入参数值的不同返回不同的实例
	 *         特点：被创建的实例具有共同的父类或接口
	 * 适用于
	 * 		需要创建的对象较少
	 * 		客户端不关心对象的创建过程
	 * 优点
	 * 		可以对创建的对象进行加工，对客户端隐藏相关细节
	 * 缺点
	 * 		因创建逻辑复杂或者创建对象过多而造成代码臃肿
	 *
	 */
	public static Mouse createMouse(int type){
		switch (type){
			case 0:
				return new DellMouse();
			case 1:
				return new HpMouse();
			default:
				return new DellMouse();
		}
	}

	public static void main(String[] args) {
		Mouse mouse = SimpleMouseFactory.createMouse(1);
		mouse.sayHi();
	}

}
