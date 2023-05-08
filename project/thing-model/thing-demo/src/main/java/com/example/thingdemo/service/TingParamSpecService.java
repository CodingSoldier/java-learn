package com.example.thingdemo.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.thingdemo.domain.TingParamSpecEntity;
import com.example.thingdemo.dto.TingParamSpecDetailDto;
import com.example.thingdemo.vo.TingParamSpecAddUpdateVo;

import java.util.List;

/**
 * <p>
 * 物模型3维度数据规格 服务类
 * </p>
 *
 * @author chenpq
 * @since 2023-05-05 17:00:52
 */
public interface TingParamSpecService extends IService<TingParamSpecEntity> {

    /**
     * 新增修改
     *
     * @param tingId
     * @param tingDimensionId
     * @param addList
     */
    void addUpdate(Long tingId, Long tingDimensionId, List<TingParamSpecAddUpdateVo> addList);

    /**
     * 详情
     *
     * @param id id
     * @return 详情
     */
    TingParamSpecDetailDto detail(Long id);

}
