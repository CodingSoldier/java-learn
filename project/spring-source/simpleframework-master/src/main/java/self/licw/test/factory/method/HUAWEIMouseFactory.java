package self.licw.test.factory.method;

import self.licw.test.factory.entity.HUAWEIMouse;
import self.licw.test.factory.entity.Mouse;

public class HUAWEIMouseFactory implements MouseFactory{
    @Override
    public Mouse createMouse() {
        return new HUAWEIMouse();
    }
}
