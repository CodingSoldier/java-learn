module org.cpq.b {
    // requires org.cpq.a;

    // transitive 传递依赖，导入了module-b的模块，能使用module-a的模块
    requires transitive org.cpq.a;

    uses org.cpq.a.service.Print;
}