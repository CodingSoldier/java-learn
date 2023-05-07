package com.example.thingdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.thingdemo.ao.TingAddUpdateAo;
import com.example.thingdemo.domain.TingDimensionEntity;
import com.example.thingdemo.domain.TingEntity;
import com.example.thingdemo.domain.TingParamSpecEntity;
import com.example.thingdemo.dto.TingDetailDto;
import com.example.thingdemo.dto.TingDimensionDetailDto;
import com.example.thingdemo.dto.TingParamSpecDetailDto;
import com.example.thingdemo.dto.TingParamSpecJsonElemDto;
import com.example.thingdemo.exception.AppException;
import com.example.thingdemo.mapper.TingMapper;
import com.example.thingdemo.service.TingDimensionService;
import com.example.thingdemo.service.TingParamSpecService;
import com.example.thingdemo.service.TingService;
import com.example.thingdemo.util.CommonUtil;
import com.example.thingdemo.util.CopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
  @Autowired
  private TingDimensionService tingDimensionService;
  @Autowired
  private TingParamSpecService tingParamSpecService;

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
    boolean b = super.removeById(id);

    LambdaQueryWrapper<TingDimensionEntity> dimensionLqw = Wrappers.lambdaQuery();
    dimensionLqw.eq(TingDimensionEntity::getTingId, id);
    tingDimensionService.remove(dimensionLqw);

    LambdaQueryWrapper<TingParamSpecEntity> paramSpecLqw = Wrappers.lambdaQuery();
    paramSpecLqw.eq(TingParamSpecEntity::getTingId, id);
    tingParamSpecService.remove(paramSpecLqw);

    return b;
  }

  @Override
  public TingDetailDto detail(Long id) {
    TingEntity tingEntity = super.getById(id);
    if (tingEntity == null) {
      return null;
    }
    TingDetailDto tingDetail = new TingDetailDto();
    BeanUtils.copyProperties(tingEntity, tingDetail);

    // 维度
    LambdaQueryWrapper<TingDimensionEntity> dimensionLqw = Wrappers.lambdaQuery();
    dimensionLqw.eq(TingDimensionEntity::getTingId, id);
    List<TingDimensionEntity> dimensionList = tingDimensionService.list(dimensionLqw);
    List<TingDimensionDetailDto> dimensionDetailList = CopyUtils.listCopy(dimensionList,
        TingDimensionDetailDto.class);
    tingDetail.setDimensionList(dimensionDetailList);

    if (CollectionUtils.isEmpty(dimensionList)) {
      return tingDetail;
    }

    // 数据规格
    List<Long> dimensionIdList = dimensionList.stream().map(TingDimensionEntity::getId)
        .collect(Collectors.toList());
    LambdaQueryWrapper<TingParamSpecEntity> paramSpecLqw = Wrappers.lambdaQuery();
    paramSpecLqw.eq(TingParamSpecEntity::getTingId, id);
    paramSpecLqw.in(TingParamSpecEntity::getTingDimensionId, dimensionIdList);
    List<TingParamSpecEntity> paramSpecAllDbList = tingParamSpecService.list(paramSpecLqw);

    List<TingParamSpecEntity> parentParamSpecDbList = paramSpecAllDbList.stream()
        .filter(e -> e.getParentId() == null).collect(Collectors.toList());
    List<TingParamSpecEntity> childParamSpecDbList = paramSpecAllDbList.stream()
        .filter(e -> e.getParentId() != null).collect(Collectors.toList());

    dimensionDetailList.forEach(dimension -> {
      List<TingParamSpecEntity> parentList = parentParamSpecDbList.stream()
          .filter(paramSpec -> Objects.equals(dimension.getId(), paramSpec.getTingDimensionId()))
          .collect(Collectors.toList());
      List<TingParamSpecDetailDto> parentDetailList = CopyUtils.listCopy(parentList,
              TingParamSpecDetailDto.class);
      // 数据规格子级
      if (CollectionUtils.isNotEmpty(parentDetailList)) {
        parentDetailList.forEach(parent -> {
          List<TingParamSpecEntity> childList = childParamSpecDbList.stream()
              .filter(child -> Objects.equals(parent.getId(), child.getParentId()))
              .collect(Collectors.toList());
          List<TingParamSpecJsonElemDto> jsonElemList = CopyUtils.listCopy(
              childList, TingParamSpecJsonElemDto.class);
          parent.setJsonElemList(jsonElemList);
        });
        dimension.setParamSpecList(parentDetailList);
      }
    });

    return tingDetail;
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
