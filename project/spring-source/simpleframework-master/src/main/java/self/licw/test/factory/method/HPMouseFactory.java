package self.licw.test.factory.method;

import self.licw.test.factory.entity.HPMouse;
import self.licw.test.factory.entity.Mouse;

public class HPMouseFactory implements MouseFactory {
    @Override
    public Mouse createMouse() {
        return new HPMouse();
    }
}
