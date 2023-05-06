package com.example.thingdemo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
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
@ApiModel(value = "物模型概述-详情返回值")
public class TingDetailDto implements Serializable {

	private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "物模型id")
  private Long id;

  @ApiModelProperty(value = "物模型名称")
  private String name;

  @ApiModelProperty(value = "物模型key")
  private String tingKey;

  @ApiModelProperty(value = "版本")
  private String version;

  @ApiModelProperty(value = "创建时间")
  private LocalDateTime createTime;

  @ApiModelProperty(value = "更新时间")
  private LocalDateTime updateTime;

  private List<TingDimensionDetailDto> dimensionList;

}
