package com.example.thingdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.thingdemo.domain.DeviceShadowEntity;
import com.example.thingdemo.vo.DeviceShadowInitVo;
import com.example.thingdemo.vo.DeviceShadowUpdateCurrentVo;
import com.example.thingdemo.vo.DeviceShadowUpdateExpectVo;

import java.util.List;

/**
 * <p>
 * 设备影子 服务类
 * </p>
 *
 * @author chenpq
 * @since 2023-05-07 21:53:04
 */
public interface DeviceShadowService extends IService<DeviceShadowEntity> {

    /**
     * 获取设备影子
     *
     * @param productKey
     * @param deviceCode
     * @return
     */
    List<DeviceShadowEntity> getShadows(String productKey, String deviceCode);

    /**
     * 初始化设备影子
     *
     * @param initVo
     */
    void initDeviceShadow(DeviceShadowInitVo initVo);

    /**
     * 更新期望值
     *
     * @param updateVo
     * @return
     */
    boolean updateExpectValue(DeviceShadowUpdateExpectVo updateVo);

    /**
     * 更新实际值
     *
     * @param updateVo
     * @return
     */
    boolean updateCurrentValue(DeviceShadowUpdateCurrentVo updateVo);

}
