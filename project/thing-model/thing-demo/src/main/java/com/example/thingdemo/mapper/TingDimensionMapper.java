package com.example.thingdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.thingdemo.domain.TingDimensionEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 物模型3维度类型，1（properties），2（event），3（action） Mapper 接口
 * </p>
 *
 * @author chenpq
 * @since 2023-05-04 14:09:09
 */
@Mapper
public interface TingDimensionMapper extends BaseMapper<TingDimensionEntity> {

}
