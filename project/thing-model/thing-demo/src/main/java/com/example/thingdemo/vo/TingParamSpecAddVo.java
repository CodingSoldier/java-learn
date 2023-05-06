package com.example.thingdemo.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author chenpq
 * @since 2023-05-05 17:00:52
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "物模型3维度数据规格-新增请求体")
public class TingParamSpecAddVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "物模型id")
  private Long tingId;

  @ApiModelProperty(value = "物模型维度id")
  private Long tingDimensionId;

  @ApiModelProperty(value = "event、action输出输出参数唯一标识符")
  private String paramIdentifier;

  @ApiModelProperty(value = "event、action输出输出参数唯一标识符名称")
  private String paramIdentifierName;

  //@ApiModelProperty(value = "父id。struct结构体的键的父id是struct。[{}]数组元素struct，struct的键的父id是数组。[]数组元素不是struct，数组元素没有父id。")
  //private Long parentId;

  @ApiModelProperty(value = "1 inputData，2 outputData")
  private Integer inOutData;

  @ApiModelProperty(value = "数据类型: int32（原生）、float（原生）、double（原生）、text（原生）、date（String类型UTC毫秒）、bool（0或1的int类型）、enum（int类型，枚举项定义方法与bool类型定义0和1的方法相同）、struct（结构体类型，可包含前面7种类型，下面使用\"specs\":[{}]描述包含的对象）、array（数组类型，支持int、double、float、text、struct）")
  @NotEmpty(message = "数据类型不能为空")
  private String dataType;

  @ApiModelProperty(value = "数据规格	{	    \"min\": \"参数最小值（int、float、double类型特有）。\",	    \"max\": \"参数最大值（int、float、double类型特有）。\",	    \"unit\": \"属性单位（int、float、double类型特有，非必填）。\",	    \"unitName\": \"单位名称（int、float、double类型特有，非必填）。\",	    \"size\": \"数组元素的个数，最大512（array类型特有）。\",	    \"step\": \"步长（text、enum类型无此参数）。\",	    \"length\": \"数据长度，最大10240（text类型特有）。\",	    \"0\": \"0的值（bool类型特有）。\",	    \"1\": \"1的值（bool类型特有）。\",	    \"arrayItemType\": \"数组元素的类型（array类型特有）。\"	}")
  private String specs;

  @ApiModelProperty(value = "排序，小的在前面")
  private Integer sort;

  @ApiModelProperty(value = "json对象")
  private TingParamSpecAddVo json;

}
