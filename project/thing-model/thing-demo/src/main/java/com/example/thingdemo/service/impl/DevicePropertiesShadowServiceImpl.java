package com.example.thingdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.thingdemo.domain.DeviceEntity;
import com.example.thingdemo.domain.DevicePropertiesShadowEntity;
import com.example.thingdemo.domain.ThingDimensionEntity;
import com.example.thingdemo.domain.ThingParamSpecEntity;
import com.example.thingdemo.enums.DimensionEnum;
import com.example.thingdemo.mapper.DevicePropertiesShadowMapper;
import com.example.thingdemo.mapper.ThingDimensionMapper;
import com.example.thingdemo.mqtt.MqttSender;
import com.example.thingdemo.service.DeviceService;
import com.example.thingdemo.service.DevicePropertiesShadowService;
import com.example.thingdemo.service.ThingParamSpecService;
import com.example.thingdemo.util.CommonUtil;
import com.example.thingdemo.vo.DevicePropertyUpdateVo;
import com.example.thingdemo.vo.DevicePropertiesShadowInitVo;
import com.example.thingdemo.vo.DevicePropertiesShadowUpdateCurrentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备属性影子 服务实现类
 * </p>
 *
 * @author chenpq
 * @since 2023-05-07 21:53:04
 */
@Slf4j
@Service
public class DevicePropertiesShadowServiceImpl extends ServiceImpl<DevicePropertiesShadowMapper, DevicePropertiesShadowEntity> implements
    DevicePropertiesShadowService {

    @Autowired
    private DevicePropertiesShadowMapper devicePropertiesShadowMapper;
    @Autowired
    private ThingDimensionMapper thingDimensionMapper;

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private ThingParamSpecService thingParamSpecService;
    @Autowired
    private MqttSender mqttProviderSender;

    @Override
    public List<DevicePropertiesShadowEntity> getShadows(String productKey, String deviceCode) {
        LambdaQueryWrapper<DevicePropertiesShadowEntity> lqwShadow = Wrappers.lambdaQuery();
        lqwShadow.eq(DevicePropertiesShadowEntity::getProductKey, productKey);
        lqwShadow.eq(DevicePropertiesShadowEntity::getDeviceCode, deviceCode);
        return super.list(lqwShadow);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initDevicePropertiesShadow(DevicePropertiesShadowInitVo initVo) {
        LambdaQueryWrapper<DevicePropertiesShadowEntity> lqwRemoveOld = Wrappers.lambdaQuery();
        lqwRemoveOld.eq(DevicePropertiesShadowEntity::getProductKey, initVo.getProductKey());
        lqwRemoveOld.eq(DevicePropertiesShadowEntity::getDeviceCode, initVo.getDeviceCode());
        super.remove(lqwRemoveOld);

        List<ThingDimensionEntity> properties = thingDimensionMapper.getDimensions(initVo.getProductKey(), DimensionEnum.PROPERTIES.getCode());
        if (CollectionUtils.isEmpty(properties)) {
            return;
        }
        LambdaQueryWrapper<ThingParamSpecEntity> lqwSpecs = Wrappers.lambdaQuery();
        lqwSpecs.eq(ThingParamSpecEntity::getThingId, properties.get(0).getThingId());
        List<Long> idList = properties.stream().map(ThingDimensionEntity::getId).collect(Collectors.toList());
        lqwSpecs.in(ThingParamSpecEntity::getThingDimensionId, idList);
        List<ThingParamSpecEntity> paramSpecList = thingParamSpecService.list(lqwSpecs);

        ArrayList<DevicePropertiesShadowEntity> shadowList = new ArrayList<>();
        for (ThingDimensionEntity p : properties) {
            DevicePropertiesShadowEntity propertiesShadow = new DevicePropertiesShadowEntity();
            propertiesShadow.setProductKey(initVo.getProductKey());
            propertiesShadow.setDeviceCode(initVo.getDeviceCode());
            propertiesShadow.setIdentifier(p.getIdentifier());

            paramSpecList.stream()
                    .filter(e -> Objects.equals(p.getThingId(), e.getThingId())
                            && Objects.equals(p.getId(), e.getThingDimensionId()))
                    .findFirst()
                    .ifPresent(e -> propertiesShadow.setValueDataType(e.getDataType()));
            shadowList.add(propertiesShadow);
        }
        // 新增
        if (CollectionUtils.isNotEmpty(shadowList)) {
            super.saveBatch(shadowList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateExpectValue(DevicePropertyUpdateVo updateVo) {
        DeviceEntity deviceDb = deviceService.getById(updateVo.getDeviceId());
        if (deviceDb == null) {
            return false;
        }

        LambdaUpdateWrapper<DevicePropertiesShadowEntity> lwqUp = Wrappers.lambdaUpdate();
        lwqUp.eq(DevicePropertiesShadowEntity::getProductKey, deviceDb.getProductKey());
        lwqUp.eq(DevicePropertiesShadowEntity::getDeviceCode, deviceDb.getDeviceCode());
        lwqUp.eq(DevicePropertiesShadowEntity::getIdentifier, updateVo.getIdentifier());

        lwqUp.set(DevicePropertiesShadowEntity::getExpectValue, CommonUtil.objectToString(updateVo.getExpectValue()));
        boolean b = super.update(lwqUp);

        // 发送mqtt消息
        HashMap<String, Object> param = new HashMap<>();
        param.put(updateVo.getIdentifier(), updateVo.getExpectValue());
        mqttProviderSender.propertySet(deviceDb.getProductKey(), deviceDb.getDeviceCode(), param);

        return b;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCurrentValue(DevicePropertiesShadowUpdateCurrentVo updateVo) {
        LambdaUpdateWrapper<DevicePropertiesShadowEntity> lwqUp = Wrappers.lambdaUpdate();
        lwqUp.eq(DevicePropertiesShadowEntity::getProductKey, updateVo.getProductKey());
        lwqUp.eq(DevicePropertiesShadowEntity::getDeviceCode, updateVo.getDeviceCode());
        lwqUp.eq(DevicePropertiesShadowEntity::getIdentifier, updateVo.getIdentifier());

        lwqUp.set(DevicePropertiesShadowEntity::getCurrentValue, updateVo.getCurrentValue());
        return super.update(lwqUp);
    }


}
