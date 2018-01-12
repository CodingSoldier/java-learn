package com.annotation.base;

import java.lang.reflect.Field;

public class FruitInfoUtil {
    public static void getFruitInfo(Class<?> clazz){
        String fruitName = "水果名称：";
        String fruitColor = "水果颜色：";
        String fruitProvicer = "水果供应商：";

        Field[] fieldList = clazz.getDeclaredFields();
        for (Field field: fieldList){
            if(field.isAnnotationPresent(FruitName.class)){
                FruitName fn = (FruitName) field.getAnnotation(FruitName.class);
                System.out.println(fruitName+fn.value());
            }
            if (field.isAnnotationPresent(FruitColor.class)){
                FruitColor fc = (FruitColor)field.getAnnotation(FruitColor.class);
                System.out.println(fruitColor+fc.fruitColor().toString());
            }
            if(field.isAnnotationPresent(FruitProvider.class)){
                FruitProvider fp = (FruitProvider)field.getAnnotation(FruitProvider.class);
                System.out.println(fruitProvicer+fp.id());
                System.out.println(fruitProvicer+fp.name());
                System.out.println(fruitProvicer+fp.address());
            }
        }
    }
}












