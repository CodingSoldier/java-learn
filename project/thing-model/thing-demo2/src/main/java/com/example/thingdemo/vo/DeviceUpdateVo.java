package com.example.thingdemo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author chenpq
 * @since 2023-05-07 21:52:39
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "设备-修改请求体")
public class DeviceUpdateVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotNull(message = "主键不能为空")
    private Long id;

    @ApiModelProperty(value = "设备名称")
    @NotEmpty(message = "设备名称不能为空")
    private String deviceName;

}
