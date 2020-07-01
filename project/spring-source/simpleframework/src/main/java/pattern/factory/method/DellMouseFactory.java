package pattern.factory.method;

import pattern.factory.DellMouse;
import pattern.factory.Mouse;

public class DellMouseFactory implements MouseFactory {
	@Override
	public Mouse createMouse() {
		return new DellMouse();
	}
}
