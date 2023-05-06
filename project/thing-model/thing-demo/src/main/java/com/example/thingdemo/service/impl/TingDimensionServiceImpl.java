package com.example.thingdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.thingdemo.ao.TingDimensionAddUpdateAo;
import com.example.thingdemo.domain.TingDimensionEntity;
import com.example.thingdemo.dto.TingDimensionDetailDto;
import com.example.thingdemo.exception.AppException;
import com.example.thingdemo.mapper.TingDimensionMapper;
import com.example.thingdemo.service.TingDimensionService;
import com.example.thingdemo.vo.DimensionAddVo;
import com.example.thingdemo.vo.TingParamSpecAddVo;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 物模型3维度类型，1（properties），2（event），3（action） 服务实现类
 * </p>
 *
 * @author chenpq
 * @since 2023-05-04 14:09:09
 */
@Slf4j
@Service
public class TingDimensionServiceImpl extends ServiceImpl<TingDimensionMapper, TingDimensionEntity> implements TingDimensionService {

  @Autowired
  private TingDimensionMapper tingDimensionMapper;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void add(TingDimensionAddUpdateAo ao) {
    List<DimensionAddVo> dimensionList = ao.getDimensionList();
    if (CollectionUtils.isEmpty(dimensionList)) {
      throw new AppException("dimensionList不能为空。");
    }

    for (DimensionAddVo addVo : dimensionList) {
      TingDimensionEntity tingDimensionEntity = new TingDimensionEntity();
      BeanUtils.copyProperties(addVo, tingDimensionEntity);
      tingDimensionEntity.setTingId(ao.getTingId());
      super.save(tingDimensionEntity);
      Long id = tingDimensionEntity.getId();

      // 数据
      List<TingParamSpecAddVo> paramSpecList = addVo.getParamSpecList();

    }

  }

  //private void vaild(TingDimensionAddUpdateAo ao) {
  //  List<DimensionAddVo> dimensionList = ao.getDimensionList();
  //  if (CollectionUtils.isNotEmpty(dimensionList)) {
  //    for (DimensionAddVo addVo : dimensionList) {
  //      if (DimensionEnum.addVo.getDimension())
  //    }
  //  }
  //}

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Long update(TingDimensionAddUpdateAo addUpdateAo) {
    // id 为空，新增；id 不为空，修改
    //Long id = addUpdateAo.getId();
    //if (isRepeat(addUpdateAo.getId(), TingDimensionEntity::getName, addUpdateAo.getName())){
    //  throw new AppException(RespCodeEnum.PRECONDITION_FAILED.getCode(), "修改失败，XX已存在。请修改XX。");
    //}
    //TingDimensionEntity tingDimensionEntity = new TingDimensionEntity();
    //BeanUtils.copyProperties(addUpdateAo, tingDimensionEntity);
    //if (Objects.isNull(id)) {
    //  // 新增
    //  super.save(tingDimensionEntity);
    //  id = tingDimensionEntity.getId();
    //} else {
    //  // 修改
    //  super.updateById(tingDimensionEntity);
    //}
    return 0L;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean delete(Long id) {
    // 删除
    return super.removeById(id);
  }

  @Override
  public TingDimensionDetailDto detail(Long id) {
    TingDimensionEntity tingDimensionEntity = super.getById(id);
    if (tingDimensionEntity == null) {
      return null;
    }
    TingDimensionDetailDto detailDto = new TingDimensionDetailDto();
    BeanUtils.copyProperties(tingDimensionEntity, detailDto);
    return detailDto;
  }

  @Override
  public boolean isRepeat(Long id, SFunction<TingDimensionEntity,?> func, String value) {
    LambdaQueryWrapper<TingDimensionEntity> lqw = Wrappers.lambdaQuery();
    lqw.eq(TingDimensionEntity::getIsDel, 0);
    lqw.eq(func, value);
    if (Objects.nonNull(id)) {
      lqw.ne(TingDimensionEntity::getId, id);
    }
    return super.count(lqw) > 0;
  }

}
