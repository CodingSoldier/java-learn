package com.example.thingdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.thingdemo.domain.TingParamSpecEntity;
import com.example.thingdemo.dto.TingParamSpecDetailDto;
import com.example.thingdemo.mapper.TingParamSpecMapper;
import com.example.thingdemo.service.TingParamSpecService;
import com.example.thingdemo.util.CopyUtils;
import com.example.thingdemo.vo.TingParamSpecAddUpdateVo;
import com.example.thingdemo.vo.TingParamSpecJsonElemVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public void addUpdate(Long tingId, Long tingDimensionId, List<TingParamSpecAddUpdateVo> addList) {
        // 删除旧数据
        LambdaQueryWrapper<TingParamSpecEntity> delLqw = Wrappers.lambdaQuery();
        delLqw.eq(TingParamSpecEntity::getTingId, tingId);
        delLqw.eq(TingParamSpecEntity::getTingDimensionId, tingDimensionId);
        super.remove(delLqw);

        if (CollectionUtils.isEmpty(addList)) {
            return;
        }

        // 无子级的数据
        List<TingParamSpecEntity> addEntityList = new ArrayList<>();
        addList.stream()
                .filter(e -> CollectionUtils.isEmpty(e.getJsonElemList()))
                .forEach(e -> {
                    TingParamSpecEntity addEntity = new TingParamSpecEntity();
                    BeanUtils.copyProperties(e, addEntity);
                    addEntity.setTingId(tingId);
                    addEntity.setTingDimensionId(tingDimensionId);
                    addEntity.setTingDimensionId(tingDimensionId);
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
                    TingParamSpecEntity parent = new TingParamSpecEntity();
                    BeanUtils.copyProperties(hasChild, parent);
                    parent.setTingId(tingId);
                    parent.setTingDimensionId(tingDimensionId);
                    super.save(parent);

                    // 保存子级
                    List<TingParamSpecJsonElemVo> jsonList = hasChild.getJsonElemList();
                    List<TingParamSpecEntity> childList = CopyUtils.listCopy(jsonList,
                            TingParamSpecEntity.class);
                    childList.forEach(child -> {
                        child.setTingId(tingId);
                        child.setTingDimensionId(tingDimensionId);
                        child.setParentId(parent.getId());
                    });
                    if (CollectionUtils.isNotEmpty(childList)) {
                        super.saveBatch(childList);
                    }
                });
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
    public boolean isRepeat(Long id, SFunction<TingParamSpecEntity, ?> func, String value) {
        LambdaQueryWrapper<TingParamSpecEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(TingParamSpecEntity::getIsDel, 0);
        lqw.eq(func, value);
        if (Objects.nonNull(id)) {
            lqw.ne(TingParamSpecEntity::getId, id);
        }
        return super.count(lqw) > 0;
    }

}
