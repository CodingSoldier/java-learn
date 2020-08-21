package self.licw.test.factory.simple;

import self.licw.test.factory.entity.HPMouse;
import self.licw.test.factory.entity.HUAWEIMouse;
import self.licw.test.factory.entity.Mouse;

public class MouseFactory {
    public static Mouse createMouse(int type){
        switch (type){
            case 1:return new HPMouse();
            case 2:return new HUAWEIMouse();
            default:return new HUAWEIMouse();
    }
    }

    public static void main(String[] args){
            Mouse mouse = MouseFactory.createMouse(1);
            mouse.say();
        }
}
