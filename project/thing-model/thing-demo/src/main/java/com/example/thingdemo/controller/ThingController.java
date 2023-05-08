package com.example.thingdemo.controller;

import com.example.thingdemo.ao.ThingAddUpdateAo;
import com.example.thingdemo.common.Result;
import com.example.thingdemo.constant.ThingConstant;
import com.example.thingdemo.dto.ThingDetailDto;
import com.example.thingdemo.service.ThingService;
import com.example.thingdemo.vo.ThingAddVo;
import com.example.thingdemo.vo.ThingUpdateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 物模型概述 前端控制器
 * </p>
 *
 * @author chenpq
 * @since 2023-05-04 14:08:27
 */
@Slf4j
@RestController
@RequestMapping("/thing")
@Api(value = "产品", tags = "产品-API")
public class ThingController {

    @Autowired
    private ThingService thingService;

    @PostMapping("/add")
    @ApiOperation(value = "新增", notes = "返回id")
    public Result<Long> add(@RequestBody @Valid ThingAddVo addVo) {
        ThingAddUpdateAo addAo = new ThingAddUpdateAo();
        BeanUtils.copyProperties(addVo, addAo);
        if (StringUtils.isBlank(addAo.getVersion())) {
            addAo.setVersion(ThingConstant.VERSION_100);
        }
        Long id = thingService.addUpdate(addAo);
        return Result.success(id);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改", notes = "返回id")
    public Result<Long> update(@RequestBody @Valid ThingUpdateVo updateVo) {
        ThingAddUpdateAo updateAo = new ThingAddUpdateAo();
        BeanUtils.copyProperties(updateVo, updateAo);
        Long id = thingService.addUpdate(updateAo);
        return Result.success(id);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除", notes = "返回是否成功")
    public Result<Boolean> delete(@PathVariable("id") Long id) {
        boolean b = thingService.delete(id);
        return Result.success(b);
    }

    @GetMapping("/detail/{id}")
    @ApiOperation(value = "详情")
    public Result<ThingDetailDto> detail(@PathVariable("id") Long id) {
        ThingDetailDto detail = thingService.detail(id);
        return Result.success(detail);
    }


}
