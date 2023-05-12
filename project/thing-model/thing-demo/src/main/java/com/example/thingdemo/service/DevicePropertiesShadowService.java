package com.example.thingdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.thingdemo.domain.DevicePropertiesShadowEntity;
import com.example.thingdemo.vo.DevicePropertyUpdateVo;
import com.example.thingdemo.vo.DevicePropertiesShadowInitVo;
import com.example.thingdemo.vo.DevicePropertiesShadowUpdateCurrentVo;

import java.util.List;

/**
 * <p>
 * 设备属性影子 服务类
 * </p>
 *
 * @author chenpq
 * @since 2023-05-07 21:53:04
 */
public interface DevicePropertiesShadowService extends IService<DevicePropertiesShadowEntity> {

    /**
     * 获取设备属性影子
     *
     * @param productKey
     * @param deviceCode
     * @return
     */
    List<DevicePropertiesShadowEntity> getShadows(String productKey, String deviceCode);

    /**
     * 初始化设备属性影子
     *
     * @param initVo
     */
    void initDevicePropertiesShadow(DevicePropertiesShadowInitVo initVo);

    /**
     * 更新期望值
     *
     * @param updateVo
     * @return
     */
    boolean updateExpectValue(DevicePropertyUpdateVo updateVo);

    /**
     * 更新实际值
     *
     * @param updateVo
     * @return
     */
    boolean updateCurrentValue(DevicePropertiesShadowUpdateCurrentVo updateVo);

}
