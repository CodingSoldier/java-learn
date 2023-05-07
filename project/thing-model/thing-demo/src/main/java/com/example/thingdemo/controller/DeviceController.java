package com.example.thingdemo.controller;

import com.example.thingdemo.ao.DeviceAddUpdateAo;
import com.example.thingdemo.common.Result;
import com.example.thingdemo.dto.DeviceDetailDto;
import com.example.thingdemo.service.DeviceService;
import com.example.thingdemo.vo.DeviceAddVo;
import com.example.thingdemo.vo.DeviceUpdateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 设备 前端控制器
 * </p>
 *
 * @author chenpq
 * @since 2023-05-07 21:52:39
 */
@Slf4j
@RestController
@RequestMapping("/device")
@Api(value = "设备", tags = "设备-API")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping("/add")
    @ApiOperation(value = "新增", notes = "返回id")
    public Result<Long> add(@RequestBody @Valid DeviceAddVo addVo) {
        DeviceAddUpdateAo addAo = new DeviceAddUpdateAo();
        BeanUtils.copyProperties(addVo, addAo);
        Long id = deviceService.add(addAo);
        return Result.success(id);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改")
    public Result<Boolean> update(@RequestBody @Valid DeviceUpdateVo updateVo) {
        DeviceAddUpdateAo updateAo = new DeviceAddUpdateAo();
        BeanUtils.copyProperties(updateVo, updateAo);
        boolean b = deviceService.update(updateAo);
        return Result.success(b);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除", notes = "返回是否成功")
    public Result<Boolean> delete(@PathVariable("id") Long id) {
        boolean b = deviceService.delete(id);
        return Result.success(b);
    }

    @GetMapping("/detail/{id}")
    @ApiOperation(value = "详情")
    public Result<DeviceDetailDto> detail(@PathVariable("id") Long id) {
        DeviceDetailDto detail = deviceService.detail(id);
        return Result.success(detail);
    }


}
