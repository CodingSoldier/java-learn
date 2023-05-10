package com.example.thingdemo.controller;

import com.example.thingdemo.common.Result;
import com.example.thingdemo.dto.ThingDimensionDetailDto;
import com.example.thingdemo.exception.AppException;
import com.example.thingdemo.service.ThingDimensionService;
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
@RequestMapping("/thing-dimension")
@Api(value = "物模型3维度", tags = "物模型3维度-API")
public class ThingDimensionController {

    @Autowired
    private ThingDimensionService thingDimensionService;

    @PostMapping("/add/batch")
    @ApiOperation(value = "新增（多个）")
    public Result<Object> add(@RequestBody @Valid DimensionBatchAddVo addVo) {
        List<@Valid DimensionAddVo> dimensionList = addVo.getDimensionList();
        for (DimensionAddVo dimensionAddVo : dimensionList) {
            thingDimensionService.valid(dimensionAddVo);
        }
        ArrayList<Object> notRepeatList = new ArrayList<>();
        for (DimensionAddVo vo : dimensionList) {
            String notRepeat = vo.getDimension() + "-" + vo.getIdentifier();
            if (notRepeatList.contains(notRepeat)) {
                throw new AppException(vo.getIdentifier() + "重复，请修改。");
            }
            notRepeatList.add(notRepeat);
        }
        thingDimensionService.addBatch(addVo.getThingId(), addVo.getDimensionList());
        return Result.success();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改单个维度")
    public Result<Boolean> update(@RequestBody @Valid DimensionUpdateVo updateVo) {
        boolean b = thingDimensionService.update(updateVo);
        return Result.success(b);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除", notes = "返回是否成功")
    public Result<Boolean> delete(@RequestParam("thingId") Long thingId, @RequestParam("id") Long id) {
        boolean b = thingDimensionService.delete(thingId, id);
        return Result.success(b);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "详情")
    public Result<ThingDimensionDetailDto> detail(@RequestParam("thingId") Long thingId, @RequestParam("id") Long id) {
        ThingDimensionDetailDto detail = thingDimensionService.detail(thingId, id);
        return Result.success(detail);
    }

}
