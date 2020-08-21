package self.licw.test.reflect;

public class ReflectTarget {
    public static void main(String[] args) {
        //第一种方式
        ReflectTarget reflectTarget = new ReflectTarget();
        Class class1 = reflectTarget.getClass();
        System.out.println(class1.getName());
        //第二种方式
        Class class2 = ReflectTarget.class;
        System.out.println(class2.getName());
        System.out.println(class1 == class2);
        //第三种方式：
        try {
            Class class3 = Class.forName("self.licw.test.reflect.ReflectTarget");
            System.out.println(class3.getName());
            System.out.println(class3 == class2);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
