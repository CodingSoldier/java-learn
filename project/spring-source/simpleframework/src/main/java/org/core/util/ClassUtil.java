package org.core.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
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
        }
        return classSet;
    }

    public static void main(String[] args) {
        extractPackageClass("com.a");
    }

}
