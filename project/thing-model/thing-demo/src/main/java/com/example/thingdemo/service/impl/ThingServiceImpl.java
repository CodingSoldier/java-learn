package com.example.thingdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.thingdemo.ao.ThingAddUpdateAo;
import com.example.thingdemo.constant.RedisConstant;
import com.example.thingdemo.domain.ThingDimensionEntity;
import com.example.thingdemo.domain.ThingEntity;
import com.example.thingdemo.domain.ThingParamSpecEntity;
import com.example.thingdemo.dto.ThingDetailDto;
import com.example.thingdemo.dto.ThingDimensionDetailDto;
import com.example.thingdemo.dto.ThingParamSpecDetailDto;
import com.example.thingdemo.dto.ThingParamSpecJsonElemDto;
import com.example.thingdemo.enums.DimensionEnum;
import com.example.thingdemo.enums.InoutDataEnum;
import com.example.thingdemo.exception.AppException;
import com.example.thingdemo.mapper.ThingMapper;
import com.example.thingdemo.service.ThingDimensionService;
import com.example.thingdemo.service.ThingParamSpecService;
import com.example.thingdemo.service.ThingService;
import com.example.thingdemo.util.CommonUtil;
import com.example.thingdemo.util.CopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
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
public class ThingServiceImpl extends ServiceImpl<ThingMapper, ThingEntity> implements ThingService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThingMapper thingMapper;
    @Autowired
    private ThingDimensionService thingDimensionService;
    @Autowired
    private ThingParamSpecService thingParamSpecService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addUpdate(ThingAddUpdateAo addUpdateAo) {
        // id 为空，新增；id 不为空，修改
        Long id = addUpdateAo.getId();
        if (isRepeat(addUpdateAo.getId(), ThingEntity::getName, addUpdateAo.getName())) {
            throw new AppException("修改失败，产品名称已存在。请修改产品名称。");
        }
        ThingEntity thingEntity = new ThingEntity();
        BeanUtils.copyProperties(addUpdateAo, thingEntity);
        if (Objects.isNull(id)) {
            // 新增
            thingEntity.setProductKey(CommonUtil.uuid32());
            super.save(thingEntity);
            id = thingEntity.getId();
        } else {
            // 修改
            super.updateById(thingEntity);
        }

        // 删除缓存
        ThingEntity thingDb = super.getById(id);
        removeThingCache(thingDb.getProductKey());

        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        ThingEntity thingDb = super.getById(id);
        if (thingDb == null) {
            return false;
        }
        // 删除
        boolean b = super.removeById(id);

        LambdaQueryWrapper<ThingDimensionEntity> dimensionLqw = Wrappers.lambdaQuery();
        dimensionLqw.eq(ThingDimensionEntity::getThingId, id);
        thingDimensionService.remove(dimensionLqw);

        LambdaQueryWrapper<ThingParamSpecEntity> paramSpecLqw = Wrappers.lambdaQuery();
        paramSpecLqw.eq(ThingParamSpecEntity::getThingId, id);
        thingParamSpecService.remove(paramSpecLqw);

        // 删除缓存
        removeThingCache(thingDb.getProductKey());
        return b;
    }

    @Override
    public ThingDetailDto detail(Long id) {
        ThingEntity thingEntity = super.getById(id);
        if (thingEntity == null) {
            return null;
        }
        ThingDetailDto thingDetail = new ThingDetailDto();
        BeanUtils.copyProperties(thingEntity, thingDetail);

        // 维度
        LambdaQueryWrapper<ThingDimensionEntity> dimensionLqw = Wrappers.lambdaQuery();
        dimensionLqw.eq(ThingDimensionEntity::getThingId, id);
        List<ThingDimensionEntity> dimensionList = thingDimensionService.list(dimensionLqw);
        List<ThingDimensionDetailDto> dimensionDetailList = CopyUtils.listCopy(dimensionList,
                ThingDimensionDetailDto.class);
        thingDetail.setDimensionList(dimensionDetailList);

        if (CollectionUtils.isEmpty(dimensionList)) {
            return thingDetail;
        }

        // 数据规格
        List<Long> dimensionIdList = dimensionList.stream().map(ThingDimensionEntity::getId)
                .collect(Collectors.toList());
        LambdaQueryWrapper<ThingParamSpecEntity> paramSpecLqw = Wrappers.lambdaQuery();
        paramSpecLqw.eq(ThingParamSpecEntity::getThingId, id);
        paramSpecLqw.in(ThingParamSpecEntity::getThingDimensionId, dimensionIdList);
        List<ThingParamSpecEntity> paramSpecAllDbList = thingParamSpecService.list(paramSpecLqw);

        List<ThingParamSpecEntity> parentParamSpecDbList = paramSpecAllDbList.stream()
                .filter(e -> e.getParentId() == null).collect(Collectors.toList());
        List<ThingParamSpecEntity> childParamSpecDbList = paramSpecAllDbList.stream()
                .filter(e -> e.getParentId() != null).collect(Collectors.toList());

        dimensionDetailList.forEach(dimension -> {
            List<ThingParamSpecEntity> parentList = parentParamSpecDbList.stream()
                    .filter(paramSpec -> Objects.equals(dimension.getId(), paramSpec.getThingDimensionId()))
                    .collect(Collectors.toList());
            List<ThingParamSpecDetailDto> parentDetailList = CopyUtils.listCopy(parentList,
                    ThingParamSpecDetailDto.class);
            // 数据规格子级
            if (CollectionUtils.isNotEmpty(parentDetailList)) {
                parentDetailList.forEach(parent -> {
                    List<ThingParamSpecEntity> childList = childParamSpecDbList.stream()
                            .filter(child -> Objects.equals(parent.getId(), child.getParentId()))
                            .collect(Collectors.toList());
                    List<ThingParamSpecJsonElemDto> jsonElemList = CopyUtils.listCopy(
                            childList, ThingParamSpecJsonElemDto.class);
                    parent.setJsonElemList(jsonElemList);
                });
                dimension.setParamSpecList(parentDetailList);
            }
        });

        return thingDetail;
    }

    @Override
    public ThingCache getThingCache(String productKey) {
        String redisKey = RedisConstant.THING + productKey;
        Object o = redisTemplate.opsForValue().get(redisKey);
        if (o instanceof ThingCache) {
            ThingCache thingCache = (ThingCache) o;
            return thingCache;
        }

        LambdaQueryWrapper<ThingEntity> thingLqw = Wrappers.lambdaQuery();
        thingLqw.eq(ThingEntity::getProductKey, productKey);
        ThingEntity thingDb = super.getOne(thingLqw, false);
        if (thingDb == null) {
            return null;
        }
        ThingCache thingCache = new ThingCache();
        ThingProfileCache profile = new ThingProfileCache();
        BeanUtils.copyProperties(thingDb, profile);
        thingCache.setProfile(profile);

        ArrayList<ThingPropertiesCache> properties = new ArrayList<>();
        ArrayList<ThingEventCache> events = new ArrayList<>();
        ArrayList<ThingActionCache> actions = new ArrayList<>();
        thingCache.setProperties(properties);
        thingCache.setEvents(events);
        thingCache.setActions(actions);

        // 维度
        LambdaQueryWrapper<ThingDimensionEntity> dimensionLqw = Wrappers.lambdaQuery();
        dimensionLqw.eq(ThingDimensionEntity::getThingId, thingDb.getId());
        List<ThingDimensionEntity> dimensionList = thingDimensionService.list(dimensionLqw);

        // 数据规格
        List<Long> dimensionIdList = dimensionList.stream().map(ThingDimensionEntity::getId)
            .collect(Collectors.toList());
        LambdaQueryWrapper<ThingParamSpecEntity> paramSpecLqw = Wrappers.lambdaQuery();
        paramSpecLqw.eq(ThingParamSpecEntity::getThingId, thingDb.getId());
        paramSpecLqw.in(ThingParamSpecEntity::getThingDimensionId, dimensionIdList);
        List<ThingParamSpecEntity> paramSpecAllDbList = thingParamSpecService.list(paramSpecLqw);

        List<ThingParamSpecEntity> parentParamSpecDbList = paramSpecAllDbList.stream()
            .filter(e -> e.getParentId() == null).collect(Collectors.toList());
        List<ThingParamSpecEntity> childParamSpecDbList = paramSpecAllDbList.stream()
            .filter(e -> e.getParentId() != null).collect(Collectors.toList());

        // 维度遍历
        dimensionList.forEach(dimension -> {
            // 属性
            if (DimensionEnum.PROPERTIES.getCode().equals(dimension.getDimension())) {
                ThingPropertiesCache property = new ThingPropertiesCache();
                BeanUtils.copyProperties(dimension, property);
                parentParamSpecDbList.stream()
                    .filter(paramSpec -> Objects.equals(dimension.getId(), paramSpec.getThingDimensionId()))
                    .findFirst()
                    .ifPresent(e -> {
                            property.setDataType(e.getDataType());
                            property.setSpecs(e.getSpecs());
                    });
                properties.add(property);
            } else {
                // 事件、动作
                List<ThingParamSpecEntity> parentDimensionParamSpecList = parentParamSpecDbList.stream()
                    .filter(paramSpec -> Objects.equals(dimension.getId(), paramSpec.getThingDimensionId()))
                    .collect(Collectors.toList());
                List<ThingParamSpecCache> inputData = new ArrayList<>();
                List<ThingParamSpecCache> outputData = new ArrayList<>();
                // 每个维度的数据规则
                parentDimensionParamSpecList.forEach(parentEntity -> {
                    ThingParamSpecCache paramSpecParentCache = new ThingParamSpecCache();
                    BeanUtils.copyProperties(parentEntity, paramSpecParentCache);
                    // 判断是inputData还是outputData
                    if (InoutDataEnum.INPUT_DATA.getCode().equals(parentEntity.getInOutData())) {
                        inputData.add(paramSpecParentCache);
                    } else if (InoutDataEnum.OUTPUT_DATA.getCode().equals(parentEntity.getInOutData())) {
                        outputData.add(paramSpecParentCache);
                    }
                    // 设置子级数据规格
                    List<ThingParamSpecEntity> childList = childParamSpecDbList.stream()
                        .filter(child -> Objects.equals(parentEntity.getId(), child.getParentId()))
                        .collect(Collectors.toList());
                    List<ThingParamSpecJsonElemCache> jsonElemList = CopyUtils.listCopy(
                        childList, ThingParamSpecJsonElemCache.class);
                    paramSpecParentCache.setJsonElemList(jsonElemList);
                });

                if (DimensionEnum.EVENT.getCode().equals(dimension.getDimension())) {
                    ThingEventCache event = new ThingEventCache();
                    BeanUtils.copyProperties(dimension, event);
                    event.setOutputData(outputData);
                    events.add(event);
                } else if (DimensionEnum.ACTION.getCode().equals(dimension.getDimension())) {
                    ThingActionCache action = new ThingActionCache();
                    BeanUtils.copyProperties(dimension, action);
                    action.setInputData(inputData);
                    action.setOutputData(outputData);
                    actions.add(action);
                }
            }
        });
        redisTemplate.opsForValue().set(redisKey, thingCache, 10, TimeUnit.DAYS);
        return thingCache;
    }

    @Override
    public boolean removeThingCache(String productKey) {
        Boolean delete = redisTemplate.delete(RedisConstant.THING + productKey);
        removeThingVersionCache(productKey);
        return delete;
    }

    @Override
    public String getThingVersionCache(String productKey) {
        String redisKey = RedisConstant.THING_VERSION + productKey;
        Object o = redisTemplate.opsForValue().get(redisKey);
        String version = CommonUtil.objectToString(o);
        if (StringUtils.isNotBlank(version)) {
            return version;
        }

        LambdaQueryWrapper<ThingEntity> thingLqw = Wrappers.lambdaQuery();
        thingLqw.eq(ThingEntity::getProductKey, productKey);
        ThingEntity thingDb = super.getOne(thingLqw, false);
        if (thingDb == null) {
            return null;
        }
        version = thingDb.getVersion();
        redisTemplate.opsForValue().set(redisKey, version, 10, TimeUnit.DAYS);
        return version;
    }

    @Override
    public boolean removeThingVersionCache(String productKey) {
        return redisTemplate.delete(RedisConstant.THING_VERSION + productKey);
    }

    @Override
    public boolean isRepeat(Long id, SFunction<ThingEntity, ?> func, String value) {
        LambdaQueryWrapper<ThingEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(ThingEntity::getIsDel, 0);
        lqw.eq(func, value);
        if (Objects.nonNull(id)) {
            lqw.ne(ThingEntity::getId, id);
        }
        return super.count(lqw) > 0;
    }

}
