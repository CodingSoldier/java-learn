package com.example.thingdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.thingdemo.domain.ThingParamSpecEntity;
import com.example.thingdemo.dto.ThingParamSpecDetailDto;
import com.example.thingdemo.mapper.ThingParamSpecMapper;
import com.example.thingdemo.service.ThingParamSpecService;
import com.example.thingdemo.util.CopyUtils;
import com.example.thingdemo.vo.ThingParamSpecAddUpdateVo;
import com.example.thingdemo.vo.ThingParamSpecJsonElemVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
public class ThingParamSpecServiceImpl extends ServiceImpl<ThingParamSpecMapper, ThingParamSpecEntity> implements ThingParamSpecService {

    @Autowired
    private ThingParamSpecMapper thingParamSpecMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUpdate(Long thingId, Long thingDimensionId, List<ThingParamSpecAddUpdateVo> addList) {
        // 删除旧数据
        LambdaQueryWrapper<ThingParamSpecEntity> delLqw = Wrappers.lambdaQuery();
        delLqw.eq(ThingParamSpecEntity::getThingId, thingId);
        delLqw.eq(ThingParamSpecEntity::getThingDimensionId, thingDimensionId);
        super.remove(delLqw);

        if (CollectionUtils.isEmpty(addList)) {
            return;
        }

        // 无子级的数据
        List<ThingParamSpecEntity> addEntityList = new ArrayList<>();
        addList.stream()
                .filter(e -> CollectionUtils.isEmpty(e.getJsonElemList()))
                .forEach(e -> {
                    ThingParamSpecEntity addEntity = new ThingParamSpecEntity();
                    BeanUtils.copyProperties(e, addEntity);
                    addEntity.setThingId(thingId);
                    addEntity.setThingDimensionId(thingDimensionId);
                    addEntity.setThingDimensionId(thingDimensionId);
                    addEntityList.add(addEntity);
                });
        if (CollectionUtils.isNotEmpty(addEntityList)) {
            super.saveBatch(addEntityList);
        }

        // 有子级的数据
        addList.stream()
                .filter(e -> CollectionUtils.isNotEmpty(e.getJsonElemList()))
                .forEach(hasChild -> {
                    // 保存父级
                    ThingParamSpecEntity parent = new ThingParamSpecEntity();
                    BeanUtils.copyProperties(hasChild, parent);
                    parent.setThingId(thingId);
                    parent.setThingDimensionId(thingDimensionId);
                    super.save(parent);

                    // 保存子级
                    List<ThingParamSpecJsonElemVo> jsonList = hasChild.getJsonElemList();
                    List<ThingParamSpecEntity> childList = CopyUtils.listCopy(jsonList,
                            ThingParamSpecEntity.class);
                    childList.forEach(child -> {
                        child.setThingId(thingId);
                        child.setThingDimensionId(thingDimensionId);
                        child.setParentId(parent.getId());
                    });
                    if (CollectionUtils.isNotEmpty(childList)) {
                        super.saveBatch(childList);
                    }
                });
    }

    @Override
    public ThingParamSpecDetailDto detail(Long id) {
        ThingParamSpecEntity thingParamSpecEntity = super.getById(id);
        if (thingParamSpecEntity == null) {
            return null;
        }
        ThingParamSpecDetailDto detailDto = new ThingParamSpecDetailDto();
        BeanUtils.copyProperties(thingParamSpecEntity, detailDto);
        return detailDto;
    }


}
