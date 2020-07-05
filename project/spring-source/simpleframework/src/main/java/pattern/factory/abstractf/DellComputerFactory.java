package pattern.factory.abstractf;

import pattern.factory.DellKeyboard;
import pattern.factory.DellMouse;
import pattern.factory.Keyboard;
import pattern.factory.Mouse;

public class DellComputerFactory implements ComputerFactory {
	@Override
	public Mouse createMouse() {
		return new DellMouse();
	}

	@Override
	public Keyboard createKeyboard() {
		return new DellKeyboard();
	}
}
