package com.example.thingdemo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author chenpq
 * @since 2023-05-07 21:53:04
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "设备影子-更新期望值请求体")
public class DeviceShadowUpdateExpectVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "产品key")
    @NotEmpty(message = "产品key不能为空")
    private String productKey;

    @ApiModelProperty(value = "设备编码")
    @NotEmpty(message = "设备编码不能为空")
    private String deviceCode;

    @ApiModelProperty(value = "properties唯一标识符")
    @NotEmpty(message = "identifier不能为空")
    private String identifier;

    @ApiModelProperty(value = "期望值")
    @NotEmpty(message = "期望值不能为空")
    private String expectValue;

}
