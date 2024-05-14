package com.cpq.mybatispulslearn.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpq.mybatispulslearn.linyi.third_to_cottage.entity.ThirdToCottage;
import com.cpq.mybatispulslearn.linyi.third_to_cottage.mapper.ThirdToCottageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskConfig {

    @Autowired
    private ThirdToCottageMapper thirdToCottageMapper;

    public String getCron() {
        ThirdToCottage thirdToCottage = thirdToCottageMapper.selectOne(Wrappers.lambdaQuery());
        return thirdToCottage.getThirdVillageCode();
    }
}
