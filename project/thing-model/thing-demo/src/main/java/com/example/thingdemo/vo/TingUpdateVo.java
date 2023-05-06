package com.example.thingdemo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
* @author chenpq
* @since 2023-05-04 14:08:27
*/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "物模型概述-修改请求体")
public class TingUpdateVo implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "主键", required = true)
  @NotNull(message = "主键不能为空")
  private Long id;

  @ApiModelProperty(value = "物模型名称", required = true)
  @NotEmpty(message = "物模型名称不能为空")
  private String name;

  @ApiModelProperty(value = "版本，版本号必须是纯数字和英文点号", example = "1.0.0")
  @Pattern(regexp="^[1-9]\\d?(\\.([1-9]?\\d)){2}$", message = "版本号必须是纯数字和英文点号，例如：1.0.0")
  private String version;

}
