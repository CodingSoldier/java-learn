package org.cpq;


public class JDK21 {
    public static void main(String[] args) {

        /**
         * 序列化集合
         */
        // ArrayList<Integer> al = new ArrayList<>();
        // al.add(1);
        // al.addFirst(0);
        // al.addLast(2);
        // System.out.println(al.getFirst());
        // System.out.println(al.getLast());
        // System.out.println(al.reversed());

        // LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        // map.put(2, "two");
        // map.putFirst(1, "one");
        // map.putLast(3, "three");
        // System.out.println(map);
        //
        // Map.Entry<Integer, String> entry = map.pollFirstEntry();
        // System.out.println(entry);
        // System.out.println(map);
        //
        // System.out.println(map.reversed());
        // System.out.println(map);

        /**
         * 记录模式
         */
        // 记录模式 与 instanceof 结合使用
        // Shape circle = new Shape("circle", 1);
        // if (circle instanceof Shape(String type, long unit)) {
        //     System.out.println("type = " + type + "，" + "unit = " + unit );
        // }

        // 记录模式 与 switch 结合使用
        // IShape shape = new Square(11);
        // IShape shape = new Rectangle(11, 2);
        // switch (shape) {
        //     case Circle(double radius):
        //         System.out.println("shape is circle radius = " + radius);
        //         break;
        //     case Square(double s):
        //         System.out.println("shape is square side = " + s);
        //         break;
        //     case Rectangle(double length, double width):
        //         System.out.println("shape is Rectangle length = " + length);
        //         break;
        //     default:
        //         System.out.println("default.....");
        //         break;
        // }


        /**
         * switch 的模式匹配
         */
        System.out.println(formatterPatternSwitch(1));



    }

    static String formatterPatternSwitch(Object obj) {
       return switch (obj) {
           case Integer i -> String.format("int %d", i);
           case Long l -> String.format("long %d", l);
           case Double d -> String.format("double %f", d);
           case String s -> String.format("String %s", s);
           default -> String.format("unsupported type %s", obj.getClass().getName());
       };
    }

}

// 定义记录类
record Shape(String type, long unit){}

interface IShape{}
record Circle(double radius) implements IShape{}
record Square(double side) implements IShape{}
record Rectangle(double length, double width) implements IShape{}