package com.example.thingdemo.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.thingdemo.ao.ThingAddUpdateAo;
import com.example.thingdemo.cache.ThingCache;
import com.example.thingdemo.domain.ThingEntity;
import com.example.thingdemo.dto.ThingDetailDto;

/**
 * <p>
 * 物模型概述 服务类
 * </p>
 *
 * @author chenpq
 * @since 2023-05-04 14:08:27
 */
public interface ThingService extends IService<ThingEntity> {

    /**
     * 新增/修改
     *
     * @param addUpdateAo 新增/修改 参数
     * @return id
     */
    Long addUpdate(ThingAddUpdateAo addUpdateAo);


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
    ThingDetailDto detail(Long id);

    /**
     * 获取物模型缓存
     * @param productKey
     * @return
     */
    ThingCache getThingCache(String productKey);

    /**
     * 删除物模型缓存
     * @param productKey
     * @return
     */
    boolean removeThingCache(String productKey);

    String getThingVersionCache(String productKey);

    boolean removeThingVersionCache(String productKey);

    /**
     * 是否重复
     *
     * @param id    id
     * @param func  列函数
     * @param value 列值
     * @return 是否重复
     */
    boolean isRepeat(Long id, SFunction<ThingEntity, ?> func, String value);

}
