module org.cpq.a {

    // exports只能导出一个包，不支持使用,拼接导出多个包
    exports org.cpq.a.a1;

    // to表示向org.cpq.c模块开放org.cpq.a.a2包
    // to 多个模块可以使用,分割
    // exports org.cpq.a.a2 to org.cpq.c;

    // opens导出的包，可以反射访问。
    // 但不能实例化，如果需要实例化，需要配合exports使用
    // open module org.cpq.a {} 表示模块org.cpq.a的所有包都可以被反射访问。
    opens org.cpq.a.reflex;

    // 为接口 Print 提供了两个具体实现类PrintImpl01、PrintImpl02
    // 还需要exports service包
    exports org.cpq.a.service;
    provides org.cpq.a.service.Print
        with org.cpq.a.impl.PrintImpl01,
             org.cpq.a.impl.PrintImpl02;
}