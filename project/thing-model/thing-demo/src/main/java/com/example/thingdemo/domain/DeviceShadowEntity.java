package com.example.thingdemo.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备影子
 * </p>
 *
 * @author chenpq
 * @since 2023-05-07 21:53:04
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("device_shadow")
@ApiModel(value = "DeviceShadowEntity对象", description = "设备影子")
public class DeviceShadowEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "产品key")
    private String productKey;

    @ApiModelProperty(value = "设备编码")
    private String deviceCode;

    @ApiModelProperty(value = "properties唯一标识符")
    private String identifier;

    @ApiModelProperty(value = "设备实际值")
    private String currentValue;

    @ApiModelProperty(value = "期望值")
    private String expectValue;

    @ApiModelProperty(value = "值数据类型")
    private String valueDataType;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建用户id")
    private Long createUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新用户id")
    private Long updateUser;

    @ApiModelProperty(value = "是否删除，0未删除，1删除")
    @TableLogic
    private Integer isDel;


}
