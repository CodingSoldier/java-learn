package com.example.java917;


public class Jdk9_17 {

  public static void main(String[] args) throws Exception {

    // HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    // HttpRequest req = HttpRequest.newBuilder(URI.create("https://www.baidu.com")).build();
    // HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
    // System.out.println(resp.body());

    // var list = new ArrayList<Integer>();
    // list.add(11122);
    // list.add(111);
    // Integer i = list.get(0);
    // System.out.println(i);

    // var simple = switch (lang) {
    //   case "java", "scale", "groory" -> "jvm";
    //   case "c", "cqp" -> "c";
    //   case "go" -> "g";
    //   default -> {
    //     if (lang == null) {
    //       yield "unknown";
    //     } else yield "non";
    //   }
    // }

    // var block = """
    //         lang: java
    //         version: 13
    //         dbname: mysql
    //         ip: 192.168.140.2
    //         """;
    // long count = block.lines().count();
    // System.out.println(count);
    //
    // var b = """
    //         lang: java \
    //         version: 12 \
    //         ip: 22344234
    //         """;
    // System.out.println(b.lines().count());

    // Object o = new Random().nextInt() % 2 == 0 ? "java16" : 1000.0d;
    // if (o instanceof String s) {
    //   System.out.println(s.length());
    // } else {
    //   System.out.println(o);
    // }



  }
}

@FunctionalInterface
interface Mapper<A,B> {
  B map(A a);
}

class MathUtilImpl implements MathUtil {
  public static void main(String[] args) throws Exception {
    Mapper<String, Integer> aa = (var a) -> a.length();
    System.out.println(aa);
  }

}

interface MathUtil {
  // implements 该接口的类将自动获取该方法。 - jdk 8.
  default void h(){
    g();
  }

  // 在接口定义的属性直接被视为 static。 -jdk 8.
  double Pi = 3.1415;

  // 可以在接口直接定义静态方法。 - jdk 8.
  static void f(){}

  // 可以在接口内直接定义私有方法。 -jdk 9.
  private void g(){}
}

@FunctionalInterface
interface Mapper11<A, B> {
  B map(A a);
}
class JdkMapper11 {
  public static void main(String[] args) throws Exception {
    Mapper11<String, Integer> string2int = (var a) -> a.length();
    Integer i = string2int.map("sdf sf");
    System.out.println(i);
  }
}

record Student(String name, Integer age) {

  public static void main(String[] args) {
    var student = new Student("名字", 11);
    System.out.println(student.age);
    System.out.println(student.name);
  }

}


sealed class People {}

non-sealed class Teacher extends People{}

sealed class Driver extends  People{}

non-sealed class TruckDriver extends Driver{}

sealed class People1 permits Teacher1, Driver1{}
non-sealed class Teacher1 extends People1 {}
non-sealed class Driver1 extends People1{}
























