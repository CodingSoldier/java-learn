package com.example.thingdemo.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 物模型概述
 * </p>
 *
 * @author chenpq
 * @since 2023-05-04 14:08:27
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("ting")
@ApiModel(value = "TingEntity对象", description = "物模型概述")
public class TingEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "主键")
  @TableId(value = "id", type = IdType.AUTO)
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
  @TableLogic
  private Integer isDel;


}
