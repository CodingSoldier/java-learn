package com.cpq.mybatispulslearn.linyi.third_to_cottage.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpq.mybatispulslearn.linyi.third_to_cottage.entity.ThirdToCottage;
import com.cpq.mybatispulslearn.linyi.third_to_cottage.mapper.ThirdToCottageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 第三方系统、田丁后台的社区、房屋映射表 前端控制器
 * </p>
 *
 * @author cpq
 * @since 2019-08-07
 */
@RestController
@RequestMapping("/third_to_cottage/thirdToCottage")
public class ThirdToCottageController {

    @Autowired
    private ThirdToCottageMapper thirdToCottageMapper;

    @GetMapping("/test")
    public String test() {
        LambdaQueryWrapper<ThirdToCottage> lqw = Wrappers.lambdaQuery();
        List<ThirdToCottage> thirdToCottages = thirdToCottageMapper.selectList(lqw);
        System.out.println(thirdToCottages);
        return "";
    }

}
