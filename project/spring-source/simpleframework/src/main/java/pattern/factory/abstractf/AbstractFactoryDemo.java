package pattern.factory.abstractf;

import pattern.factory.Keyboard;
import pattern.factory.Mouse;

public class AbstractFactoryDemo {
	public static void main(String[] args) {
		ComputerFactory hpComputerFactory = new HpComputerFactory();
		Mouse mouse = hpComputerFactory.createMouse();
		Keyboard keyboard = hpComputerFactory.createKeyboard();
		mouse.sayHi();
		keyboard.sayHello();
	}
}
