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
 * @since 2023-05-07 21:52:39
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "设备-新增请求体")
public class DeviceAddVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备名称")
    @NotEmpty(message = "设备名称不能为空")
    private String deviceName;

    @ApiModelProperty(value = "设备编码")
    @NotEmpty(message = "设备编码不能为空")
    private String deviceCode;

    @ApiModelProperty(value = "产品key")
    @NotEmpty(message = "产品不能为空")
    private String productKey;

}
