package com.example.thingdemo.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenpq05
 * @since 2022-02-09
 */
public enum DimensionEnum {

  /**
   * ting_dimension#dimension
   * 维度类型，1（properties），2（event），3（action）
   */
  PROPERTIES(1, "属性"),
  EVENT(2, "事件"),
  ACTION(3, "动作"),
  ;

  private final Integer code;

  private final String des;

  DimensionEnum(Integer code, String des) {
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
    DimensionEnum[] values = DimensionEnum.values();
    for (DimensionEnum elem : values) {
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
