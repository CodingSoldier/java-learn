package com.example.thingdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.thingdemo.domain.ThingDimensionEntity;
import com.example.thingdemo.domain.ThingEntity;
import com.example.thingdemo.domain.ThingParamSpecEntity;
import com.example.thingdemo.dto.ThingDimensionDetailDto;
import com.example.thingdemo.dto.ThingParamSpecDetailDto;
import com.example.thingdemo.dto.ThingParamSpecJsonElemDto;
import com.example.thingdemo.enums.DimensionEnum;
import com.example.thingdemo.exception.AppException;
import com.example.thingdemo.mapper.ThingDimensionMapper;
import com.example.thingdemo.service.ThingDimensionService;
import com.example.thingdemo.service.ThingParamSpecService;
import com.example.thingdemo.service.ThingService;
import com.example.thingdemo.util.CopyUtils;
import com.example.thingdemo.vo.DimensionAddVo;
import com.example.thingdemo.vo.DimensionUpdateVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
public class ThingDimensionServiceImpl extends ServiceImpl<ThingDimensionMapper, ThingDimensionEntity> implements ThingDimensionService {

    @Autowired
    private ThingDimensionMapper thingDimensionMapper;
    @Autowired
    private ThingParamSpecService thingParamSpecService;
    @Autowired
    private ThingService thingService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBatch(Long thingId, List<DimensionAddVo> dimensionList) {
        if (CollectionUtils.isEmpty(dimensionList)) {
            throw new AppException("dimensionList不能为空。");
        }

        // 校验Identifier是否重复
        for (DimensionAddVo vo : dimensionList) {
            isRepeat(null, thingId, vo.getDimension(), ThingDimensionEntity::getIdentifier, vo.getIdentifier());
        }

        // 新增维度
        for (DimensionAddVo addVo : dimensionList) {
            ThingDimensionEntity thingDimensionEntity = new ThingDimensionEntity();
            BeanUtils.copyProperties(addVo, thingDimensionEntity);
            thingDimensionEntity.setThingId(thingId);
            super.save(thingDimensionEntity);
            Long dimensionId = thingDimensionEntity.getId();

            // 新增数据规格
            thingParamSpecService.addUpdate(thingId, dimensionId, addVo.getParamSpecList());
        }

        ThingEntity thingDb = thingService.getById(thingId);
        if (thingDb != null) {
            // 删除缓存
            thingService.removeThingCache(thingDb.getProductKey());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(DimensionUpdateVo updateVo) {
        ThingDimensionEntity dimensionDb = super.getById(updateVo.getId());
        if (dimensionDb == null) {
            return false;
        }

        isRepeat(updateVo.getId(), dimensionDb.getThingId(), dimensionDb.getDimension(), ThingDimensionEntity::getIdentifier, updateVo.getIdentifier());

        // 校验
        DimensionAddVo dimensionAddVo = new DimensionAddVo();
        BeanUtils.copyProperties(updateVo, dimensionAddVo);
        valid(dimensionAddVo);

        ThingDimensionEntity thingDimensionEntity = new ThingDimensionEntity();
        BeanUtils.copyProperties(updateVo, thingDimensionEntity);

        if (DimensionEnum.PROPERTIES.getCode().equals(thingDimensionEntity.getDimension())) {
            thingDimensionEntity.setEventType(null);
            thingDimensionEntity.setActionCallType(null);
        }
        if (DimensionEnum.EVENT.getCode().equals(thingDimensionEntity.getDimension())) {
            thingDimensionEntity.setPropertiesAccessMode("");
            thingDimensionEntity.setActionCallType(null);
        }
        if (DimensionEnum.ACTION.getCode().equals(thingDimensionEntity.getDimension())) {
            thingDimensionEntity.setPropertiesAccessMode("");
            thingDimensionEntity.setEventType(null);
        }

        super.updateById(thingDimensionEntity);
        Long dimensionId = dimensionDb.getId();
        Long thingId = dimensionDb.getThingId();

        // 新增数据规格
        thingParamSpecService.addUpdate(thingId, dimensionId, updateVo.getParamSpecList());

        ThingEntity thingDb = thingService.getById(thingId);
        if (thingDb != null) {
            // 删除缓存
            thingService.removeThingCache(thingDb.getProductKey());
        }

        return true;
    }

    @Override
    public void valid(DimensionAddVo vo) {
        if (vo == null) {
            return;
        }
        if (DimensionEnum.PROPERTIES.getCode().equals(vo.getDimension())) {
            if (StringUtils.isBlank(vo.getPropertiesAccessMode())) {
                throw new AppException("属性的读写类型不能为空");
            }
            vo.setEventType(null);
            vo.setActionCallType(null);
        }
        if (DimensionEnum.EVENT.getCode().equals(vo.getDimension())) {
            if (vo.getEventType() == null) {
                throw new AppException("事件的事件类型不能为空");
            }
            vo.setPropertiesAccessMode("");
            vo.setActionCallType(null);
        }
        if (DimensionEnum.ACTION.getCode().equals(vo.getDimension())) {
            if (vo.getActionCallType() == null) {
                throw new AppException("动作的调用方式不能为空");
            }
            vo.setPropertiesAccessMode("");
            vo.setEventType(null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long thingId, Long id) {
        ThingEntity thingDb = thingService.getById(thingId);

        LambdaQueryWrapper<ThingDimensionEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(ThingDimensionEntity::getThingId, thingId);
        lqw.eq(ThingDimensionEntity::getId, id);
        boolean b = super.remove(lqw);

        LambdaQueryWrapper<ThingParamSpecEntity> paramSpecLqw = Wrappers.lambdaQuery();
        paramSpecLqw.eq(ThingParamSpecEntity::getThingId, thingId);
        paramSpecLqw.eq(ThingParamSpecEntity::getThingDimensionId, id);
        thingParamSpecService.remove(paramSpecLqw);

        if (thingDb != null) {
            // 删除缓存
            thingService.removeThingCache(thingDb.getProductKey());
        }

        return b;
    }

    @Override
    public ThingDimensionDetailDto detail(Long thingId, Long id) {
        LambdaQueryWrapper<ThingDimensionEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(ThingDimensionEntity::getThingId, thingId);
        lqw.eq(ThingDimensionEntity::getId, id);
        ThingDimensionEntity dbEntity = super.getOne(lqw, false);
        if (dbEntity == null) {
            return null;
        }
        ThingDimensionDetailDto detailDto = new ThingDimensionDetailDto();
        BeanUtils.copyProperties(dbEntity, detailDto);

        // 数据规格
        LambdaQueryWrapper<ThingParamSpecEntity> paramSpecLqw = Wrappers.lambdaQuery();
        paramSpecLqw.eq(ThingParamSpecEntity::getThingDimensionId, id);
        List<ThingParamSpecEntity> paramSpecAllDbList = thingParamSpecService.list(paramSpecLqw);

        List<ThingParamSpecEntity> parentParamSpecDbList = paramSpecAllDbList.stream()
                .filter(e -> e.getParentId() == null).collect(Collectors.toList());
        List<ThingParamSpecEntity> childParamSpecDbList = paramSpecAllDbList.stream()
                .filter(e -> e.getParentId() != null).collect(Collectors.toList());

        List<ThingParamSpecDetailDto> parentDetailList = CopyUtils.listCopy(parentParamSpecDbList,
                ThingParamSpecDetailDto.class);
        // 数据规格子级
        if (CollectionUtils.isNotEmpty(childParamSpecDbList)) {
            parentDetailList.forEach(parent -> {
                List<ThingParamSpecEntity> childList = childParamSpecDbList.stream()
                        .filter(child -> Objects.equals(parent.getId(), child.getParentId()))
                        .collect(Collectors.toList());
                List<ThingParamSpecJsonElemDto> jsonElemList = CopyUtils.listCopy(
                        childList, ThingParamSpecJsonElemDto.class);
                parent.setJsonElemList(jsonElemList);
            });
        }
        detailDto.setParamSpecList(parentDetailList);
        return detailDto;
    }

    @Override
    public boolean isRepeat(Long id, Long thingId, Integer dimension, SFunction<ThingDimensionEntity, ?> func, String value) {
        LambdaQueryWrapper<ThingDimensionEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(ThingDimensionEntity::getIsDel, 0);
        lqw.eq(func, value);
        lqw.ne(ThingDimensionEntity::getThingId, thingId);
        lqw.ne(ThingDimensionEntity::getDimension, dimension);
        if (Objects.nonNull(id)) {
            lqw.ne(ThingDimensionEntity::getId, id);
        }
        return super.count(lqw) > 0;
    }

}
