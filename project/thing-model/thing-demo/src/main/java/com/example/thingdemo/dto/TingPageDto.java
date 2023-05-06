package com.example.thingdemo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@ApiModel(value = "物模型概述-分页-返回值")
public class TingPageDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "主键")
  private Long id;

  @ApiModelProperty(value = "物模型名称")
  private String name;

  @ApiModelProperty(value = "物模型key")
  private String tingKey;

  @ApiModelProperty(value = "版本")
  private String version;

  @ApiModelProperty(value = "创建时间")
  private LocalDateTime createTime;

  @ApiModelProperty(value = "创建用户id")
  private Long createUser;

  @ApiModelProperty(value = "更新时间")
  private LocalDateTime updateTime;

  @ApiModelProperty(value = "更新用户id")
  private Long updateUser;

  @ApiModelProperty(value = "是否删除，0未删除，1删除")
  private Integer isDel;

}
