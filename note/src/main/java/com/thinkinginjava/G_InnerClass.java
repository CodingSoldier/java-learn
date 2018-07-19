package com.thinkinginjava;

import org.junit.Test;

class Outer {
    class Inner {
        { System.out.println("Inner created"); }
    }
    public Inner getInner() { return new Inner(); }
}

public class G_InnerClass {
    //P191
    /*即使内部类为public，内部类前面也要加上外部类名Outer，不能直接使用Inner。
    * 通过编译结果：只产生了Outer.class，并没有产生Inner.class。
    * 所以绝不可能直接导入Inner类，只能导入Outer类，然后通过Outer.Inner来获取Inner类*/
    @Test
    public void t(){
        Outer o = new Outer();
        Outer.Inner i = o.getInner();
    }
}



class Outer1
{
    private int x = 3;
    class Inner
    {
        int x = 6;
        void function(){
            //int x = 9;

            //内部类中单独写x，查找循序：局部变量 -> 本内部类成员变量 -> 外部类成员变量，所以值为9、6、3都是有可能的
            System.out.println("inner:"+x);

            //System.out.println("inner:"+this.x);  //this.x已经限定为本类Inner的成员变量，

            System.out.println("inner:"+Outer1.this.x); //已经限定为外部类成员变量
            // 输出是9 6 3。Outer不是静态类为什么可以通过Outer来访问?
        }
    }
    void method()
    {
        // 外部类要访问内部类，必须建立内部类对象
        Inner in = new Inner();
        in.function();
    }
    public static void main(String[] args)
    {
        Outer1 out = new Outer1();
        out.method();

        // 直接访问内部类中的成员,几乎不用
        // 外部类的内部类的引用 = 外部类实体的内部类实体
        // Outer.Inner in = new Outer().new Inner();
        // in.function();
    }
}





// P196
interface SimpleInterface{
    void f();
}
class Outer5 {
    private class Inner implements SimpleInterface {
        public void f() {
            System.out.println("Outer5.Inner.inFn");
        }
    }
    public SimpleInterface get() { return new Inner(); }
    public Inner get2() { return new Inner(); }
}
class E11_HiddenInnerClass {
    public static void main(String args[]) {
        Outer5 out = new Outer5();
        SimpleInterface si = out.get();
        si = out.get2();
        si.f();
 //私有内部类不能在其他类中被访问
 //       Inner i1 = out.get2();
 //       Inner i2 = (Inner)si;
    }
}














