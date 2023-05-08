package com.example.thingdemo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author chenpq
 * @since 2023-05-04 14:09:09
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "物模型-新增请求体")
public class DimensionBatchAddVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "物模型id", required = true)
    @NotNull(message = "物模型id不能为空")
    private Long thingId;

    @ApiModelProperty(value = "物模型维度", required = true)
    @NotNull(message = "维度不能为空")
    @Size(min = 1, message = "维度不能为空")
    private List<@Valid DimensionAddVo> dimensionList;

}
