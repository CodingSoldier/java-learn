package org.core.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @author chenpiqian
 * @date: 2020-07-07
 */
@Slf4j
public class ClassUtil {

    public static final String FILE_PROTOCOL = "file";

    /**
     * 获取ClassLoader
     *
     * @return
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> loadClass(String className){
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("load class error:", e);
            throw new RuntimeException(e);
        }
    }

    public static Set<Class<?>> extractPackageClass(String packageName){
        // 1、获取到类的加载器
        ClassLoader classLoader = getClassLoader();

        // 2、通过类加载器获取到包内资源
        URL url = classLoader.getResource(packageName.replace(".", "/"));
        if (url == null){
            log.error("packageName是空");
            return null;
        }

        // 3、根据协议获取文件
        Set<Class<?>> classSet = null;
        if (url.getProtocol().equalsIgnoreCase(FILE_PROTOCOL)){
            classSet = new HashSet<>();
            File file = new File(url.getPath());
            extractClassFile(classSet, file, packageName);
        }
        return classSet;
    }

    public static void extractClassFile(Set<Class<?>> emptyClassSet, File fileSource, String packageName){
        if (!fileSource.isDirectory()){
            return;
        }
        File[] files = fileSource.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    return true;
                } else {
                    String absoluteFilePath = pathname.getAbsolutePath();
                    if (absoluteFilePath.endsWith(".class")) {
                        addToClassSet(absoluteFilePath);
                    }
                }
                return false;
            }

            private void addToClassSet(String absoluteFilePath) {
                absoluteFilePath = absoluteFilePath.replace(File.separator, ".");
                String className = absoluteFilePath.substring(absoluteFilePath.indexOf(packageName));
                className = className.substring(0, className.lastIndexOf("."));
                Class<?> targetClass = loadClass(className);
                emptyClassSet.add(targetClass);
            }
        });
        if (files != null){
            for (File f:files){
                extractClassFile(emptyClassSet, f, packageName);
            }
        }
    }

    public static void main(String[] args) {
        Set<Class<?>> classSet = extractPackageClass("pattern.factory");
        System.out.println(classSet.toString());
    }

}
