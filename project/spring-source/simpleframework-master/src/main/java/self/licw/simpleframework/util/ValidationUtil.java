package self.licw.simpleframework.util;

import java.util.Collection;
import java.util.Map;

public class ValidationUtil {
    /**
     * Collection 集合是否为null或者size为0
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection){
        return collection == null || collection.isEmpty();
    }

    /**
     * String 字符串是否为null或者""
     * @param obj
     * @return
     */
    public static boolean isEmpty(String obj){
        return obj == null || "".equals(obj);
    }

    /**
     * Array 数组(任何对象类型的数组）是否为null或者size为0
     * @param objects
     * @return
     */
    public static boolean isEmpty(Object[] objects){
        return objects == null || objects.length == 0;
    }

    /**
     * map 字典是否为null或者size为0
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?,?> map){
        return map == null || map.isEmpty();
    }
}
