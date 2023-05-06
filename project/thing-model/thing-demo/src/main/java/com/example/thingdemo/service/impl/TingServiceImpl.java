package com.example.thingdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.thingdemo.ao.TingAddUpdateAo;
import com.example.thingdemo.domain.TingEntity;
import com.example.thingdemo.dto.TingDetailDto;
import com.example.thingdemo.exception.AppException;
import com.example.thingdemo.mapper.TingMapper;
import com.example.thingdemo.service.TingService;
import com.example.thingdemo.util.CommonUtil;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 物模型概述 服务实现类
 * </p>
 *
 * @author chenpq
 * @since 2023-05-04 14:08:27
 */
@Slf4j
@Service
public class TingServiceImpl extends ServiceImpl<TingMapper, TingEntity> implements TingService {

  @Autowired
  private TingMapper tingMapper;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Long addUpdate(TingAddUpdateAo addUpdateAo) {
    // id 为空，新增；id 不为空，修改
    Long id = addUpdateAo.getId();
    if (isRepeat(addUpdateAo.getId(), TingEntity::getName, addUpdateAo.getName())){
      throw new AppException("修改失败，物模型名称已存在。请修改物模型名称。");
    }
    TingEntity tingEntity = new TingEntity();
    BeanUtils.copyProperties(addUpdateAo, tingEntity);
    if (Objects.isNull(id)) {
      // 新增
      tingEntity.setTingKey(CommonUtil.uuid32());
      super.save(tingEntity);
      id = tingEntity.getId();
    } else {
      // 修改
      super.updateById(tingEntity);
    }
    return id;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean delete(Long id) {
    // 删除
    return super.removeById(id);
  }

  @Override
  public TingDetailDto detail(Long id) {
    TingEntity tingEntity = super.getById(id);
    if (tingEntity == null) {
      return null;
    }
    TingDetailDto detailDto = new TingDetailDto();
    BeanUtils.copyProperties(tingEntity, detailDto);
    return detailDto;
  }

  @Override
  public boolean isRepeat(Long id, SFunction<TingEntity,?> func, String value) {
    LambdaQueryWrapper<TingEntity> lqw = Wrappers.lambdaQuery();
    lqw.eq(TingEntity::getIsDel, 0);
    lqw.eq(func, value);
    if (Objects.nonNull(id)) {
      lqw.ne(TingEntity::getId, id);
    }
    return super.count(lqw) > 0;
  }

}
