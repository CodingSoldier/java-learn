package com.example.thingdemo.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.thingdemo.ao.DeviceAddUpdateAo;
import com.example.thingdemo.domain.DeviceEntity;
import com.example.thingdemo.dto.DeviceDetailDto;

/**
 * <p>
 * 设备 服务类
 * </p>
 *
 * @author chenpq
 * @since 2023-05-07 21:52:39
 */
public interface DeviceService extends IService<DeviceEntity> {

    /**
     * 新增
     *
     * @param addUpdateAo
     * @return
     */
    Long add(DeviceAddUpdateAo addUpdateAo);

    /**
     * 更新
     * @param addUpdateAo
     * @return
     */
    boolean update(DeviceAddUpdateAo addUpdateAo);

    /**
     * 删除
     *
     * @param id id
     * @return 是否成功
     */
    boolean delete(Long id);

    /**
     * 详情
     *
     * @param id id
     * @return 详情
     */
    DeviceDetailDto detail(Long id);

    /**
     * 是否重复
     *
     * @param id
     * @param productKey
     * @param func
     * @param value
     * @return
     */
    boolean isRepeat(Long id, String productKey, SFunction<DeviceEntity, ?> func, String value);

}
