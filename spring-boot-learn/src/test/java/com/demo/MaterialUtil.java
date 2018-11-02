package com.demo;

import org.apache.commons.lang3.StringUtils;

public class MaterialUtil {

    public static final String GANG_YIN = "(钢印)";  //钢印

    public static String getNameGangYin(String name){
        if (StringUtils.isNotBlank(name) && name.length() > 3 && !"钢印".equals(name.substring(name.length() - 3, name.length()-1)) ){
            name += "(钢印)";
        }
        return  name;
    }
}
