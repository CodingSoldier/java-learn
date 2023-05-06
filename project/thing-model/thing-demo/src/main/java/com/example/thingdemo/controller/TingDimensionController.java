package com.example.thingdemo.controller;

import com.example.thingdemo.ao.TingDimensionAddUpdateAo;
import com.example.thingdemo.common.Result;
import com.example.thingdemo.service.TingDimensionService;
import com.example.thingdemo.vo.DimensionBatchAddVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

  @PostMapping("/add")
  @ApiOperation(value = "新增", notes = "返回id")
  public Result<Object> add(@RequestBody @Valid DimensionBatchAddVo addVo) {
    TingDimensionAddUpdateAo addAo = new TingDimensionAddUpdateAo();
    BeanUtils.copyProperties(addVo, addAo);
    tingDimensionService.add(addAo);
    return Result.success();
  }

  //@PostMapping("/update")
  //@ApiOperation(value = "修改", notes = "返回id")
  //public Result<Long> update(@RequestBody @Valid TingDimensionUpdateVo updateVo) {
  //  TingDimensionAddUpdateAo updateAo = new TingDimensionAddUpdateAo();
  //  BeanUtils.copyProperties(updateVo, updateAo);
  //  Long id = tingDimensionService.addUpdate(updateAo);
  //  return Result.success(id);
  //}

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
