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
import java.util.List;

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

  @ApiModelProperty(value = "产品名称")
  private String name;

  @ApiModelProperty(value = "产品key")
  private String productKey;

  @ApiModelProperty(value = "版本")
  private String version;

  @ApiModelProperty(value = "创建时间")
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  private LocalDateTime createTime;

  @ApiModelProperty(value = "更新时间")
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  private LocalDateTime updateTime;

  private List<TingDimensionDetailDto> dimensionList;

}
