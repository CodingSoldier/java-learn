package com.example.thingdemo.ao;

import com.example.thingdemo.vo.DimensionAddVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
* @author chenpq
* @since 2023-05-04 14:09:09
*/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "物模型3维度类型，1（properties），2（event），3（action）-新增修改ao")
public class TingDimensionAddUpdateAo implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "物模型id", required = true)
  @NotNull(message = "物模型id不能为空")
  private Long tingId;

  @ApiModelProperty(value = "物模型维度", required = true)
  @NotNull(message = "维度不能为空")
  @Size(min = 1, message = "维度不能为空")
  private List<@Valid DimensionAddVo> dimensionList;

}
