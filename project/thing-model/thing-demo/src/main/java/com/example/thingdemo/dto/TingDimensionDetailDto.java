package com.example.thingdemo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
* @author chenpq
* @since 2023-05-04 14:09:09
*/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "物模型3维度类型，1（properties），2（event），3（action）-详情返回值")
public class TingDimensionDetailDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "主键")
  private Long id;

  @ApiModelProperty(value = "物模型id")
  private Long tingId;

  @ApiModelProperty(value = "物模型维度名称")
  private String name;

  @ApiModelProperty(value = "物模型维度描述")
  private String description;

  @ApiModelProperty(value = "维度类型，1（properties），2（event），3（action）")
  private Integer dimension;

  @ApiModelProperty(value = "唯一标识符")
  private String identifier;

  @ApiModelProperty(value = "属性读写类型：只读（r）或读写（rw）")
  private String propertiesAccessMode;

  @ApiModelProperty(value = "action调用方式，1（同步调用）或2（异步调用）")
  private Integer actionCallType;

  @ApiModelProperty(value = "事件类型 1信息，2告警，3故障")
  private Integer eventType;

  @ApiModelProperty(value = "是否必选：是1，否0")
  private Integer required;

  @ApiModelProperty(value = "创建时间")
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  private LocalDateTime createTime;

  @ApiModelProperty(value = "更新时间")
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  private LocalDateTime updateTime;

  @ApiModelProperty(value = "数据规则")
  private List<TingParamSpecDetailDto> paramSpecList;

}
