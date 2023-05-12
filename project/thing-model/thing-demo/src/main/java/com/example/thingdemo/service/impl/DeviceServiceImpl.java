package com.example.thingdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.thingdemo.ao.DeviceAddUpdateAo;
import com.example.thingdemo.domain.DeviceEntity;
import com.example.thingdemo.domain.DevicePropertiesShadowEntity;
import com.example.thingdemo.dto.DeviceDetailDto;
import com.example.thingdemo.dto.DevicePropertiesShadowDto;
import com.example.thingdemo.exception.AppException;
import com.example.thingdemo.mapper.DeviceMapper;
import com.example.thingdemo.service.DeviceService;
import com.example.thingdemo.service.DevicePropertiesShadowService;
import com.example.thingdemo.util.CopyUtils;
import com.example.thingdemo.vo.DevicePropertiesShadowInitVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 设备 服务实现类
 * </p>
 *
 * @author chenpq
 * @since 2023-05-07 21:52:39
 */
@Slf4j
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, DeviceEntity> implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private DevicePropertiesShadowService devicePropertiesShadowService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(DeviceAddUpdateAo addUpdateAo) {
        if (isRepeat(null, addUpdateAo.getProductKey(),
                DeviceEntity::getDeviceCode, addUpdateAo.getDeviceCode())) {
            throw new AppException("修改失败，设备编码已存在。请修改设备编码。");
        }
        if (isRepeat(null, addUpdateAo.getProductKey(),
                DeviceEntity::getDeviceName, addUpdateAo.getDeviceName())) {
            throw new AppException("修改失败，设备名称已存在。请修改名称编码。");
        }
        DeviceEntity deviceEntity = new DeviceEntity();
        BeanUtils.copyProperties(addUpdateAo, deviceEntity);
        super.save(deviceEntity);
        Long id = deviceEntity.getId();

        // 初始化设备属性影子
        DevicePropertiesShadowInitVo initVo = new DevicePropertiesShadowInitVo();
        initVo.setProductKey(deviceEntity.getProductKey());
        initVo.setDeviceCode(deviceEntity.getDeviceCode());
        devicePropertiesShadowService.initDevicePropertiesShadow(initVo);

        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(DeviceAddUpdateAo addUpdateAo) {
        Long id = addUpdateAo.getId();
        DeviceEntity deviceDb = super.getById(id);
        if (deviceDb == null) {
            return false;
        }
        if (isRepeat(id, deviceDb.getProductKey(),
                DeviceEntity::getDeviceName, deviceDb.getDeviceName())) {
            throw new AppException("修改失败，设备名称已存在。请修改名称编码。");
        }
        DeviceEntity deviceEntity = new DeviceEntity();
        BeanUtils.copyProperties(addUpdateAo, deviceEntity);
        return super.updateById(deviceEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        DeviceEntity deviceDb = super.getById(id);
        if (deviceDb == null) {
            return false;
        }
        // 删除
        boolean b = super.removeById(id);

        // 删除设备属性影子
        LambdaQueryWrapper<DevicePropertiesShadowEntity> lqwShadow = Wrappers.lambdaQuery();
        lqwShadow.eq(DevicePropertiesShadowEntity::getDeviceCode, deviceDb.getDeviceCode());
        lqwShadow.eq(DevicePropertiesShadowEntity::getProductKey, deviceDb.getProductKey());
        devicePropertiesShadowService.remove(lqwShadow);

        return b;
    }

    @Override
    public DeviceDetailDto detail(Long id) {
        DeviceEntity deviceDb = super.getById(id);
        if (deviceDb == null) {
            return null;
        }
        DeviceDetailDto detailDto = new DeviceDetailDto();
        BeanUtils.copyProperties(deviceDb, detailDto);

        // 设备属性影子
        List<DevicePropertiesShadowEntity> shadowList = devicePropertiesShadowService.getShadows(deviceDb.getProductKey(),
                deviceDb.getDeviceCode());
        List<DevicePropertiesShadowDto> shadowDtoList = CopyUtils.listCopy(shadowList, DevicePropertiesShadowDto.class);
        detailDto.setShadowPropertiesList(shadowDtoList);
        return detailDto;
    }

    @Override
    public boolean isRepeat(Long id, String productKey, SFunction<DeviceEntity, ?> func, String value) {
        LambdaQueryWrapper<DeviceEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(DeviceEntity::getIsDel, 0);
        lqw.eq(DeviceEntity::getProductKey, productKey);
        lqw.eq(func, value);
        if (Objects.nonNull(id)) {
            lqw.ne(DeviceEntity::getId, id);
        }
        return super.count(lqw) > 0;
    }

}
