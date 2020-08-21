package self.licw.simpleframework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ClassUtil {
    /**
     * 获取包下类集合
     *
     * @param packageName 包名
     * @return 类集合
     */

    public static final String FILE_PROTOCOL = "file";

    public static Set<Class<?>> extractPackageClass(String packageName) {
        //1.获取类的加载器
        ClassLoader classLoader = getClassLoader();
        //2.通过类加载器取得加载的资源
        //classloader的getresource只能识别/ 所以要把用户传进来的包的. 替换成/
        URL url = classLoader.getResource(packageName.replace(".", "/"));
        if (url == null) {
            return null;
        }
        //3.依据不同的资源类型，采取不同的方式获取资源的集合。根据url的协议去判断资源的类型，如http file等
        Set<Class<?>> classSet = null;
        //过滤出文件类型 file协议 的资源
        if (url.getProtocol().equalsIgnoreCase(FILE_PROTOCOL)) {
            classSet = new HashSet<Class<?>>();
            File packageDirectory = new File(url.getPath());
            extractClassFile(classSet, packageDirectory, packageName);
        }
        return classSet;
    }

    /**
     * 递归获取目标package里边的所有class文件（包括子package里的class文件）
     *
     * @param classSet    装载目标类的集合
     * @param Filesource  文件或者目录
     * @param packageName 包名
     */
    private static void extractClassFile(final Set<Class<?>> classSet, File Filesource, final String packageName) {
        if (!Filesource.isDirectory()) {
            return;
        }
        //列出文件夹下的文件或文件夹，但不包括子文件夹
        File[] files = Filesource.listFiles(new FileFilter() {
                                                //可以传入FileFilter为其过滤
                                                @Override
                                                public boolean accept(File file) {
                                                    //对于文件夹 筛选出来
                                                    if (file.isDirectory()) {
                                                        //true 需要筛选出来
                                                        return true;
                                                    } else {
                                                        //获取文件的绝对值路径
                                                        String absoluteFilePath = file.getAbsolutePath();
                                                        //对于.class结尾的文件要直接加载
                                                        if (absoluteFilePath.endsWith(".class")) {
                                                            //若是class文件，直接加载
                                                            try {
                                                                addToClassSet(absoluteFilePath);
                                                            } catch (ClassNotFoundException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                    //对于非.class文件和非文件夹则直接抛弃
                                                    return false;
                                                }

                                                // 根据class文件的绝对值路径，获取并生成class对象并放入classset中
                                                private void addToClassSet(String absoluteFilePath) throws ClassNotFoundException {
                                                    //1.从class文件的绝对值路径中提取出包含了package的类名
                                                    //eg:将D:\javaproject\test\self\licw\entity\HeadLine.class替换成self.licw.entity.HeadLine
                                                    //因为java是跨平台的，所以用File.separator属性即可获得该平台的文件分割符，超级方便
                                                    absoluteFilePath = absoluteFilePath.replace(File.separator, ".");
                                                    //String.indexOf 返回字符串第一次出现处的索引
                                                    String className = absoluteFilePath.substring(absoluteFilePath.indexOf(packageName));
                                                    className = className.substring(0, className.lastIndexOf("."));
                                                    //2.通过反射机制获得对应的class对象
                                                    Class clazz = loadClass(className);
                                                    classSet.add(clazz);
                                                }
                                            }
        );
        //对前边过滤出来的文件夹继续进行递归
        if (files != null) {
            for (File f : files) {
                //递归调用
                extractClassFile(classSet, f, packageName);
            }
        }

    }

    /**
     * 实例化class 通过无参的构造函数实例化出class对象
     * @param clazz class的对象
     * @param <T> class的类型
     * @param accessible 是否支持创建出私有class对象的实例
     * @return class对象类的实例
     */
    public static <T> T newInstance(Class<?> clazz,boolean accessible){
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(accessible);
            return (T) constructor.newInstance();
        } catch (Exception e) {
            log.error("newInstance error",e);
            throw new RuntimeException(e);
    }
    }


    /**
     * 获得classLoader
     *
     * @return 当前线程的classLoader
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取class对象
     *
     * @param className class全名=package + 类名
     * @return Class
     */
    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        Class clazz = Class.forName(className);
        return clazz;
    }

    /**
     * 设置target类里边指定属性的属性值
     * @param field 成员变量
     * @param target 类实例
     * @param value 成员变量的值
     * @param accessible 是否允许设置私有属性
     */
    public static void setField(Field field,Object target,Object value,boolean accessible){
        field.setAccessible(accessible);
        try {
            field.set(target,value);
        } catch (IllegalAccessException e) {
            log.error(target.getClass() + "set field error" + field.getName());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        extractPackageClass("self.licw.o2o.service");
    }
}
