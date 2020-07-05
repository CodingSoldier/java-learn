package pattern.factory.abstractf;

import pattern.factory.*;

public class HpComputerFactory implements ComputerFactory {
	@Override
	public Mouse createMouse() {
		return new HpMouse();
	}

	@Override
	public Keyboard createKeyboard() {
		return new HpKeyboard();
	}
}
