package com.example.thingdemo.ao;

import com.example.thingdemo.vo.TingParamSpecAddVo;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
* @author chenpq
* @since 2023-05-05 17:00:52
*/
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "物模型3维度数据规格-新增修改ao")
public class TingParamSpecAddUpdateAo extends TingParamSpecAddVo {

  @ApiModelProperty(value = "主键")
  private Long id;

}
