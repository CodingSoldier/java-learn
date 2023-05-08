package com.example.thingdemo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author chenpq
 * @since 2023-05-07 21:53:04
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "设备影子-更新期望值请求体")
public class DevicePropertyUpdateVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备id")
    @NotNull(message = "设备id不能为空")
    private Long deviceId;

    @ApiModelProperty(value = "properties唯一标识符")
    @NotEmpty(message = "identifier不能为空")
    private String identifier;

    @ApiModelProperty(value = "期望值")
    @NotNull(message = "期望值不能为空")
    private Object expectValue;

}
