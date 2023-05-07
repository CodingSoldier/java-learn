package com.example.thingdemo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author chenpq
 * @since 2023-05-04 14:09:09
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "物模型3维度类型，1（properties），2（event），3（action）-新增请求体")
public class DimensionAddVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "物模型维度名称", required = true)
    @NotEmpty(message = "物模型维度名称不能为空")
    private String name;

    @ApiModelProperty(value = "物模型维度描述")
    private String description;

    @ApiModelProperty(value = "维度类型，1（properties），2（event），3（action）")
    @NotNull(message = "维度类型不能为空")
    @Min(value = 1, message = "维度类型只能是1、2、3")
    @Max(value = 3, message = "维度类型只能是1、2、3")
    private Integer dimension;

    @ApiModelProperty(value = "唯一标识符")
    @NotEmpty(message = "唯一标识符不能为空")
    private String identifier;

    @ApiModelProperty(value = "属性读写类型：只读（r）或读写（rw）")
    @Pattern(regexp = "^(r|rw)$", message = "属性读写类型错误，只能是r、rw")
    private String propertiesAccessMode;

    @ApiModelProperty(value = "action调用方式，1（同步调用）或2（异步调用）")
    @Min(value = 1, message = "action调用方式错误，只能是1、2")
    @Max(value = 2, message = "action调用方式错误，只能是1、2")
    private Integer actionCallType;

    @ApiModelProperty(value = "事件类型 1信息，2告警，3故障")
    @Min(value = 1, message = "事件类型错误，只能是1、2、3")
    @Max(value = 3, message = "事件类型错误，只能是1、2、3")
    private Integer eventType;

    @ApiModelProperty(value = "数据规格")
    @Valid
    @NotNull(message = "数据规格不能为空")
    private List<TingParamSpecAddUpdateVo> paramSpecList;


}
