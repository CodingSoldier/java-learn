package com.example.thingdemo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author chenpq
 * @since 2023-05-07 21:53:04
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "设备影子-更新实际值请求体")
public class DeviceShadowUpdateCurrentVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "产品key")
    private String productKey;

    @ApiModelProperty(value = "设备编码")
    private String deviceCode;

    @ApiModelProperty(value = "properties唯一标识符")
    private String identifier;

    @ApiModelProperty(value = "设备实际值")
    private String currentValue;

}
