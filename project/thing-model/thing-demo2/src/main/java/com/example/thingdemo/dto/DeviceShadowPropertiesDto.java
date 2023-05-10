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

/**
 * @author chenpq
 * @since 2023-05-07 21:53:04
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "设备影子-详情返回值")
public class DeviceShadowPropertiesDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "properties唯一标识符")
    private String identifier;

    @ApiModelProperty(value = "设备实际值")
    private String currentValue;

    @ApiModelProperty(value = "期望值")
    private String expectValue;

    @ApiModelProperty(value = "值数据类型")
    private String valueDataType;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
