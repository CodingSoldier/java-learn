package com.example.thingdemo.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenpq05
 * @since 2022-02-09
 */
public enum InoutDataEnum {

    /**
     * thing_param_spec#in_out_data
     * 1 inputData，2 outputData
     */
    INPUT_DATA(1, "inputData"),
    OUTPUT_DATA(2, "outputData"),
    ;

    private final Integer code;

    private final String des;

    InoutDataEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public Integer getCode() {
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
    public static Map<Integer, String> getCodeDes() {
        HashMap<Integer, String> map = new HashMap<>();
        InoutDataEnum[] values = InoutDataEnum.values();
        for (InoutDataEnum elem : values) {
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
    public static String getDesByCode(Integer code) {
        if (code == null) {
            return "";
        }
        Map<Integer, String> map = getCodeDes();
        return map.get(code);
    }

}
