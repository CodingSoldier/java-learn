package com.example.thingdemo.cache;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
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
public class ThingActionCache implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "物模型维度名称")
    private String name;

    @ApiModelProperty(value = "唯一标识符")
    private String identifier;

    @ApiModelProperty(value = "action调用方式，1（同步调用）或2（异步调用）")
    private Integer actionCallType;

    @ApiModelProperty(value = "是否必选：是1，否0")
    private Integer required;

    @ApiModelProperty(value = "输入参数")
    private List<ThingParamSpecCache> inputData;

    @ApiModelProperty(value = "输出参数")
    private List<ThingParamSpecCache> outputData;

}
