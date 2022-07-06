package com.vanrui.dto;

import java.io.Serializable;

public class RabbitDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final String MATERIAL_DIRECT_EXCHANGE = "materialDirectExchange";

  public static final String BUSINESS_VISITOR_EXCHANGE = "businessVisitorExchange";

  public static final String YQ_VISITOR_EXCHANGE = "yqVisitRecordExchange";

  public static final String YQ_TEMP_EXCHANGE = "yqTempExchange";

  public static final String YQ_HOUSE_CONFIG_EXCHANGE = "yqHouseConfigExchange";

  public static final String YQ_EMP_CONFIG_EXCHANGE = "yqEmpConfigExchange";

  public static final String PROJECT_ROUTING_KEY = "projectRoutingKey";

  public static final String ENCLOSURE_ROUTING_KEY = "enclosureRoutingKey";

  public static final String EMP_ROUTING_KEY = "empRoutingKey";

  public static final String BLACK_ROUTING_KEY = "blackRoutingKey";

  public static final String DEVICE_ROUTING_KEY = "deviceRoutingKey";

  public static final String FAMILY_ROUTING_KEY = "familyRoutingKey";

  public static final String WORK_ROUTING_KEY = "workRoutingKey";

  public static final String VISITOR_ROUTING_KEY = "visitorRoutingKey";

  public static final String DAILY_VISITOR_ROUTING_KEY = "dailyVisitorRoutingKey";

  public static final String CARD_ROUTING_KEY = "cardRoutingKey";

  public static final String BUSINESS_VISITOR_KEY = "businessVisitorKey";

  public static final String YQ_VISITOR_ROUTING_KEY = "yqVisitorKey";

  public static final String YQ_TEMP_ROUTING_KEY = "yqTempKey";

  public static final String YQ_HOUSE_ROUTING_KEY = "yqHouseConfigKey";

  public static final String YQ_EMP_ROUTING_KEY = "yqEmpConfigKey";

  public static final int ADD = 1;

  public static final int UPDATE = 2;

  public static final int DELETE = 3;

  public static final int STATUS = 4;

  private Long primaryKey;

  private Integer action;

  private String external;

  public String getExternal() {
    return external;
  }

  public void setExternal(String external) {
    this.external = external;
  }

  public Long getPrimaryKey() {
    return primaryKey;
  }

  public RabbitDTO() {
    super();
  }

  public RabbitDTO(Long primaryKey, Integer action) {
    this.primaryKey = primaryKey;
    this.action = action;
  }

  public void setPrimaryKey(Long primaryKey) {
    this.primaryKey = primaryKey;
  }

  public Integer getAction() {
    return action;
  }

  public void setAction(Integer action) {
    this.action = action;
  }

  @Override
  public String toString() {
    if (external != null) {
      return "RabbitDTO{" +
          "primaryKey=" + primaryKey +
          ", action=" + action +
          ", external='" + external + '\'' +
          '}';
    } else {
      return "RabbitDTO{" +
          "primaryKey=" + primaryKey +
          ", action=" + action +
          '}';
    }
  }
}
