package com.example.thingdemo.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.thingdemo.domain.TingDimensionEntity;
import com.example.thingdemo.dto.TingDimensionDetailDto;
import com.example.thingdemo.vo.DimensionAddVo;
import com.example.thingdemo.vo.DimensionUpdateVo;
import java.util.List;

/**
 * <p>
 * 物模型3维度类型，1（properties），2（event），3（action） 服务类
 * </p>
 *
 * @author chenpq
 * @since 2023-05-04 14:09:09
 */
public interface TingDimensionService extends IService<TingDimensionEntity> {

  /**
   * 批量新增
   * @param tingId
   * @param dimensionList
   */
  void addBatch(Long tingId, List<DimensionAddVo> dimensionList);

  /**
   * 更新单个维度
   * @param updateVo
   * @return
   */
  boolean update(DimensionUpdateVo updateVo);


  /**
   * 删除
   * @param id id
   * @return 是否成功
   */
  boolean delete(Long id);

  /**
   * 详情
   * @param id id
   * @return 详情
   */
  TingDimensionDetailDto detail(Long id);


  /**
   * 是否重复
   * @param id id
   * @param func 列函数
   * @param value 列值
   * @return 是否重复
   */
  boolean isRepeat(Long id, SFunction<TingDimensionEntity,?> func, String value);

}
