package org.cpq.c;

import org.cpq.a.a1.HelloA1;
// import org.cpq.a.a2.HelloA2;

/**
 * Hello world!
 *
 */
public class MainC
{
    public static void main( String[] args )
    {
        // module-b使用声明了传递依赖requires transitive org.cpq.a; 在module-c也能使用module-a导出的包
        new HelloA1().hello();

        // new HelloA2().hello();
    }
}
