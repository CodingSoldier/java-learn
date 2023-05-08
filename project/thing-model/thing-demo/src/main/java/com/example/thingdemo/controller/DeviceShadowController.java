package com.example.thingdemo.controller;

import com.example.thingdemo.common.Result;
import com.example.thingdemo.service.DeviceShadowService;
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
 * 设备影子 前端控制器
 * </p>
 *
 * @author chenpq
 * @since 2023-05-07 21:53:04
 */
@Slf4j
@RestController
@RequestMapping("/device-shadow")
@Api(value = "设备影子", tags = "设备影子-API")
public class DeviceShadowController {

    @Autowired
    private DeviceShadowService deviceShadowService;

    @PostMapping("/update/expect-value")
    @ApiOperation(value = "修改")
    public Result<Boolean> update(@RequestBody @Valid DevicePropertyUpdateVo updateVo) {
        boolean b = deviceShadowService.updateExpectValue(updateVo);
        return Result.success(b);
    }

}
