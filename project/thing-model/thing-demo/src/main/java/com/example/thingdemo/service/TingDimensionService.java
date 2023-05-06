package com.example.thingdemo.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.thingdemo.ao.TingDimensionAddUpdateAo;
import com.example.thingdemo.domain.TingDimensionEntity;
import com.example.thingdemo.dto.TingDimensionDetailDto;

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
   * 新增
   * @param addUpdateAo 新增/修改 参数
   * @return id
   */
  void add(TingDimensionAddUpdateAo addUpdateAo);

  Long update(TingDimensionAddUpdateAo addUpdateAo);


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
