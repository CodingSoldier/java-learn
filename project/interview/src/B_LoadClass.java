/**
 * @author chenpiqian
 * @date: 2019-12-30
 */
public class B_LoadClass {

    public static void main(String[] args) throws Exception{
        ClassLoader c = AA_Wali.class.getClassLoader();
        /**
         * loadClass()方法只是加载类文件，不会初始化类
         * 源码中不会执行resolveClass()，即只执行了 java--笔记.txt -> JVM类装载过程 中的第1步
         *
         * 可以加快类的加载数据，当用到类的时候再初始化类。比如spring的lazyLoading
         */
        c.loadClass("AA_Wali");

        System.out.println("*************分隔符***************");

        /**
         * forName()方法会初始化类，static静态代码块执行了
         */
        Class c1 = Class.forName("AA_Wali");
    }

}
