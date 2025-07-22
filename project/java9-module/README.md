module-a/src/main/java/module-info.java

    module中的包默认是私有的
    使用exports声明模块的包对其他模块开放，exports只能导出包，不支持导出类。如果对外导出的类少，推荐放到一个包下

module-b 导入 maven依赖

    <dependency>
      <groupId>org.cpq</groupId>
      <artifactId>module-a</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
由于module-a只开放了org.cpq.a.a1，所以module-b只能使用org.cpq.a.a1包下的类
不能使用org.cpq.a.a2包下的类

module默认是没有传递性的，module-b必须声明requires transitive org.cpq.a;才能让module-c使用module-a的包

module-a使用opens org.cpq.a.reflex;声明reflex包内的类可以反射访问

module-a使用exports + provides提供服务。module-b使用uses声明需要使用的服务，module-b使用ServiceLoader<Print> loads = ServiceLoader.load(Print.class);获取实现类
