package com.example.thingdemo.controller;

import com.example.thingdemo.common.Result;
import com.example.thingdemo.service.DevicePropertiesShadowService;
import com.example.thingdemo.vo.DevicePropertyUpdateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 设备属性影子 前端控制器
 * </p>
 *
 * @author chenpq
 * @since 2023-05-07 21:53:04
 */
@Slf4j
@RestController
@RequestMapping("/device-properties-shadow")
@Api(value = "设备属性影子", tags = "设备属性影子-API")
public class DevicePropertiesShadowController {

    @Autowired
    private DevicePropertiesShadowService devicePropertiesShadowService;

    @PostMapping("/update/expect-value")
    @ApiOperation(value = "修改")
    public Result<Boolean> update(@RequestBody @Valid DevicePropertyUpdateVo updateVo) {
        boolean b = devicePropertiesShadowService.updateExpectValue(updateVo);
        return Result.success(b);
    }

}
