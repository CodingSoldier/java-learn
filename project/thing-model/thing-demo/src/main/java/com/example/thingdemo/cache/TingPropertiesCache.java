package com.example.thingdemo.cache;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author chenpq
 * @since 2023-05-04 14:09:09
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TingPropertiesCache implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "物模型维度名称")
    private String name;

    @ApiModelProperty(value = "唯一标识符")
    private String identifier;

    @ApiModelProperty(value = "属性读写类型：只读（r）或读写（rw）")
    private String propertiesAccessMode;

    @ApiModelProperty(value = "是否必选：是1，否0")
    private Integer required;

    @ApiModelProperty(value = "数据类型: int32（原生）、float（原生）、double（原生）、text（原生）、date（String类型UTC毫秒）、bool（0或1的int类型）、enum（int类型，枚举项定义方法与bool类型定义0和1的方法相同）、struct（结构体类型，可包含前面7种类型，下面使用\"specs\":[{}]描述包含的对象）、array（数组类型，支持int、double、float、text、struct）")
    private String dataType;

    @ApiModelProperty(value = "数据规格	{	    \"min\": \"参数最小值（int、float、double类型特有）。\",	    \"max\": \"参数最大值（int、float、double类型特有）。\",	    \"unit\": \"属性单位（int、float、double类型特有，非必填）。\",	    \"unitName\": \"单位名称（int、float、double类型特有，非必填）。\",	    \"size\": \"数组元素的个数，最大512（array类型特有）。\",	    \"step\": \"步长（text、enum类型无此参数）。\",	    \"length\": \"数据长度，最大10240（text类型特有）。\",	    \"0\": \"0的值（bool类型特有）。\",	    \"1\": \"1的值（bool类型特有）。\",	    \"arrayItemType\": \"数组元素的类型（array类型特有）。\"	}")
    private String specs;

}
