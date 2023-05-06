package com.example.thingdemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 物模型3维度类型，1（properties），2（event），3（action）
 * </p>
 *
 * @author chenpq
 * @since 2023-05-04 14:09:09
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("ting_dimension")
@ApiModel(value = "TingDimensionEntity对象", description = "物模型3维度类型，1（properties），2（event），3（action）")
public class TingDimensionEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "主键")
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @ApiModelProperty(value = "物模型id")
  private Long tingId;

  @ApiModelProperty(value = "物模型维度名称")
  private String name;

  @ApiModelProperty(value = "物模型维度描述")
  private String description;

  @ApiModelProperty(value = "维度类型，1（properties），2（event），3（action）")
  private Integer dimension;

  @ApiModelProperty(value = "唯一标识符")
  private String identifier;

  @ApiModelProperty(value = "属性读写类型：只读（r）或读写（rw）")
  private String propertiesAccessMode;

  @ApiModelProperty(value = "action调用方式，1（同步调用）或2（异步调用）")
  private Integer actionCallType;

  @ApiModelProperty(value = "事件类型 1信息，2告警，3故障")
  private Integer eventType;

  @ApiModelProperty(value = "是否必选：是1，否0")
  private Integer required;

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
