package pattern.factory.method;

import pattern.factory.HpMouse;
import pattern.factory.Mouse;

public class HpMouseFactory implements MouseFactory {
	@Override
	public Mouse createMouse() {
		return new HpMouse();
	}
}
