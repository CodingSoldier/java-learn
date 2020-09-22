package com.example.tinyscript;

import org.junit.jupiter.api.Test;

class T01 {

    @Test
    void contextLoads() {

        int a = 1 << 2;
        //System.out.println( a );

        a = 4 >> 2;
        System.out.println( a );

        a = -1 >> 2;
        System.out.println(a);

    }

}
