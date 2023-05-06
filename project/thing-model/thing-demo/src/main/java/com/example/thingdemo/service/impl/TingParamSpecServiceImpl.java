package com.example.thingdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.thingdemo.ao.TingParamSpecAddUpdateAo;
import com.example.thingdemo.domain.TingParamSpecEntity;
import com.example.thingdemo.dto.TingParamSpecDetailDto;
import com.example.thingdemo.mapper.TingParamSpecMapper;
import com.example.thingdemo.service.TingParamSpecService;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 物模型3维度数据规格 服务实现类
 * </p>
 *
 * @author chenpq
 * @since 2023-05-05 17:00:52
 */
@Slf4j
@Service
public class TingParamSpecServiceImpl extends ServiceImpl<TingParamSpecMapper, TingParamSpecEntity> implements TingParamSpecService {

  @Autowired
  private TingParamSpecMapper tingParamSpecMapper;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Long addUpdate(TingParamSpecAddUpdateAo addUpdateAo) {
    // id 为空，新增；id 不为空，修改
    Long id = addUpdateAo.getId();
    //if (isRepeat(addUpdateAo.getId(), TingParamSpecEntity::getName, addUpdateAo.getName())){
    //  throw new AppException(RespCodeEnum.PRECONDITION_FAILED.getCode(), "修改失败，XX已存在。请修改XX。");
    //}
    TingParamSpecEntity tingParamSpecEntity = new TingParamSpecEntity();
    BeanUtils.copyProperties(addUpdateAo, tingParamSpecEntity);
    if (Objects.isNull(id)) {
      // 新增
      super.save(tingParamSpecEntity);
      id = tingParamSpecEntity.getId();
    } else {
      // 修改
      super.updateById(tingParamSpecEntity);
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
  public TingParamSpecDetailDto detail(Long id) {
    TingParamSpecEntity tingParamSpecEntity = super.getById(id);
    if (tingParamSpecEntity == null) {
      return null;
    }
    TingParamSpecDetailDto detailDto = new TingParamSpecDetailDto();
    BeanUtils.copyProperties(tingParamSpecEntity, detailDto);
    return detailDto;
  }


  @Override
  public boolean isRepeat(Long id, SFunction<TingParamSpecEntity,?> func, String value) {
    LambdaQueryWrapper<TingParamSpecEntity> lqw = Wrappers.lambdaQuery();
    lqw.eq(TingParamSpecEntity::getIsDel, 0);
    lqw.eq(func, value);
    if (Objects.nonNull(id)) {
      lqw.ne(TingParamSpecEntity::getId, id);
    }
    return super.count(lqw) > 0;
  }

}
