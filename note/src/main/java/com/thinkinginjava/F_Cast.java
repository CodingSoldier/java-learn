package com.thinkinginjava;

import org.junit.Test;

class Useful{
    public void f(){};
    public void g(){};
}
class MoreUseful extends Useful{
    public void u(){};
}


abstract class NoAbstractMethods {
    void f() { System.out.println("f()"); }
}

public class F_Cast {

    //向下转型  p167
    @Test
    public void t(){
        Useful[] x = {new Useful(), new MoreUseful()};
        //((MoreUseful)x[0]).u();
        ((MoreUseful)x[1]).u();
    }

    //不含抽象方法的抽象类依然不能创建实体  p171
    @Test
    public void t2(){
        //new NoAbstractMethods();

    }

}
