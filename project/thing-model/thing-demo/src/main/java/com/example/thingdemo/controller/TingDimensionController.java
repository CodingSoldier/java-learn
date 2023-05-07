package com.example.thingdemo.controller;

import com.example.thingdemo.common.Result;
import com.example.thingdemo.dto.TingDimensionDetailDto;
import com.example.thingdemo.exception.AppException;
import com.example.thingdemo.service.TingDimensionService;
import com.example.thingdemo.vo.DimensionAddVo;
import com.example.thingdemo.vo.DimensionBatchAddVo;
import com.example.thingdemo.vo.DimensionUpdateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 物模型3维度类型，1（properties），2（event），3（action） 前端控制器
 * </p>
 *
 * @author chenpq
 * @since 2023-05-04 14:09:09
 */
@Slf4j
@RestController
@RequestMapping("/ting-dimension")
@Api(value = "物模型3维度", tags = "物模型3维度-API")
public class TingDimensionController {

  @Autowired
  private TingDimensionService tingDimensionService;

  @PostMapping("/add/batch")
  @ApiOperation(value = "新增（多个）")
  public Result<Object> add(@RequestBody @Valid DimensionBatchAddVo addVo) {
    List<@Valid DimensionAddVo> dimensionList = addVo.getDimensionList();
    for (DimensionAddVo dimensionAddVo : dimensionList) {
      tingDimensionService.valid(dimensionAddVo);
    }
    ArrayList<Object> notRepeatList = new ArrayList<>();
    for (DimensionAddVo vo : dimensionList) {
      String notRepeat = vo.getDimension() + "-" + vo.getIdentifier();
      if (notRepeatList.contains(notRepeat)) {
        throw new AppException(vo.getIdentifier() + "重复，请修改。");
      }
      notRepeatList.add(notRepeat);
    }
    tingDimensionService.addBatch(addVo.getTingId(), addVo.getDimensionList());
    return Result.success();
  }

  @PostMapping("/update")
  @ApiOperation(value = "修改单个维度")
  public Result<Boolean> update(@RequestBody @Valid DimensionUpdateVo updateVo) {
    boolean b = tingDimensionService.update(updateVo);
    return Result.success(b);
  }

  @DeleteMapping("/delete")
  @ApiOperation(value = "删除", notes = "返回是否成功")
  public Result<Boolean> delete(@RequestParam("tingId")Long tingId, @RequestParam("id") Long id) {
   boolean b = tingDimensionService.delete(tingId, id);
   return Result.success(b);
  }

  @GetMapping("/detail")
  @ApiOperation(value = "详情")
  public Result<TingDimensionDetailDto> detail(@RequestParam("tingId")Long tingId, @RequestParam("id") Long id) {
   TingDimensionDetailDto detail = tingDimensionService.detail(tingId, id);
   return Result.success(detail);
  }

}
