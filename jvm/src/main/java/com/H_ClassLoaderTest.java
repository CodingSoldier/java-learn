package com;

import java.io.InputStream;

// P228
public class H_ClassLoaderTest {
    public static void main(String[] args) throws Exception{

        ClassLoader myLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                try {
                    String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                    InputStream is = getClass().getResourceAsStream(fileName);  // 获取Class，转为二进制流
                    if (is == null){
                        return super.loadClass(name); //使用父类加载器，类相等。父类（Object）未修改，父类使用双亲委派模型加载class
                    }
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    return defineClass(name, b, 0, b.length);  //使用本加载器，与虚拟机使用默认加载器加载的类不相等
                }catch (Exception e){
                    throw new ClassNotFoundException(name);
                }
            }
        };

        Object obj = myLoader.loadClass("com.H_ClassLoaderTest").newInstance();
        System.out.println(obj.getClass());
        System.out.println(obj instanceof com.H_ClassLoaderTest);

    }
}
