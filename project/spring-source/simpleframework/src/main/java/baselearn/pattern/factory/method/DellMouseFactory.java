package baselearn.pattern.factory.method;

import baselearn.pattern.factory.DellMouse;
import baselearn.pattern.factory.Mouse;

public class DellMouseFactory implements MouseFactory {
	@Override
	public Mouse createMouse() {
		return new DellMouse();
	}
}
