package com.mybatis;

import com.mybatis.mapper.HrDepartmentMapper;
import com.mybatis.model.HrDepartment;
import com.utils.BaseTest;
import org.junit.Test;

import javax.annotation.Resource;

public class T extends BaseTest {
    @Resource
    HrDepartmentMapper mapper;
    @Test
    public void t1() throws Exception{
    /*type在数据库中是枚举类型，生成的pojo是String类型，数据库中列的值必须是特定字段，可以使用枚举类型*/
        HrDepartment hr = new HrDepartment();
        //hr.setType("GOOD1");  //插入失败，不是枚举值
        //hr.setType("1");  //坑爹，可根据枚举下标插入值，定义
        hr.setType("GOOD");  //插入成功
        hr.setName("名字111");
        mapper.insert(hr);
    }
}
