package com.example.thingdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.thingdemo.domain.ThingParamSpecEntity;
import com.example.thingdemo.dto.ThingParamSpecDetailDto;
import com.example.thingdemo.vo.ThingParamSpecAddUpdateVo;

import java.util.List;

/**
 * <p>
 * 物模型3维度数据规格 服务类
 * </p>
 *
 * @author chenpq
 * @since 2023-05-05 17:00:52
 */
public interface ThingParamSpecService extends IService<ThingParamSpecEntity> {

    /**
     * 新增修改
     *
     * @param thingId
     * @param thingDimensionId
     * @param addList
     */
    void addUpdate(Long thingId, Long thingDimensionId, List<ThingParamSpecAddUpdateVo> addList);

    /**
     * 详情
     *
     * @param id id
     * @return 详情
     */
    ThingParamSpecDetailDto detail(Long id);

}
