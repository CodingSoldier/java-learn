package com.example.reactor0.a05debug;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author chenpq05
 * @since 2022/7/8 11:23
 */
public class FooNameHelper {

    public static Foo concatAndSubstringFooName(Foo foo) {
        Foo concat = concatFooName(foo);
        return substringFooName(concat);
    }

    public static Foo concatFooName(Foo foo) {

        int random = ThreadLocalRandom.current()
                .nextInt(0, 80);

        String processedName = (random != 0)
                ? foo.getFormattedName()
                : foo.getFormattedName() + "-bael";

        foo.setFormattedName(processedName);
        return foo;
    }

    public static Foo substringFooName(Foo foo) {
        int random = ThreadLocalRandom.current()
                .nextInt(0, 100);

        String processedName = (random == 0)
                ? foo.getFormattedName().substring(10, 15)
                : foo.getFormattedName().substring(0, 5);

        foo.setFormattedName(processedName);

        return foo;
    }

}