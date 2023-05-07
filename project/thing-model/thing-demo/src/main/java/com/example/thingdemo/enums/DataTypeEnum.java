package com.example.thingdemo.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenpq05
 * @since 2022-02-09
 */
public enum DataTypeEnum {

    /**
     * ting_data_spec#data_type
     * 数据类型: int32（原生）、float（原生）、double（原生）、text（原生）、date（String类型UTC毫秒）、bool（0或1的int类型）、enum（int类型，枚举项定义方法与bool类型定义0和1的方法相同）、struct（结构体类型，可包含前面7种类型，下面使用"specs":[{}]描述包含的对象）、array（数组类型，支持int、double、float、text、struct）
     */
    ARRAY("array", "数组"),
    STRUCT("struct", "结构体"),
    ;

    private final String code;

    private final String des;

    DataTypeEnum(String code, String des) {
        this.code = code;
        this.des = des;
    }

    public String getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

    /**
     * 获取code、des所有键值对
     *
     * @return Map
     */
    public static Map<String, String> getCodeDes() {
        HashMap<String, String> map = new HashMap<>();
        DataTypeEnum[] values = DataTypeEnum.values();
        for (DataTypeEnum elem : values) {
            map.put(elem.getCode(), elem.getDes());
        }
        return map;
    }

    /**
     * 通过code获取des
     *
     * @param code code
     * @return des
     */
    public static String getDesByCode(String code) {
        if (code == null) {
            return "";
        }
        Map<String, String> map = getCodeDes();
        return map.get(code);
    }

}
