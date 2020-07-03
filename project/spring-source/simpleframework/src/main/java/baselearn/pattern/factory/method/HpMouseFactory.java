package baselearn.pattern.factory.method;

import baselearn.pattern.factory.HpMouse;
import baselearn.pattern.factory.Mouse;

public class HpMouseFactory implements MouseFactory {
	@Override
	public Mouse createMouse() {
		return new HpMouse();
	}
}
