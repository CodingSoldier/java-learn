package org.cpq.b;

import org.cpq.a.service.Print;

import java.util.ServiceLoader;

/**
 * Hello world!
 *
 */
public class MainB
{
    public static void main( String[] args ) throws Exception
    {
        // 由于 module org.cpq.a 只开放了 org.cpq.a.a1 ，所以只能使用HelloA1，
        // 无法使用org.cpq.a.a2.HelloA2
        // new HelloA1().hello();

        // Class<?> c = Class.forName("org.cpq.a.reflex.Reflex01");
        // Object instance = c.getDeclaredConstructor().newInstance();
        // Method method = c.getDeclaredMethods()[0];
        // method.setAccessible(true);
        // method.invoke(instance, null);

        ServiceLoader<Print> loads = ServiceLoader.load(Print.class);
        for (Print load : loads) {
            load.printMsg();
        }

    }
}
