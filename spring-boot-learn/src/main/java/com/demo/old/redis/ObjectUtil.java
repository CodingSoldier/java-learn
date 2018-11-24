package com.demo.old.redis;

import org.assertj.core.util.DateUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by cyjxc1 on 2017/6/13.
 */
public class ObjectUtil {
    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     * */
    public static List<Map<String,String>> getFiledsInfo(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        List<Map<String,String>> list = new ArrayList();
        Map infoMap=null;
        for(int i=0;i<fields.length;i++){
            infoMap = new HashMap();
            infoMap.put("type", fields[i].getType().toString());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value", getFieldValueByName(fields[i].getName(), o));
            list.add(infoMap);
        }
        return list;
    }

    /**
     * 根据属性名获取属性值
     * */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    // 该方法的参数列表是一个类的 类名、成员变量、给变量的赋值
    public static void setProperty(Object obj, String PropertyName, Object value)
            throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException ,ParseException{

        // 获取obj类的字节文件对象
        Class c = obj.getClass();

        // 获取该类的成员变量
        Field f = c.getDeclaredField(PropertyName);

        // 取消语言访问检查
        f.setAccessible(true);
        //byte short int long char
        if(f.getType().getTypeName().endsWith("Date") && null != value){
            Date d;
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy",Locale.US);
                d = formatter.parse((String)value);
            } catch (Exception e){
                d = DateUtil.parse((String)value);
            }
            value = d;
        }else if((f.getType().getTypeName().endsWith("int") || f.getType().getTypeName().endsWith("Integer")) && null != value){
            value = Integer.parseInt((String)value);
        }else if((f.getType().getTypeName().endsWith("boolean") || f.getType().getTypeName().endsWith("Boolean")) && null != value){
            value = Boolean.parseBoolean((String)value);
        }else if((f.getType().getTypeName().endsWith("float") || f.getType().getTypeName().endsWith("Float")) && null != value){
            value = Float.parseFloat((String)value);
        }else if((f.getType().getTypeName().endsWith("double") || f.getType().getTypeName().endsWith("Double")) && null != value){
            value = Double.parseDouble((String)value);
        }else if((f.getType().getTypeName().endsWith("byte") || f.getType().getTypeName().endsWith("Byte")) && null != value){
            value = Byte.parseByte((String)value);
        }else if((f.getType().getTypeName().endsWith("long") || f.getType().getTypeName().endsWith("Long")) && null != value){
            value = Long.parseLong((String)value);
        }
        // 给变量赋值
        if(null != value){
            f.set(obj, value);
        }

    }

    public static void copySameField(Object src, Object dest) throws Exception{

        copySameField(src,dest,null);
    }

    /**
     * 复制相同的对象属性
     * @param src
     * @param dest
     * @param ignoreProperties
     * @throws Exception
     */
    public static void copySameField(Object src, Object dest,String[] ignoreProperties) throws Exception{

        Map<String, Object> srcMap = new HashMap<String, Object>();
        List<String> propertiesList = new ArrayList<>();
        if(ignoreProperties != null) {
            propertiesList = Arrays.asList(ignoreProperties);
        }
        Field[] srcFields = src.getClass().getDeclaredFields();
        for (Field fd : srcFields) {
            try {
                fd.setAccessible(true);
                srcMap.put(fd.getName(), fd.get(src)); //获取属性值
            } catch (Exception e) {
                throw new Exception("获取对像属性值失败 "+e.getMessage());
            }
        }

        Field[] destFields = dest.getClass().getDeclaredFields();
        for (Field fd : destFields) {
            if (srcMap.get(fd.getName()) == null) {
                continue;
            }

            if(propertiesList.contains(fd.getName())){
                continue;
            }
            try {
                fd.setAccessible(true);
                fd.set(dest, srcMap.get(fd.getName())); //给属性赋值
            } catch (Exception e) {
                throw new Exception("给对像属性赋值失败 "+e.getMessage());
            }
        }
    }

}
