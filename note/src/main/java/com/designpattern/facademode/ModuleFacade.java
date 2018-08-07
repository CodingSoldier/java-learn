package com.designpattern.facademode;

public class ModuleFacade {
    ModuleA a = new ModuleA();
    ModuleB b = new ModuleB();
    ModuleC c = new ModuleC();

    public void a1(){
        a.a1();
    }

    public void b1(){
        b.b1();
    }

    public void c1(){
        c.c1();
    }

}
