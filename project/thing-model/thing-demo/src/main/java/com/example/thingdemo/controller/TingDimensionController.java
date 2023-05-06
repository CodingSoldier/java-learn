package com.example.thingdemo.controller;

import com.example.thingdemo.common.Result;
import com.example.thingdemo.service.TingDimensionService;
import com.example.thingdemo.vo.DimensionBatchAddVo;
import com.example.thingdemo.vo.DimensionUpdateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    tingDimensionService.addBatch(addVo.getTingId(), addVo.getDimensionList());
    return Result.success();
  }

  @PostMapping("/update")
  @ApiOperation(value = "修改单个维度")
  public Result<Boolean> update(@RequestBody @Valid DimensionUpdateVo updateVo) {
    boolean b = tingDimensionService.update(updateVo);
    return Result.success(b);
  }

  //@DeleteMapping("/delete/{id}")
  //@ApiOperation(value = "删除", notes = "返回是否成功")
  //public Result<Boolean> delete(@PathVariable("id") Long id) {
  //  boolean b = tingDimensionService.delete(id);
  //  return Result.success(b);
  //}
  //
  //@GetMapping("/detail/{id}")
  //@ApiOperation(value = "详情")
  //public Result<TingDimensionDetailDto> detail(@PathVariable("id") Long id) {
  //  TingDimensionDetailDto detail = tingDimensionService.detail(id);
  //  return Result.success(detail);
  //}



}
