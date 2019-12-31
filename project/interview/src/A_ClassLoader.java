import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author chenpiqian
 * @date: 2019-12-30
 */
public class A_ClassLoader {

    /**
     * 自定义ClassLoader类加载器，加载class文件
     */
    static class MyClassLoader extends ClassLoader{

        private String path;
        private String classLoaderName;

        public MyClassLoader(String path, String classLoaderName) {
            this.path = path;
            this.classLoaderName = classLoaderName;
        }

        /**
         * 查找指定二进制名称的类，遵循加载类的委托模型
         * @param name 二进制类的名称
         */
        @Override
        protected Class<?> findClass(String name) {
            byte[] b = loadClassByte(name);

            /**
             * defineClass将字节数组转化为类的实例
             */
            return defineClass(name, b, 0, b.length);
        }

        private byte[] loadClassByte(String name){
            String classFile = path + name + ".class";
            InputStream in = null;
            ByteArrayOutputStream out = null;
            try {
                in = new FileInputStream(new File(classFile));
                out = new ByteArrayOutputStream();
                int i = 0;
                while ((i = in.read()) != -1){
                    out.write(i);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                try {
                    out.close();
                    in.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return out.toByteArray();
        }
    }

    static class TestClassLoader{
        public static void main(String[] args) throws Exception{

            //// 创建自定义类加载器
            //MyClassLoader m = new MyClassLoader("D:\\third-code\\java-learn\\project\\interview\\src\\", "myClassLoader");
            ///**
            // * 先执行 javac AA_Wali.java 编译为class文件
            // * 加载AA_Wali.class
            // */
            //Class c = m.loadClass("AA_Wali");
            //// 创建类实例
            //c.newInstance();


            /**
             * 双亲委派模型，看图 A_双亲委派模型.jpg
             * 为什么使用双亲委派模型加载类： 避免同样的字节码多次加载，浪费内存
             */
            MyClassLoader m = new MyClassLoader("D:\\third-code\\java-learn\\project\\interview\\src\\", "myClassLoader");
            System.out.println(m.getParent());
            System.out.println(m.getParent().getParent());
            System.out.println(m.getParent().getParent().getParent());
            // Bootstrap ClassLoader是本地方法实现的，不能通过java代码获取


        }

    }

}
