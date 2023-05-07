package com.example.thingdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.thingdemo.domain.DeviceShadowEntity;
import com.example.thingdemo.domain.TingDimensionEntity;
import com.example.thingdemo.domain.TingParamSpecEntity;
import com.example.thingdemo.enums.DimensionEnum;
import com.example.thingdemo.mapper.DeviceShadowMapper;
import com.example.thingdemo.mapper.TingDimensionMapper;
import com.example.thingdemo.service.DeviceShadowService;
import com.example.thingdemo.service.TingParamSpecService;
import com.example.thingdemo.vo.DeviceShadowInitVo;
import com.example.thingdemo.vo.DeviceShadowUpdateCurrentVo;
import com.example.thingdemo.vo.DeviceShadowUpdateExpectVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备影子 服务实现类
 * </p>
 *
 * @author chenpq
 * @since 2023-05-07 21:53:04
 */
@Slf4j
@Service
public class DeviceShadowServiceImpl extends ServiceImpl<DeviceShadowMapper, DeviceShadowEntity> implements DeviceShadowService {

    @Autowired
    private DeviceShadowMapper deviceShadowMapper;
    @Autowired
    private TingDimensionMapper tingDimensionMapper;
    @Autowired
    private TingParamSpecService tingParamSpecService;

    @Override
    public List<DeviceShadowEntity> getShadows(String productKey, String deviceCode) {
        LambdaQueryWrapper<DeviceShadowEntity> lqwShadow = Wrappers.lambdaQuery();
        lqwShadow.eq(DeviceShadowEntity::getProductKey, productKey);
        lqwShadow.eq(DeviceShadowEntity::getDeviceCode, deviceCode);
        return super.list(lqwShadow);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initDeviceShadow(DeviceShadowInitVo initVo) {
        LambdaQueryWrapper<DeviceShadowEntity> lqwRemoveOld = Wrappers.lambdaQuery();
        lqwRemoveOld.eq(DeviceShadowEntity::getProductKey, initVo.getProductKey());
        lqwRemoveOld.eq(DeviceShadowEntity::getDeviceCode, initVo.getDeviceCode());
        super.remove(lqwRemoveOld);

        List<TingDimensionEntity> properties = tingDimensionMapper.getDimensions(initVo.getProductKey(), DimensionEnum.PROPERTIES.getCode());
        if (CollectionUtils.isEmpty(properties)) {
            return;
        }
        LambdaQueryWrapper<TingParamSpecEntity> lqwSpecs = Wrappers.lambdaQuery();
        lqwSpecs.eq(TingParamSpecEntity::getTingId, properties.get(0).getTingId());
        List<Long> idList = properties.stream().map(TingDimensionEntity::getId).collect(Collectors.toList());
        lqwSpecs.in(TingParamSpecEntity::getTingDimensionId, idList);
        List<TingParamSpecEntity> paramSpecList = tingParamSpecService.list(lqwSpecs);

        ArrayList<DeviceShadowEntity> shadowList = new ArrayList<>();
        for (TingDimensionEntity p : properties) {
            DeviceShadowEntity deviceShadow = new DeviceShadowEntity();
            deviceShadow.setProductKey(initVo.getProductKey());
            deviceShadow.setDeviceCode(initVo.getDeviceCode());
            deviceShadow.setIdentifier(p.getIdentifier());

            paramSpecList.stream()
                    .filter(e -> Objects.equals(p.getTingId(), e.getTingId())
                            && Objects.equals(p.getId(), e.getTingDimensionId()))
                    .findFirst()
                    .ifPresent(e -> deviceShadow.setValueDataType(e.getDataType()));
            shadowList.add(deviceShadow);
        }
        // 新增
        if (CollectionUtils.isNotEmpty(shadowList)) {
            super.saveBatch(shadowList);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateExpectValue(DeviceShadowUpdateExpectVo updateVo) {
        LambdaUpdateWrapper<DeviceShadowEntity> lwqUp = Wrappers.lambdaUpdate();
        lwqUp.eq(DeviceShadowEntity::getProductKey, updateVo.getProductKey());
        lwqUp.eq(DeviceShadowEntity::getDeviceCode, updateVo.getDeviceCode());
        lwqUp.eq(DeviceShadowEntity::getIdentifier, updateVo.getIdentifier());

        lwqUp.set(DeviceShadowEntity::getExpectValue, updateVo.getExpectValue());
        return super.update(lwqUp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCurrentValue(DeviceShadowUpdateCurrentVo updateVo) {
        LambdaUpdateWrapper<DeviceShadowEntity> lwqUp = Wrappers.lambdaUpdate();
        lwqUp.eq(DeviceShadowEntity::getProductKey, updateVo.getProductKey());
        lwqUp.eq(DeviceShadowEntity::getDeviceCode, updateVo.getDeviceCode());
        lwqUp.eq(DeviceShadowEntity::getIdentifier, updateVo.getIdentifier());

        lwqUp.set(DeviceShadowEntity::getCurrentValue, updateVo.getCurrentValue());
        return super.update(lwqUp);
    }


}
