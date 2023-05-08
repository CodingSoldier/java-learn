package com.example.thingdemo.controller;

import com.example.thingdemo.ao.TingAddUpdateAo;
import com.example.thingdemo.common.Result;
import com.example.thingdemo.constant.TingConstant;
import com.example.thingdemo.dto.TingDetailDto;
import com.example.thingdemo.service.TingService;
import com.example.thingdemo.vo.TingAddVo;
import com.example.thingdemo.vo.TingUpdateVo;
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
@RequestMapping("/ting")
@Api(value = "产品", tags = "产品-API")
public class TingController {

    @Autowired
    private TingService tingService;

    @PostMapping("/add")
    @ApiOperation(value = "新增", notes = "返回id")
    public Result<Long> add(@RequestBody @Valid TingAddVo addVo) {
        TingAddUpdateAo addAo = new TingAddUpdateAo();
        BeanUtils.copyProperties(addVo, addAo);
        if (StringUtils.isBlank(addAo.getVersion())) {
            addAo.setVersion(TingConstant.VERSION_100);
        }
        Long id = tingService.addUpdate(addAo);
        return Result.success(id);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改", notes = "返回id")
    public Result<Long> update(@RequestBody @Valid TingUpdateVo updateVo) {
        TingAddUpdateAo updateAo = new TingAddUpdateAo();
        BeanUtils.copyProperties(updateVo, updateAo);
        Long id = tingService.addUpdate(updateAo);
        return Result.success(id);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除", notes = "返回是否成功")
    public Result<Boolean> delete(@PathVariable("id") Long id) {
        boolean b = tingService.delete(id);
        return Result.success(b);
    }


    @GetMapping("/detail/{id}")
    @ApiOperation(value = "详情")
    public Result<TingDetailDto> detail(@PathVariable("id") Long id) {
        TingDetailDto detail = tingService.detail(id);
        return Result.success(detail);
    }


}
