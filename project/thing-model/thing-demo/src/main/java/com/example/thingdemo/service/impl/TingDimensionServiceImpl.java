package com.example.thingdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.thingdemo.domain.TingDimensionEntity;
import com.example.thingdemo.domain.TingEntity;
import com.example.thingdemo.domain.TingParamSpecEntity;
import com.example.thingdemo.dto.TingDimensionDetailDto;
import com.example.thingdemo.dto.TingParamSpecDetailDto;
import com.example.thingdemo.dto.TingParamSpecJsonElemDto;
import com.example.thingdemo.enums.DimensionEnum;
import com.example.thingdemo.exception.AppException;
import com.example.thingdemo.mapper.TingDimensionMapper;
import com.example.thingdemo.service.TingDimensionService;
import com.example.thingdemo.service.TingParamSpecService;
import com.example.thingdemo.service.TingService;
import com.example.thingdemo.util.CopyUtils;
import com.example.thingdemo.vo.DimensionAddVo;
import com.example.thingdemo.vo.DimensionUpdateVo;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private TingParamSpecService tingParamSpecService;
    @Autowired
    private TingService tingService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBatch(Long tingId, List<DimensionAddVo> dimensionList) {
        if (CollectionUtils.isEmpty(dimensionList)) {
            throw new AppException("dimensionList不能为空。");
        }

        // 校验Identifier是否重复
        for (DimensionAddVo vo : dimensionList) {
            isRepeat(null, tingId, vo.getDimension(), TingDimensionEntity::getIdentifier, vo.getIdentifier());
        }

        // 新增维度
        for (DimensionAddVo addVo : dimensionList) {
            TingDimensionEntity tingDimensionEntity = new TingDimensionEntity();
            BeanUtils.copyProperties(addVo, tingDimensionEntity);
            tingDimensionEntity.setTingId(tingId);
            super.save(tingDimensionEntity);
            Long dimensionId = tingDimensionEntity.getId();

            // 新增数据规格
            tingParamSpecService.addUpdate(tingId, dimensionId, addVo.getParamSpecList());
        }

        TingEntity tingDb = tingService.getById(tingId);
        if (tingDb != null) {
            // 删除缓存
            tingService.removeTingCache(tingDb.getProductKey());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(DimensionUpdateVo updateVo) {
        TingDimensionEntity dimensionDb = super.getById(updateVo.getId());
        if (dimensionDb == null) {
            return false;
        }

        isRepeat(updateVo.getId(), dimensionDb.getTingId(), dimensionDb.getDimension(), TingDimensionEntity::getIdentifier, updateVo.getIdentifier());

        // 校验
        DimensionAddVo dimensionAddVo = new DimensionAddVo();
        BeanUtils.copyProperties(updateVo, dimensionAddVo);
        valid(dimensionAddVo);

        TingDimensionEntity tingDimensionEntity = new TingDimensionEntity();
        BeanUtils.copyProperties(updateVo, tingDimensionEntity);

        if (DimensionEnum.PROPERTIES.getCode().equals(tingDimensionEntity.getDimension())) {
            tingDimensionEntity.setEventType(null);
            tingDimensionEntity.setActionCallType(null);
        }
        if (DimensionEnum.EVENT.getCode().equals(tingDimensionEntity.getDimension())) {
            tingDimensionEntity.setPropertiesAccessMode("");
            tingDimensionEntity.setActionCallType(null);
        }
        if (DimensionEnum.ACTION.getCode().equals(tingDimensionEntity.getDimension())) {
            tingDimensionEntity.setPropertiesAccessMode("");
            tingDimensionEntity.setEventType(null);
        }

        super.updateById(tingDimensionEntity);
        Long dimensionId = dimensionDb.getId();
        Long tingId = dimensionDb.getTingId();

        // 新增数据规格
        tingParamSpecService.addUpdate(tingId, dimensionId, updateVo.getParamSpecList());

        TingEntity tingDb = tingService.getById(tingId);
        if (tingDb != null) {
            // 删除缓存
            tingService.removeTingCache(tingDb.getProductKey());
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
    public boolean delete(Long tingId, Long id) {
        TingEntity tingDb = tingService.getById(tingId);

        LambdaQueryWrapper<TingDimensionEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(TingDimensionEntity::getTingId, tingId);
        lqw.eq(TingDimensionEntity::getId, id);
        boolean b = super.remove(lqw);

        LambdaQueryWrapper<TingParamSpecEntity> paramSpecLqw = Wrappers.lambdaQuery();
        paramSpecLqw.eq(TingParamSpecEntity::getTingId, tingId);
        paramSpecLqw.eq(TingParamSpecEntity::getTingDimensionId, id);
        tingParamSpecService.remove(paramSpecLqw);

        if (tingDb != null) {
            // 删除缓存
            tingService.removeTingCache(tingDb.getProductKey());
        }

        return b;
    }

    @Override
    public TingDimensionDetailDto detail(Long tingId, Long id) {
        LambdaQueryWrapper<TingDimensionEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(TingDimensionEntity::getTingId, tingId);
        lqw.eq(TingDimensionEntity::getId, id);
        TingDimensionEntity dbEntity = super.getOne(lqw, false);
        if (dbEntity == null) {
            return null;
        }
        TingDimensionDetailDto detailDto = new TingDimensionDetailDto();
        BeanUtils.copyProperties(dbEntity, detailDto);

        // 数据规格
        LambdaQueryWrapper<TingParamSpecEntity> paramSpecLqw = Wrappers.lambdaQuery();
        paramSpecLqw.eq(TingParamSpecEntity::getTingDimensionId, id);
        List<TingParamSpecEntity> paramSpecAllDbList = tingParamSpecService.list(paramSpecLqw);

        List<TingParamSpecEntity> parentParamSpecDbList = paramSpecAllDbList.stream()
                .filter(e -> e.getParentId() == null).collect(Collectors.toList());
        List<TingParamSpecEntity> childParamSpecDbList = paramSpecAllDbList.stream()
                .filter(e -> e.getParentId() != null).collect(Collectors.toList());

        List<TingParamSpecDetailDto> parentDetailList = CopyUtils.listCopy(parentParamSpecDbList,
                TingParamSpecDetailDto.class);
        // 数据规格子级
        if (CollectionUtils.isNotEmpty(childParamSpecDbList)) {
            parentDetailList.forEach(parent -> {
                List<TingParamSpecEntity> childList = childParamSpecDbList.stream()
                        .filter(child -> Objects.equals(parent.getId(), child.getParentId()))
                        .collect(Collectors.toList());
                List<TingParamSpecJsonElemDto> jsonElemList = CopyUtils.listCopy(
                        childList, TingParamSpecJsonElemDto.class);
                parent.setJsonElemList(jsonElemList);
            });
        }
        detailDto.setParamSpecList(parentDetailList);
        return detailDto;
    }

    @Override
    public boolean isRepeat(Long id, Long tingId, Integer dimension, SFunction<TingDimensionEntity, ?> func, String value) {
        LambdaQueryWrapper<TingDimensionEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(TingDimensionEntity::getIsDel, 0);
        lqw.eq(func, value);
        lqw.ne(TingDimensionEntity::getTingId, tingId);
        lqw.ne(TingDimensionEntity::getDimension, dimension);
        if (Objects.nonNull(id)) {
            lqw.ne(TingDimensionEntity::getId, id);
        }
        return super.count(lqw) > 0;
    }

}
