package pattern.factory.method;

import pattern.factory.Mouse;

public class FactoryMethodDemo {
	public static void main(String[] args) {
		MouseFactory hpMouseFactory = new HpMouseFactory();
		Mouse mouse = hpMouseFactory.createMouse();
		mouse.sayHi();
	}
}
