package com.example.thingdemo.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.thingdemo.ao.TingParamSpecAddUpdateAo;
import com.example.thingdemo.domain.TingParamSpecEntity;
import com.example.thingdemo.dto.TingParamSpecDetailDto;

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
   * 新增/修改
   * @param addUpdateAo 新增/修改 参数
   * @return id
   */
  Long addUpdate(TingParamSpecAddUpdateAo addUpdateAo);


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
  TingParamSpecDetailDto detail(Long id);

  /**
   * 是否重复
   * @param id id
   * @param func 列函数
   * @param value 列值
   * @return 是否重复
   */
  boolean isRepeat(Long id, SFunction<TingParamSpecEntity,?> func, String value);

}
